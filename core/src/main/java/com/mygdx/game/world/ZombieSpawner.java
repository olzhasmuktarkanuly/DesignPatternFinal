package com.mygdx.game.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.world.AcidPool;
import com.mygdx.game.entities.Player;
import com.mygdx.game.enemies.SpitterZombie;
import com.mygdx.game.core.EnemyFactory;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.screens.GameScreen;

/**
 * ZombieSpawner — теперь использует Factory Method (EnemyFactory)
 * вместо прямого new XxxZombie(...).
 */
public class ZombieSpawner {

    private float spawnTimer = 0f;
    private float spawnInterval = 2.0f;
    private int maxEnemies = 20;

    // Фабрики (Factory Method)
    private final EnemyFactory commonFactory   = new EnemyFactory.CommonFactory();
    private final EnemyFactory hunterFactory   = new EnemyFactory.HunterFactory();
    private final EnemyFactory bruteFactory    = new EnemyFactory.BruteFactory();
    private final EnemyFactory exploderFactory = new EnemyFactory.ExploderFactory();
    private final EnemyFactory spitterFactory  = new EnemyFactory.SpitterFactory();

    // Ссылка на общий список луж (для SpitterZombie)
    private Array<AcidPool> acidPools;

    public void setAcidPools(Array<AcidPool> acidPools) {
        this.acidPools = acidPools;
    }

    public void update(float delta, Player player, Array<Enemy> enemies, int levelIndex,
                       Array<Rectangle> walls, Rectangle exitZone) {
        spawnTimer += delta;
        if (spawnTimer < spawnInterval || enemies.size >= maxEnemies) return;
        spawnTimer = 0f;

        Enemy enemy = createEnemy(levelIndex, player, walls, exitZone);
        initEnemy(enemy);
        enemies.add(enemy);
    }

    private void initEnemy(Enemy enemy) {
        if (enemy instanceof SpitterZombie) {
            ((SpitterZombie) enemy).setAcidPools(acidPools);
        }
    }

    private Enemy createEnemy(int levelIndex, Player player, Array<Rectangle> walls, Rectangle exitZone) {
        Rectangle spawnRect = findSpawnPosition(player, walls, exitZone);
        float x = spawnRect.x;
        float y = spawnRect.y;

        switch (levelIndex) {
            case 0:
                return commonFactory.createEnemy(x, y);
            case 1:
                return MathUtils.randomBoolean(0.75f)
                    ? commonFactory.createEnemy(x, y)
                    : hunterFactory.createEnemy(x, y);
            case 2: {
                int roll = MathUtils.random(99);
                if (roll < 50) return commonFactory.createEnemy(x, y);
                if (roll < 75) return hunterFactory.createEnemy(x, y);
                if (roll < 90) return bruteFactory.createEnemy(x, y);
                return spitterFactory.createEnemy(x, y);
            }
            case 3:
            default: {
                int roll = MathUtils.random(99);
                if (roll < 35) return commonFactory.createEnemy(x, y);
                if (roll < 55) return hunterFactory.createEnemy(x, y);
                if (roll < 75) return bruteFactory.createEnemy(x, y);
                if (roll < 90) return spitterFactory.createEnemy(x, y);
                return exploderFactory.createEnemy(x, y);
            }
        }
    }

    private Rectangle findSpawnPosition(Player player, Array<Rectangle> walls, Rectangle exitZone) {
        float x, y;
        Rectangle testRect;
        do {
            x = MathUtils.random(0f, GameScreen.WORLD_WIDTH - 40f);
            y = MathUtils.random(0f, GameScreen.WORLD_HEIGHT - 40f);
            testRect = new Rectangle(x, y, 40, 40);
        } while (tooCloseToPlayer(player, testRect) || overlapsWall(testRect, walls) || testRect.overlaps(exitZone));
        return testRect;
    }

    private boolean tooCloseToPlayer(Player player, Rectangle rect) {
        float dx = player.getCenterX() - (rect.x + rect.width / 2f);
        float dy = player.getCenterY() - (rect.y + rect.height / 2f);
        return dx * dx + dy * dy < 350f * 350f;
    }

    private boolean overlapsWall(Rectangle rect, Array<Rectangle> walls) {
        for (Rectangle wall : walls) {
            if (rect.overlaps(wall)) return true;
        }
        return false;
    }

    public void configureForLevel(int levelIndex) {
        switch (levelIndex) {
            case 0: spawnInterval = 2.2f; maxEnemies = 12; break;
            case 1: spawnInterval = 1.8f; maxEnemies = 16; break;
            case 2: spawnInterval = 1.5f; maxEnemies = 20; break;
            case 3: spawnInterval = 1.2f; maxEnemies = 24; break;
            default: spawnInterval = 2.0f; maxEnemies = 20; break;
        }
    }

    public void reset() { spawnTimer = 0f; }
}
