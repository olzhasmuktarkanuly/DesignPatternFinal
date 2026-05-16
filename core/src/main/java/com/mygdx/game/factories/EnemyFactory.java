package com.mygdx.game.factories;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.enemies.BruteZombie;
import com.mygdx.game.enemies.CommonZombie;
import com.mygdx.game.enemies.ExploderZombie;
import com.mygdx.game.enemies.HunterZombie;
import com.mygdx.game.enemies.SpitterZombie;
import com.mygdx.game.entities.Enemy;

public class EnemyFactory {

    public Enemy createEnemyForLevel(int levelIndex, float x, float y) {
        switch (levelIndex) {
            case 0:
                return new CommonZombie(x, y);

            case 1:
                if (MathUtils.randomBoolean(0.75f)) {
                    return new CommonZombie(x, y);
                }

                return new HunterZombie(x, y);

            case 2:
                return createLevelThreeEnemy(x, y);

            case 3:
            default:
                return createFinalLevelEnemy(x, y);
        }
    }

    private Enemy createLevelThreeEnemy(float x, float y) {
        int roll = MathUtils.random(99);

        if (roll < 50) {
            return new CommonZombie(x, y);
        }

        if (roll < 75) {
            return new HunterZombie(x, y);
        }

        if (roll < 90) {
            return new BruteZombie(x, y);
        }

        return new SpitterZombie(x, y);
    }

    private Enemy createFinalLevelEnemy(float x, float y) {
        int roll = MathUtils.random(99);

        if (roll < 35) {
            return new CommonZombie(x, y);
        }

        if (roll < 55) {
            return new HunterZombie(x, y);
        }

        if (roll < 75) {
            return new BruteZombie(x, y);
        }

        if (roll < 90) {
            return new SpitterZombie(x, y);
        }

        return new ExploderZombie(x, y);
    }
}
