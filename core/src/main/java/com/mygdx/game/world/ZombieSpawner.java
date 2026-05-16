package com.mygdx.game.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.factories.EnemyFactory;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.Player;
import com.mygdx.game.screens.GameScreen;

public class ZombieSpawner {
    private final EnemyFactory enemyFactory = new EnemyFactory();
    private float spawnTimer = 0f;
    private float spawnInterval = 2.0f;
    private int maxEnemies = 20;

    public void update(float delta, Player player, Array<Enemy> enemies, int levelIndex,
                       Array<Rectangle> walls, Rectangle exitZone) {
        spawnTimer += delta;

        if (spawnTimer < spawnInterval) {
            return;
        }

        if (enemies.size >= maxEnemies) {
            return;
        }

        spawnTimer = 0f;

        Enemy enemy = createEnemy(levelIndex, player, walls, exitZone);
        enemies.add(enemy);
    }

    private Enemy createEnemy(int levelIndex, Player player, Array<Rectangle> walls, Rectangle exitZone) {
        Rectangle spawnRect = findSpawnPosition(player, walls, exitZone);
        return enemyFactory.createEnemyForLevel(levelIndex, spawnRect.x, spawnRect.y);
    }

    private Rectangle findSpawnPosition(Player player, Array<Rectangle> walls, Rectangle exitZone) {
        float x;
        float y;
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
            if (rect.overlaps(wall)) {
                return true;
            }
        }
        return false;
    }

    public void configureForLevel(int levelIndex) {
        switch (levelIndex) {
            case 0:
                spawnInterval = 2.2f;
                maxEnemies = 12;
                break;
            case 1:
                spawnInterval = 1.8f;
                maxEnemies = 16;
                break;
            case 2:
                spawnInterval = 1.5f;
                maxEnemies = 20;
                break;
            case 3:
                spawnInterval = 1.2f;
                maxEnemies = 24;
                break;
            default:
                spawnInterval = 2.0f;
                maxEnemies = 20;
                break;
        }
    }

    public void reset() {
        spawnTimer = 0f;
    }
}
