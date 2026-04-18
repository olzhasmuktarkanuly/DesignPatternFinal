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
import com.mygdx.game.core.Assets;
import com.mygdx.game.core.MainGame;
import com.mygdx.game.entities.Bullet;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.Player;
import com.mygdx.game.world.LevelData;
import com.mygdx.game.world.LevelManager;
import com.mygdx.game.world.ZombieSpawner;

public class GameScreen extends ScreenAdapter {

    public static final float WORLD_WIDTH = 2000f;
    public static final float WORLD_HEIGHT = 2000f;
    private static final int TILE_SIZE = 32;

    private final MainGame game;
    private final ZombieSpawner zombieSpawner = new ZombieSpawner();

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    private Player player;
    private Array<Enemy> enemies;
    private Array<Bullet> bullets;
    private Array<Rectangle> walls;
    private Rectangle exitZone;

    private int levelIndex = 0;
    private int kills = 0;
    private boolean victory = false;

    private float shootCooldown = 0.2f;
    private float shootTimer = 0f;

    public GameScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        batch = new SpriteBatch();
        font = new BitmapFont();
        bullets = new Array<>();

        loadLevel(levelIndex);
    }

    private void loadLevel(int index) {
        LevelData level = LevelManager.createLevel(index);

        player = new Player(level.playerStartX, level.playerStartY, 32, 32);
        enemies = level.enemies;
        walls = level.walls;
        exitZone = level.exitZone;
        bullets.clear();

        zombieSpawner.configureForLevel(index);
        zombieSpawner.reset();
    }

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
        drawBullets();
        drawPlayer();
        drawEnemies();
        drawHud();
        batch.end();
    }

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

        shootTimer -= delta;

        handlePlayerInput(delta);
        keepPlayerInsideWorld();
        zombieSpawner.update(delta, player, enemies, levelIndex, walls, exitZone);
        updateBullets(delta);
        updateEnemies(delta);
        handleShooting();
        checkExitZone();
        centerCameraOnPlayer();
    }

    private void handlePlayerInput(float delta) {
        float oldX = player.getBounds().x;
        float oldY = player.getBounds().y;

        float dx = 0f;
        float dy = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += 1f;

        if (dx != 0f && dy != 0f) {
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            dx /= len;
            dy /= len;
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
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && shootTimer <= 0f) {
            shootTimer = shootCooldown;

            Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mouse);

            float dirX = mouse.x - player.getCenterX();
            float dirY = mouse.y - player.getCenterY();

            bullets.add(new Bullet(player.getCenterX(), player.getCenterY(), dirX, dirY));
        }
    }

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
            if (!bullets.get(i).isActive()) {
                bullets.removeIndex(i);
            }
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

        if (b.x < 0) b.x = 0;
        if (b.y < 0) b.y = 0;
        if (b.x + b.width > WORLD_WIDTH) b.x = WORLD_WIDTH - b.width;
        if (b.y + b.height > WORLD_HEIGHT) b.y = WORLD_HEIGHT - b.height;
    }

    private void centerCameraOnPlayer() {
        float targetX = player.getCenterX();
        float targetY = player.getCenterY();

        float halfW = camera.viewportWidth / 2f;
        float halfH = camera.viewportHeight / 2f;

        if (targetX < halfW) targetX = halfW;
        if (targetY < halfH) targetY = halfH;
        if (targetX > WORLD_WIDTH - halfW) targetX = WORLD_WIDTH - halfW;
        if (targetY > WORLD_HEIGHT - halfH) targetY = WORLD_HEIGHT - halfH;

        camera.position.set(targetX, targetY, 0f);
    }

    private void drawFloor() {
        for (int x = 0; x < WORLD_WIDTH; x += TILE_SIZE) {
            for (int y = 0; y < WORLD_HEIGHT; y += TILE_SIZE) {
                batch.draw(Assets.floorTexture, x, y, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void drawWalls() {
        for (Rectangle wall : walls) {
            batch.draw(Assets.wallTexture, wall.x, wall.y, wall.width, wall.height);
        }
    }

    private void drawExitZone() {
        if (!victory) {
            batch.draw(Assets.safeZoneTexture, exitZone.x, exitZone.y, exitZone.width, exitZone.height);
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

    private void drawHud() {
        float left = camera.position.x - 390f;
        float top = camera.position.y + 285f;

        font.setColor(Color.WHITE);
        font.draw(batch, "HP: " + player.getHp(), left, top);
        font.draw(batch, "Kills: " + kills, left, top - 25f);
        font.draw(batch, "Map: " + (victory ? "Completed" : (levelIndex + 1) + "/4"), left, top - 50f);
        font.draw(batch, "Objective: Find the safe zone", left, top - 75f);
        font.draw(batch, "Zombies respawn infinitely", left, top - 100f);

        if (player.isDead()) {
            font.draw(batch, "GAME OVER - Press R to restart or ESC for menu", camera.position.x - 145f, camera.position.y);
        }

        if (victory) {
            font.draw(batch, "YOU ESCAPED! Press R to restart or ESC for menu", camera.position.x - 160f, camera.position.y);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
