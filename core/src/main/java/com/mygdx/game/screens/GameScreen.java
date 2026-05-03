package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
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
import com.mygdx.game.entities.AK47;
import com.mygdx.game.entities.Bullet;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.Pistol;
import com.mygdx.game.entities.Player;
import com.mygdx.game.entities.Sniper;
import com.mygdx.game.entities.Weapon;
import com.mygdx.game.world.LevelData;
import com.mygdx.game.world.LevelManager;
import com.mygdx.game.world.ZombieSpawner;


public class GameScreen extends ScreenAdapter {

    public static final float WORLD_WIDTH = 2000f;
    public static final float WORLD_HEIGHT = 2000f;
    private static final int TILE_SIZE = 32;
    private static final float MEDKIT_USE_TIME = 3f;
    private static final float PUSH_COOLDOWN = 0.45f;
    private static final float PUSH_RANGE = 65f;
    private static final float PUSH_DISTANCE = 45f;

    private final MainGame game;
    private final ZombieSpawner zombieSpawner = new ZombieSpawner();

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    private Player player;
    private Array<Enemy> enemies;
    private Array<Bullet> bullets;
    private Array<Rectangle> walls;
    private Array<Pickup> pickups;

    private Rectangle startSafeZone;
    private Rectangle exitZone;

    private int levelIndex = 0;
    private int kills = 0;
    private boolean victory = false;

    private float shootTimer = 0f;
    private float healUseTimer = 0f;
    private float pushTimer = 0f;
    private float meleeTimer = 0f;

    private HandSlot selectedSlot = HandSlot.NONE;

    private Weapon primaryWeapon = null;
    private Weapon pistolWeapon = null;
    private Weapon currentWeapon = null;

    private int primaryAmmo = 0;
    private int pistolAmmo = 0;

    private String meleeWeaponName = "None";
    private boolean hasMedkit = false;

    private String boostItemName = "None";
    private String throwableName = "None";
    private int throwableCount = 0;

    public GameScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(0.75f);

        bullets = new Array<>();
        pickups = new Array<>();

        resetInventory();
        loadLevel(levelIndex);
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (amountY > 0) {
                    selectNextSlot();
                } else if (amountY < 0) {
                    selectPreviousSlot();
                }

                return true;
            }
        });
    }

    private void resetInventory() {
        primaryWeapon = null;
        pistolWeapon = null;
        currentWeapon = null;

        primaryAmmo = 0;
        pistolAmmo = 0;

        meleeWeaponName = "None";
        hasMedkit = false;

        boostItemName = "None";
        throwableName = "None";
        throwableCount = 0;
        selectedSlot = HandSlot.NONE;

        shootTimer = 0f;
        healUseTimer = 0f;
        pushTimer = 0f;
        meleeTimer = 0f;
    }

    private void loadLevel(int index) {
        LevelData level = LevelManager.createLevel(index);

        player = new Player(level.playerStartX, level.playerStartY, 32, 32);
        enemies = level.enemies;
        walls = level.walls;
        exitZone = level.exitZone;

        startSafeZone = new Rectangle(
            Math.max(20, level.playerStartX - 80),
            Math.max(20, level.playerStartY - 80),
            360,
            230
        );

        bullets.clear();
        pickups.clear();

        createPickupsForLevel(index);

        zombieSpawner.configureForLevel(index);
        zombieSpawner.reset();
    }

    private void createPickupsForLevel(int index) {
        float sx = startSafeZone.x + 20;
        float sy = startSafeZone.y + 25;

        addSafeZoneLoadout(sx, sy);
        addMapLoot(index);
    }

    private void addSafeZoneLoadout(float sx, float sy) {
        float col = 70f;
        float row = 48f;

        addPickup(PickupType.PRIMARY_AK47, sx, sy);
        addPickup(PickupType.PRIMARY_SNIPER, sx + col, sy);
        addPickup(PickupType.PISTOL, sx + col * 2, sy);

        addPickup(PickupType.BAT, sx, sy + row);
        addPickup(PickupType.KATANA, sx + col, sy + row);
        addPickup(PickupType.SHOVEL, sx + col * 2, sy + row);

        addPickup(PickupType.MEDKIT, sx, sy + row * 2);
        addPickup(PickupType.PILLS, sx + col, sy + row * 2);
        addPickup(PickupType.ADRENALINE, sx + col * 2, sy + row * 2);

        addPickup(PickupType.PRIMARY_AMMO, sx, sy + row * 3);
        addPickup(PickupType.PISTOL_AMMO, sx + col, sy + row * 3);
        addPickup(PickupType.MOLOTOV, sx + col * 2, sy + row * 3);
        addPickup(PickupType.BOMB, sx + col * 3, sy + row * 3);
    }

    private void addMapLoot(int index) {
        if (index == 0) {
            addPickup(PickupType.PRIMARY_AMMO, 600, 420);
            addPickup(PickupType.PISTOL_AMMO, 850, 560);
            addPickup(PickupType.PILLS, 1100, 700);
        } else if (index == 1) {
            addPickup(PickupType.PRIMARY_AMMO, 700, 500);
            addPickup(PickupType.PISTOL_AMMO, 950, 650);
            addPickup(PickupType.MEDKIT, 1200, 780);
            addPickup(PickupType.ADRENALINE, 1450, 900);
            addPickup(PickupType.MOLOTOV, 1350, 620);
        } else if (index == 2) {
            addPickup(PickupType.PRIMARY_AMMO, 700, 520);
            addPickup(PickupType.PRIMARY_AMMO, 980, 840);
            addPickup(PickupType.PISTOL_AMMO, 1150, 720);
            addPickup(PickupType.MEDKIT, 1300, 950);
            addPickup(PickupType.MOLOTOV, 1500, 1050);
            addPickup(PickupType.PILLS, 1450, 760);
        } else {
            addPickup(PickupType.PRIMARY_AMMO, 750, 520);
            addPickup(PickupType.PRIMARY_AMMO, 1150, 900);
            addPickup(PickupType.PISTOL_AMMO, 1300, 820);
            addPickup(PickupType.MEDKIT, 1500, 1100);
            addPickup(PickupType.BOMB, 1650, 980);
            addPickup(PickupType.MOLOTOV, 1750, 1200);
            addPickup(PickupType.ADRENALINE, 1450, 1350);
        }
    }

    private void addPickup(PickupType type, float x, float y) {
        pickups.add(new Pickup(type, x, y));
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
        drawSafeZones();
        drawWalls();
        drawPickups();
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
                resetInventory();
                loadLevel(levelIndex);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                game.setScreen(new MenuScreen(game));
            }

            return;
        }

        shootTimer -= delta;
        pushTimer -= delta;
        meleeTimer -= delta;

        handlePlayerInput(delta);
        keepPlayerInsideWorld();
        handleMouseControls(delta);

        updatePickups();

        zombieSpawner.update(delta, player, enemies, levelIndex, walls, exitZone);

        updateBullets(delta);
        updateEnemies(delta);

        checkExitZone();
        centerCameraOnPlayer();
    }
        private void handleMouseControls   (float delta) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                pushZombies();
            }

            if (selectedSlot == HandSlot.PRIMARY || selectedSlot == HandSlot.PISTOL) {
                handleGunShooting();
                healUseTimer = 0f;
                return;
            }

            if (selectedSlot == HandSlot.MELEE) {
                handleMeleeAttack();
                healUseTimer = 0f;
                return;
            }

            if (selectedSlot == HandSlot.THROWABLE) {
                handleThrowableUse();
                healUseTimer = 0f;
                return;
            }

            if (selectedSlot == HandSlot.MEDKIT) {
                handleMedkitUse(delta);
                return;
            }

            if (selectedSlot == HandSlot.BOOST) {
                handleBoostUse();
                healUseTimer = 0f;
                return;
            }

            healUseTimer = 0f;
        }

        private void handleGunShooting() {
            if (currentWeapon == null) {
                return;
            }

            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)
                && shootTimer <= 0f
                && getCurrentAmmo() >= currentWeapon.getAmmoPerShot()) {

                shootTimer = currentWeapon.getCooldown();
                spendCurrentAmmo(currentWeapon.getAmmoPerShot());

                Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(mouse);

                float dirX = mouse.x - player.getCenterX();
                float dirY = mouse.y - player.getCenterY();

                currentWeapon.shoot(
                    player.getCenterX(),
                    player.getCenterY(),
                    dirX,
                    dirY,
                    bullets
                );
            }
        }

        private void pushZombies() {
            if (pushTimer > 0f) {
                return;
            }

            pushTimer = PUSH_COOLDOWN;

            float px = player.getCenterX();
            float py = player.getCenterY();

            for (Enemy enemy : enemies) {
                Rectangle e = enemy.getBounds();

                float ex = e.x + e.width / 2f;
                float ey = e.y + e.height / 2f;

                float dx = ex - px;
                float dy = ey - py;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                if (distance > 0f && distance <= PUSH_RANGE) {
                    dx /= distance;
                    dy /= distance;

                    e.x += dx * PUSH_DISTANCE;
                    e.y += dy * PUSH_DISTANCE;
                }
            }
        }

        private void handleMeleeAttack() {
            if (!Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                return;
            }

            if (meleeTimer > 0f || meleeWeaponName.equals("None")) {
                return;
            }

            int damage = getMeleeDamage();
            float range = getMeleeRange();

            meleeTimer = getMeleeCooldown();

            float px = player.getCenterX();
            float py = player.getCenterY();

            for (Enemy enemy : enemies) {
                Rectangle e = enemy.getBounds();

                float ex = e.x + e.width / 2f;
                float ey = e.y + e.height / 2f;

                float dx = ex - px;
                float dy = ey - py;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                if (distance <= range) {
                    enemy.takeDamage(damage);
                }
            }
        }

        private int getMeleeDamage() {
            if (meleeWeaponName.equals("Katana")) {
                return 45;
            }

            if (meleeWeaponName.equals("Shovel")) {
                return 55;
            }

            if (meleeWeaponName.equals("Bat")) {
                return 30;
            }

            return 0;
        }

        private float getMeleeRange() {
            if (meleeWeaponName.equals("Katana")) {
                return 75f;
            }

            if (meleeWeaponName.equals("Shovel")) {
                return 65f;
            }

            if (meleeWeaponName.equals("Bat")) {
                return 60f;
            }

            return 0f;
        }

        private float getMeleeCooldown() {
            if (meleeWeaponName.equals("Katana")) {
                return 0.45f;
            }

            if (meleeWeaponName.equals("Shovel")) {
                return 0.8f;
            }

            if (meleeWeaponName.equals("Bat")) {
                return 0.55f;
            }

            return 0.5f;
        }

        private void handleThrowableUse() {
            if (!Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                return;
            }

            if (throwableName.equals("None") || throwableCount <= 0) {
                return;
            }

            throwableCount--;

            if (throwableCount <= 0) {
                throwableCount = 0;
                throwableName = "None";
                selectedSlot = HandSlot.NONE;
            }
        }

        private void handleMedkitUse(float delta) {
            if (!hasMedkit) {
                selectedSlot = HandSlot.NONE;
                healUseTimer = 0f;
                return;
            }

            if (!Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                healUseTimer = 0f;
                return;
            }

            healUseTimer += delta;

            if (healUseTimer >= MEDKIT_USE_TIME) {
                player.takeDamage(-50);
                hasMedkit = false;
                healUseTimer = 0f;
                selectedSlot = HandSlot.NONE;
            }
        }

        private void handleBoostUse() {
            if (!Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                return;
            }

            if (boostItemName.equals("Pills")) {
                player.takeDamage(-25);
                boostItemName = "None";
                selectedSlot = HandSlot.NONE;
            } else if (boostItemName.equals("Adrenaline")) {
                player.takeDamage(-15);
                boostItemName = "None";
                selectedSlot = HandSlot.NONE;
            }
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
    private int getCurrentAmmo() {
        if (currentWeapon == primaryWeapon) {
            return primaryAmmo;
        }

        if (currentWeapon == pistolWeapon) {
            return pistolAmmo;
        }

        return 0;
    }

    private void spendCurrentAmmo(int amount) {
        if (currentWeapon == primaryWeapon) {
            primaryAmmo -= amount;
            if (primaryAmmo < 0) {
                primaryAmmo = 0;
            }
        } else if (currentWeapon == pistolWeapon) {
            pistolAmmo -= amount;
            if (pistolAmmo < 0) {
                pistolAmmo = 0;
            }
        }
    }
    private void updatePickups() {
        Pickup nearestPickup = getNearestPickupInRange();

        if (nearestPickup != null && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            boolean pickedUp = applyPickup(nearestPickup.type);

            if (pickedUp) {
                pickups.removeValue(nearestPickup, true);
            }
        }
    }

    private boolean applyPickup(PickupType type) {
        switch (type) {
            case PRIMARY_AK47: {
                PickupType oldPrimary = getPrimaryPickupType();

                if (oldPrimary == PickupType.PRIMARY_AK47) {
                    return false;
                }

                primaryWeapon = new AK47();
                selectedSlot = HandSlot.PRIMARY;
                applySelectedSlot();

                if (oldPrimary != null) {
                    dropPickupNearPlayer(oldPrimary);
                }

                return true;
            }

            case PRIMARY_SNIPER: {
                PickupType oldPrimary = getPrimaryPickupType();

                if (oldPrimary == PickupType.PRIMARY_SNIPER) {
                    return false;
                }

                primaryWeapon = new Sniper();
                selectedSlot = HandSlot.PRIMARY;
                applySelectedSlot();

                if (oldPrimary != null) {
                    dropPickupNearPlayer(oldPrimary);
                }

                return true;
            }

            case PISTOL:
                if (pistolWeapon != null) {
                    return false;
                }

                pistolWeapon = new Pistol();
                selectedSlot = HandSlot.PISTOL;
                applySelectedSlot();
                return true;

            case PRIMARY_AMMO:
                primaryAmmo += 30;
                return true;

            case PISTOL_AMMO:
                pistolAmmo += 20;
                return true;

            case BAT: {
                PickupType oldMelee = getMeleePickupType();

                if (oldMelee == PickupType.BAT) {
                    return false;
                }

                meleeWeaponName = "Bat";
                selectedSlot = HandSlot.MELEE;
                applySelectedSlot();

                if (oldMelee != null) {
                    dropPickupNearPlayer(oldMelee);
                }

                return true;
            }

            case KATANA: {
                PickupType oldMelee = getMeleePickupType();

                if (oldMelee == PickupType.KATANA) {
                    return false;
                }

                meleeWeaponName = "Katana";
                selectedSlot = HandSlot.MELEE;
                applySelectedSlot();

                if (oldMelee != null) {
                    dropPickupNearPlayer(oldMelee);
                }

                return true;
            }

            case SHOVEL: {
                PickupType oldMelee = getMeleePickupType();

                if (oldMelee == PickupType.SHOVEL) {
                    return false;
                }

                meleeWeaponName = "Shovel";
                selectedSlot = HandSlot.MELEE;
                applySelectedSlot();

                if (oldMelee != null) {
                    dropPickupNearPlayer(oldMelee);
                }

                return true;
            }

            case MEDKIT:
                if (hasMedkit) {
                    return false;
                }

                hasMedkit = true;
                selectedSlot = HandSlot.MEDKIT;
                applySelectedSlot();
                return true;

            case PILLS: {
                PickupType oldBoost = getBoostPickupType();

                if (oldBoost == PickupType.PILLS) {
                    return false;
                }

                boostItemName = "Pills";
                selectedSlot = HandSlot.BOOST;
                applySelectedSlot();

                if (oldBoost != null) {
                    dropPickupNearPlayer(oldBoost);
                }

                return true;
            }

            case ADRENALINE: {
                PickupType oldBoost = getBoostPickupType();

                if (oldBoost == PickupType.ADRENALINE) {
                    return false;
                }

                boostItemName = "Adrenaline";
                selectedSlot = HandSlot.BOOST;
                applySelectedSlot();

                if (oldBoost != null) {
                    dropPickupNearPlayer(oldBoost);
                }

                return true;
            }

            case BOMB: {
                PickupType oldThrowable = getThrowablePickupType();

                if (oldThrowable == PickupType.BOMB) {
                    return false;
                }

                throwableName = "Bomb";
                throwableCount = 1;
                selectedSlot = HandSlot.THROWABLE;
                applySelectedSlot();

                if (oldThrowable != null) {
                    dropPickupNearPlayer(oldThrowable);
                }

                return true;
            }

            case MOLOTOV: {
                PickupType oldThrowable = getThrowablePickupType();

                if (oldThrowable == PickupType.MOLOTOV) {
                    return false;
                }

                throwableName = "Molotov";
                throwableCount = 1;
                selectedSlot = HandSlot.THROWABLE;
                applySelectedSlot();

                if (oldThrowable != null) {
                    dropPickupNearPlayer(oldThrowable);
                }

                return true;
            }

            default:
                return false;
        }
    }
    private void dropPickupNearPlayer(PickupType type) {
        float dropX = player.getBounds().x + 45f;
        float dropY = player.getBounds().y;

        addPickup(type, dropX, dropY);
    }

    private PickupType getPrimaryPickupType() {
        if (primaryWeapon == null) {
            return null;
        }

        if (primaryWeapon.getName().equals("AK47")) {
            return PickupType.PRIMARY_AK47;
        }

        if (primaryWeapon.getName().equals("Sniper")) {
            return PickupType.PRIMARY_SNIPER;
        }

        return null;
    }

    private PickupType getMeleePickupType() {
        if (meleeWeaponName.equals("Bat")) {
            return PickupType.BAT;
        }

        if (meleeWeaponName.equals("Katana")) {
            return PickupType.KATANA;
        }

        if (meleeWeaponName.equals("Shovel")) {
            return PickupType.SHOVEL;
        }

        return null;
    }

    private PickupType getBoostPickupType() {
        if (boostItemName.equals("Pills")) {
            return PickupType.PILLS;
        }

        if (boostItemName.equals("Adrenaline")) {
            return PickupType.ADRENALINE;
        }

        return null;
    }

    private PickupType getThrowablePickupType() {
        if (throwableName.equals("Bomb")) {
            return PickupType.BOMB;
        }

        if (throwableName.equals("Molotov")) {
            return PickupType.MOLOTOV;
        }

        return null;
    }
    private void selectNextSlot() {
        Array<HandSlot> slots = getAvailableSlots();

        if (slots.size == 0) {
            selectedSlot = HandSlot.NONE;
            currentWeapon = null;
            return;
        }

        int index = slots.indexOf(selectedSlot, true);

        if (index == -1 || index == slots.size - 1) {
            selectedSlot = slots.first();
        } else {
            selectedSlot = slots.get(index + 1);
        }

        applySelectedSlot();
    }

    private void selectPreviousSlot() {
        Array<HandSlot> slots = getAvailableSlots();

        if (slots.size == 0) {
            selectedSlot = HandSlot.NONE;
            currentWeapon = null;
            return;
        }

        int index = slots.indexOf(selectedSlot, true);

        if (index <= 0) {
            selectedSlot = slots.get(slots.size - 1);
        } else {
            selectedSlot = slots.get(index - 1);
        }

        applySelectedSlot();
    }

    private Array<HandSlot> getAvailableSlots() {
        Array<HandSlot> slots = new Array<>();

        if (primaryWeapon != null) {
            slots.add(HandSlot.PRIMARY);
        }

        if (pistolWeapon != null) {
            slots.add(HandSlot.PISTOL);
        }

        if (!meleeWeaponName.equals("None")) {
            slots.add(HandSlot.MELEE);
        }

        if (!throwableName.equals("None") && throwableCount > 0) {
            slots.add(HandSlot.THROWABLE);
        }

        if (hasMedkit) {
            slots.add(HandSlot.MEDKIT);
        }

        if (!boostItemName.equals("None")) {
            slots.add(HandSlot.BOOST);
        }

        return slots;
    }

    private void applySelectedSlot() {
        if (selectedSlot == HandSlot.PRIMARY) {
            currentWeapon = primaryWeapon;
        } else if (selectedSlot == HandSlot.PISTOL) {
            currentWeapon = pistolWeapon;
        } else {
            currentWeapon = null;
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
        boolean playerInSafeZone = isInAnySafeZone(player.getBounds());

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

            if (isInAnySafeZone(enemy.getBounds())) {
                enemy.getBounds().x = oldX;
                enemy.getBounds().y = oldY;
            }

            if (!playerInSafeZone
                && enemy.getBounds().overlaps(player.getBounds())
                && enemy.canAttack()) {

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

    private boolean isInAnySafeZone(Rectangle bounds) {
        return bounds.overlaps(startSafeZone) || bounds.overlaps(exitZone);
    }

    private void checkExitZone() {
        if (player.getBounds().overlaps(exitZone)
            && Gdx.input.isKeyJustPressed(Input.Keys.E)) {

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

    private void drawSafeZones() {
        batch.draw(
            Assets.safeZoneTexture,
            startSafeZone.x,
            startSafeZone.y,
            startSafeZone.width,
            startSafeZone.height
        );

        if (!victory) {
            batch.draw(
                Assets.safeZoneTexture,
                exitZone.x,
                exitZone.y,
                exitZone.width,
                exitZone.height
            );
        }
    }

    private void drawWalls() {
        for (Rectangle wall : walls) {
            batch.draw(Assets.wallTexture, wall.x, wall.y, wall.width, wall.height);
        }
    }

    private void drawPickups() {
        Pickup nearestPickup = getNearestPickupInRange();

        for (Pickup pickup : pickups) {
            drawPickupIcon(pickup);
        }

        if (nearestPickup != null) {
            font.setColor(Color.WHITE);
            font.draw(
                batch,
                getPickupLabel(nearestPickup.type),
                player.getBounds().x - 20,
                player.getBounds().y + player.getBounds().height + 35
            );
        }
    }
    private Pickup getNearestPickupInRange() {
        Pickup nearest = null;
        float nearestDistance = Float.MAX_VALUE;

        float playerCenterX = player.getCenterX();
        float playerCenterY = player.getCenterY();

        for (Pickup pickup : pickups) {
            float pickupCenterX = pickup.bounds.x + pickup.bounds.width / 2f;
            float pickupCenterY = pickup.bounds.y + pickup.bounds.height / 2f;

            float dx = playerCenterX - pickupCenterX;
            float dy = playerCenterY - pickupCenterY;
            float distanceSquared = dx * dx + dy * dy;

            if (distanceSquared < 2200f && distanceSquared < nearestDistance) {
                nearest = pickup;
                nearestDistance = distanceSquared;
            }
        }

        return nearest;
    }

        private void drawPickupIcon(Pickup pickup) {
        if (pickup.type == PickupType.MEDKIT) {
            batch.draw(
                Assets.medkitTexture,
                pickup.bounds.x,
                pickup.bounds.y,
                pickup.bounds.width,
                pickup.bounds.height
            );
            return;
        }

        if (pickup.type == PickupType.PRIMARY_AMMO
            || pickup.type == PickupType.PISTOL_AMMO) {

            batch.draw(
                Assets.ammoTexture,
                pickup.bounds.x,
                pickup.bounds.y,
                pickup.bounds.width,
                pickup.bounds.height
            );
            return;
        }

        batch.draw(
            Assets.bulletTexture,
            pickup.bounds.x,
            pickup.bounds.y,
            pickup.bounds.width,
            pickup.bounds.height
        );
    }
    private boolean isPlayerNearPickup(Pickup pickup) {
        float playerCenterX = player.getCenterX();
        float playerCenterY = player.getCenterY();

        float pickupCenterX = pickup.bounds.x + pickup.bounds.width / 2f;
        float pickupCenterY = pickup.bounds.y + pickup.bounds.height / 2f;

        float dx = playerCenterX - pickupCenterX;
        float dy = playerCenterY - pickupCenterY;

        float distanceSquared = dx * dx + dy * dy;

        return distanceSquared < 2500f;
    }

    private String getPickupLabel(PickupType type) {
        switch (type) {
            case PRIMARY_AK47:
                return "AK47";
            case PRIMARY_SNIPER:
                return "Sniper";
            case PISTOL:
                return "Pistol";
            case PRIMARY_AMMO:
                return "Rifle Ammo";
            case PISTOL_AMMO:
                return "Pistol Ammo";
            case BAT:
                return "Bat";
            case KATANA:
                return "Katana";
            case SHOVEL:
                return "Shovel";
            case MEDKIT:
                return "Medkit";
            case PILLS:
                return "Pills";
            case ADRENALINE:
                return "Adrenaline";
            case BOMB:
                return "Bomb";
            case MOLOTOV:
                return "Molotov";
            default:
                return "Item";
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
        font.draw(batch, "Selected: " + selectedSlot, left, top - 25f);
        font.draw(batch, "Current: " + getCurrentWeaponName(), left, top - 50f);
        font.draw(batch, "Primary: " + getPrimaryWeaponName() + " Ammo: " + primaryAmmo, left, top - 75f);
        font.draw(batch, "Pistol: " + getPistolWeaponName() + " Ammo: " + pistolAmmo, left, top - 100f);
        font.draw(batch, "Melee: " + meleeWeaponName, left, top - 125f);
        font.draw(batch, "Medkit: " + (hasMedkit ? "1" : "0"), left, top - 150f);
        font.draw(batch, "Boost: " + boostItemName, left, top - 175f);
        font.draw(batch, "Throwable: " + throwableName + " x" + throwableCount, left, top - 200f);
        font.draw(batch, "Kills: " + kills, left, top - 225f);
        font.draw(batch, "Map: " + (victory ? "Completed" : (levelIndex + 1) + "/4"), left, top - 250f);

        if (player.getBounds().overlaps(exitZone)) {
            font.draw(batch, "EXIT SAFE ZONE: Press E to continue", left, top - 275f);
        } else {
            font.draw(batch, "Objective: Reach the next safe zone", left, top - 275f);
        }

        font.draw(batch, "Wheel Switch | E Pickup | LMB Push | RMB Use", left, top - 300f);

        if (player.isDead()) {
            font.draw(
                batch,
                "GAME OVER - Press R to restart or ESC for menu",
                camera.position.x - 145f,
                camera.position.y
            );
        }

        if (victory) {
            font.draw(
                batch,
                "YOU ESCAPED! Press R to restart or ESC for menu",
                camera.position.x - 160f,
                camera.position.y
            );
        }
    }

    private String getCurrentWeaponName() {
        if (currentWeapon == null) {
            return "None";
        }

        return currentWeapon.getName();
    }

    private String getPrimaryWeaponName() {
        if (primaryWeapon == null) {
            return "None";
        }

        return primaryWeapon.getName();
    }

    private String getPistolWeaponName() {
        if (pistolWeapon == null) {
            return "None";
        }

        return pistolWeapon.getName();
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        batch.dispose();
        font.dispose();
    }

    private enum PickupType {
        PRIMARY_AK47,
        PRIMARY_SNIPER,
        PISTOL,
        PRIMARY_AMMO,
        PISTOL_AMMO,
        BAT,
        KATANA,
        SHOVEL,
        MEDKIT,
        PILLS,
        ADRENALINE,
        BOMB,
        MOLOTOV
    }

    private static class Pickup {
        private final PickupType type;
        private final Rectangle bounds;

        private Pickup(PickupType type, float x, float y) {
            this.type = type;
            this.bounds = new Rectangle(x, y, 28, 28);
        }
    }
    private enum HandSlot {
        NONE,
        PRIMARY,
        PISTOL,
        MELEE,
        THROWABLE,
        MEDKIT,
        BOOST
    }
}
