package com.mygdx.game.core;
import com.mygdx.game.enemies.SpitterZombie;
import com.mygdx.game.enemies.BruteZombie;
import com.mygdx.game.enemies.CommonZombie;
import com.mygdx.game.enemies.ExploderZombie;
import com.mygdx.game.enemies.HunterZombie;
import com.mygdx.game.entities.Enemy;

/**
 * Паттерн Factory Method — абстрактная фабрика врагов.
 * Каждый конкретный класс знает как создать своего зомби.
 * GameScreen и ZombieSpawner работают только с этим интерфейсом.
 */
public abstract class EnemyFactory {

    public abstract Enemy createEnemy(float x, float y);

    // --- Конкретные фабрики (вложенные классы для удобства) ---

    public static class CommonFactory extends EnemyFactory {
        @Override
        public Enemy createEnemy(float x, float y) {
            return new CommonZombie(x, y);
        }
    }

    public static class HunterFactory extends EnemyFactory {
        @Override
        public Enemy createEnemy(float x, float y) {
            return new HunterZombie(x, y);
        }
    }

    public static class BruteFactory extends EnemyFactory {
        @Override
        public Enemy createEnemy(float x, float y) {
            return new BruteZombie(x, y);
        }
    }

    public static class ExploderFactory extends EnemyFactory {
        @Override
        public Enemy createEnemy(float x, float y) {
            return new ExploderZombie(x, y);
        }
    }

    public static class SpitterFactory extends EnemyFactory {
        @Override
        public Enemy createEnemy(float x, float y) {
            return new SpitterZombie(x, y);
        }
    }
}
