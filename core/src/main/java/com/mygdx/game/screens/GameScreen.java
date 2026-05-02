package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.world.AcidPool;
import com.mygdx.game.core.HealEffect;
import com.mygdx.game.world.LevelData;
import com.mygdx.game.world.Loot;
import com.mygdx.game.enemies.SpitterZombie;
import com.mygdx.game.world.ZombieSpawner;
import com.mygdx.game.core.Assets;
import com.mygdx.game.core.MainGame;
import com.mygdx.game.core.PoisonEffect;
import com.mygdx.game.entities.Bullet;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.Player;
import com.mygdx.game.world.LevelManager;


public class GameScreen extends ScreenAdapter {

    public static final float WORLD_WIDTH  = 2000f;
    public static final float WORLD_HEIGHT = 2000f;
    private static final int  TILE_SIZE    = 32;

    private final MainGame game;
    private final ZombieSpawner zombieSpawner = new ZombieSpawner();

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    private Player player;
    private Array<Enemy> enemies;
    private Array<Bullet>   bullets;
    private Array<Rectangle> walls;
    private Array<Loot> loot;
    private Array<AcidPool> acidPools;
    private Rectangle exitZone;

    private int levelIndex = 0;
    private int kills      = 0;
    private boolean victory = false;

    private float shootCooldown = 0.2f;
    private float shootTimer    = 0f;

    // Мигание UI при отсутствии патронов
    private float noAmmoFlash = 0f;

    public GameScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        batch   = new SpriteBatch();
        font    = new BitmapFont();
        bullets   = new Array<>();
        acidPools = new Array<>();
        loadLevel(levelIndex);
    }

    // ─── Загрузка уровня ──────────────────────────────────────────────────────

    private void loadLevel(int index) {
        LevelData level = LevelManager.createLevel(index);

        player   = new Player(level.playerStartX, level.playerStartY, 32, 32);
        enemies  = level.enemies;
        walls    = level.walls;

        exitZone = level.exitZone;

        bullets.clear();
        acidPools.clear();

        // Инициализируем SpitterZombie общим списком луж
        for (Enemy e : enemies) {
            if (e instanceof SpitterZombie) {
                ((SpitterZombie) e).setAcidPools(acidPools);
            }
        }

        zombieSpawner.setAcidPools(acidPools);
        zombieSpawner.configureForLevel(index);
        zombieSpawner.reset();
    }

    // ─── Render ───────────────────────────────────────────────────────────────

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0.06f, 0.06f, 0.06f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        drawFloor();
        drawWalls();
        drawExitZone();
        drawAcidPools();
        drawLoot();
        drawBullets();
        drawPlayer();
        drawEnemies();
        drawHud();
        batch.end();
    }

    // ─── Update ───────────────────────────────────────────────────────────────

    private void update(float delta) {
        if (victory || player.isDead()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                levelIndex = 0;
                kills = 0;
                victory = false;
                loadLevel(levelIndex);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                game.setScreen(new MenuScreen(game));
            }
            return;
        }

        shootTimer  -= delta;
        noAmmoFlash -= delta;

        player.update(delta);                    // обновляем Decorator-эффекты
        handlePlayerInput(delta);
        keepPlayerInsideWorld();
        zombieSpawner.update(delta, player, enemies, levelIndex, walls, exitZone);
        updateBullets(delta);
        updateEnemies(delta);
        updateAcidPools(delta);
        updateLoot();
        handleShooting();
        checkExitZone();
        centerCameraOnPlayer();
    }

    // ─── Ввод игрока ──────────────────────────────────────────────────────────

    private void handlePlayerInput(float delta) {
        float oldX = player.getBounds().x;
        float oldY = player.getBounds().y;
        float dx = 0f, dy = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += 1f;

        if (dx != 0f && dy != 0f) {
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            dx /= len; dy /= len;
        }

        player.move(dx, dy, delta);

        for (Rectangle wall : walls) {
            if (player.getBounds().overlaps(wall)) {
                player.getBounds().x = oldX;
                player.getBounds().y = oldY;
                break;
            }
        }
    }

    private void handleShooting() {
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT) || shootTimer > 0f) return;

        if (!player.hasAmmo()) {
            noAmmoFlash = 0.4f; // мигание "NO AMMO"
            return;
        }

        shootTimer = shootCooldown;
        player.useAmmo(1);

        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);
        float dirX = mouse.x - player.getCenterX();
        float dirY = mouse.y - player.getCenterY();
        bullets.add(new Bullet(player.getCenterX(), player.getCenterY(), dirX, dirY));
    }

    // ─── Обновление объектов ──────────────────────────────────────────────────

    private void updateBullets(float delta) {
        for (Bullet bullet : bullets) {
            bullet.update(delta);

            if (bullet.getBounds().x < 0 || bullet.getBounds().x > WORLD_WIDTH ||
                bullet.getBounds().y < 0 || bullet.getBounds().y > WORLD_HEIGHT) {
                bullet.deactivate();
            }

            for (Rectangle wall : walls) {
                if (bullet.isActive() && bullet.getBounds().overlaps(wall)) {
                    bullet.deactivate();
                }
            }

            for (Enemy enemy : enemies) {
                if (bullet.isActive() && bullet.getBounds().overlaps(enemy.getBounds())) {
                    enemy.takeDamage(bullet.getDamage());
                    bullet.deactivate();
                    break;
                }
            }
        }

        for (int i = bullets.size - 1; i >= 0; i--) {
            if (!bullets.get(i).isActive()) bullets.removeIndex(i);
        }
    }

    private void updateEnemies(float delta) {
        for (Enemy enemy : enemies) {
            float oldX = enemy.getBounds().x;
            float oldY = enemy.getBounds().y;

            enemy.update(player, delta);

            for (Rectangle wall : walls) {
                if (enemy.getBounds().overlaps(wall)) {
                    enemy.getBounds().x = oldX;
                    enemy.getBounds().y = oldY;
                    break;
                }
            }

            if (enemy.getBounds().overlaps(player.getBounds()) && enemy.canAttack()) {
                player.takeDamage(enemy.getDamage());
                enemy.resetAttackTimer();
            }
        }

        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            if (enemy.isDead()) {
                enemy.onDeath(player);
                enemies.removeIndex(i);
                kills++;
            }
        }
    }

    private void updateAcidPools(float delta) {
        for (int i = acidPools.size - 1; i >= 0; i--) {
            AcidPool pool = acidPools.get(i);
            pool.update(player, delta);
            if (pool.isExpired()) acidPools.removeIndex(i);
        }
    }

    private void updateLoot() {
        for (int i = loot.size - 1; i >= 0; i--) {
            Loot item = loot.get(i);
            if (!item.isPickedUp() && item.getBounds().overlaps(player.getBounds())) {
                item.applyTo(player);
                item.pickUp();
                loot.removeIndex(i);
            }
        }
    }

    private void checkExitZone() {
        if (player.getBounds().overlaps(exitZone)) {
            levelIndex++;
            if (levelIndex >= 4) {
                victory = true;
            } else {
                loadLevel(levelIndex);
            }
        }
    }

    private void keepPlayerInsideWorld() {
        Rectangle b = player.getBounds();
        if (b.x < 0)                     b.x = 0;
        if (b.y < 0)                     b.y = 0;
        if (b.x + b.width  > WORLD_WIDTH)  b.x = WORLD_WIDTH  - b.width;
        if (b.y + b.height > WORLD_HEIGHT) b.y = WORLD_HEIGHT - b.height;
    }

    private void centerCameraOnPlayer() {
        float tx = player.getCenterX();
        float ty = player.getCenterY();
        float hw = camera.viewportWidth  / 2f;
        float hh = camera.viewportHeight / 2f;

        if (tx < hw)               tx = hw;
        if (ty < hh)               ty = hh;
        if (tx > WORLD_WIDTH  - hw) tx = WORLD_WIDTH  - hw;
        if (ty > WORLD_HEIGHT - hh) ty = WORLD_HEIGHT - hh;

        camera.position.set(tx, ty, 0f);
    }

    // ─── Draw ─────────────────────────────────────────────────────────────────

    private void drawFloor() {
        for (int x = 0; x < WORLD_WIDTH; x += TILE_SIZE)
            for (int y = 0; y < WORLD_HEIGHT; y += TILE_SIZE)
                batch.draw(Assets.floorTexture, x, y, TILE_SIZE, TILE_SIZE);
    }

    private void drawWalls() {
        for (Rectangle wall : walls)
            batch.draw(Assets.wallTexture, wall.x, wall.y, wall.width, wall.height);
    }

    private void drawExitZone() {
        if (!victory)
            batch.draw(Assets.safeZoneTexture, exitZone.x, exitZone.y, exitZone.width, exitZone.height);
    }

    private void drawAcidPools() {
        for (AcidPool pool : acidPools) {
            Rectangle b = pool.getBounds();
            batch.draw(pool.getTexture(), b.x, b.y, b.width, b.height);
        }
    }

    private void drawLoot() {
        for (Loot item : loot) {
            Rectangle b = item.getBounds();
            batch.draw(item.getTexture(), b.x, b.y, b.width, b.height);
        }
    }

    private void drawPlayer() {
        if (!player.isDead()) {
            Rectangle p = player.getBounds();
            batch.draw(Assets.playerTexture, p.x, p.y, p.width, p.height);
        }
    }

    private void drawEnemies() {
        for (Enemy enemy : enemies) {
            Rectangle z = enemy.getBounds();
            batch.draw(enemy.getTexture(), z.x, z.y, z.width, z.height);
        }
    }

    private void drawBullets() {
        for (Bullet bullet : bullets) {
            Rectangle b = bullet.getBounds();
            batch.draw(Assets.bulletTexture, b.x, b.y, b.width, b.height);
        }
    }

    // ─── HUD ──────────────────────────────────────────────────────────────────

    private void drawHud() {
        float left = camera.position.x - 390f;
        float top  = camera.position.y + 285f;

        // HP — зелёный если > 50, жёлтый если > 25, красный если низкий
        if      (player.getHp() > 50) font.setColor(Color.GREEN);
        else if (player.getHp() > 25) font.setColor(Color.YELLOW);
        else                           font.setColor(Color.RED);
        font.draw(batch, "HP:    " + player.getHp() + " / " + player.getMaxHp(), left, top);

        // Ammo — красный мигает если пусто
        if (noAmmoFlash > 0f) {
            font.setColor(Color.RED);
            font.draw(batch, "AMMO:  NO AMMO!", left, top - 25f);
        } else {
            font.setColor(player.getAmmo() > 0 ? Color.WHITE : Color.RED);
            font.draw(batch, "AMMO:  " + player.getAmmo() + " / " + player.getMaxAmmo(), left, top - 25f);
        }

        font.setColor(Color.WHITE);
        font.draw(batch, "LEVEL: " + (victory ? "DONE" : (levelIndex + 1) + " / 4"), left, top - 50f);
        font.draw(batch, "KILLS: " + kills, left, top - 75f);

        // Активные эффекты (Decorator) — показываем иконки
        float effectX = left;
        float effectY = top - 105f;
        if (player.hasEffect(PoisonEffect.class)) {
            font.setColor(Color.GREEN);
            font.draw(batch, "[POISON]", effectX, effectY);
            effectX += 90f;
        }
        if (player.hasEffect(HealEffect.class)) {
            font.setColor(new Color(0.4f, 1f, 0.4f, 1f));
            font.draw(batch, "[HEALING]", effectX, effectY);
        }

        font.setColor(Color.LIGHT_GRAY);
        font.draw(batch, "Safe zone -> blue zone", left, top - 130f);

        if (player.isDead()) {
            font.setColor(Color.RED);
            font.draw(batch, "GAME OVER — Press R to restart or ESC for menu",
                camera.position.x - 180f, camera.position.y);
        }

        if (victory) {
            font.setColor(Color.YELLOW);
            font.draw(batch, "YOU ESCAPED! — Press R to restart or ESC for menu",
                camera.position.x - 190f, camera.position.y);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        AcidPool.disposeTexture();
    }
}
