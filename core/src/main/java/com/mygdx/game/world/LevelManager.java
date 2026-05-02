package com.mygdx.game.world;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.core.EnemyFactory;


public class LevelManager {

    private static final EnemyFactory commonFactory   = new EnemyFactory.CommonFactory();
    private static final EnemyFactory hunterFactory   = new EnemyFactory.HunterFactory();
    private static final EnemyFactory bruteFactory    = new EnemyFactory.BruteFactory();
    private static final EnemyFactory exploderFactory = new EnemyFactory.ExploderFactory();
    private static final EnemyFactory spitterFactory  = new EnemyFactory.SpitterFactory();

    public static LevelData createLevel(int levelIndex) {
        switch (levelIndex) {
            case 0:  return createLevel1();
            case 1:  return createLevel2();
            case 2:  return createLevel3();
            case 3:  return createLevel4();
            default: return createLevel1();
        }
    }

    private static LevelData createLevel1() {
        LevelData level = new LevelData(100, 100, new Rectangle(1700, 1700, 80, 80));

        level.walls.add(new Rectangle(300, 300, 400, 30));
        level.walls.add(new Rectangle(800, 500, 30, 300));
        level.walls.add(new Rectangle(1200, 200, 300, 30));

        level.enemies.add(commonFactory.createEnemy(700, 500));
        level.enemies.add(commonFactory.createEnemy(900, 700));
        level.enemies.add(commonFactory.createEnemy(1100, 600));


        level.loot.add(new AmmoBox(600, 800));
        level.loot.add(new AmmoBox(1300, 700));

        return level;
    }

    private static LevelData createLevel2() {
        LevelData level = new LevelData(150, 150, new Rectangle(1600, 1600, 80, 80));

        level.walls.add(new Rectangle(200, 600, 500, 30));
        level.walls.add(new Rectangle(900, 200, 30, 400));
        level.walls.add(new Rectangle(1200, 900, 350, 30));

        level.enemies.add(commonFactory.createEnemy(600, 500));
        level.enemies.add(commonFactory.createEnemy(1000, 800));
        level.enemies.add(hunterFactory.createEnemy(1300, 1000));


        level.loot.add(new AmmoBox(700, 900));
        level.loot.add(new AmmoBox(1400, 400));
        level.loot.add(new AmmoBox(500, 1200));

        return level;
    }

    private static LevelData createLevel3() {
        LevelData level = new LevelData(120, 120, new Rectangle(1750, 1500, 80, 80));

        level.walls.add(new Rectangle(400, 400, 30, 500));
        level.walls.add(new Rectangle(700, 900, 400, 30));
        level.walls.add(new Rectangle(1300, 300, 30, 500));

        level.enemies.add(commonFactory.createEnemy(800, 300));
        level.enemies.add(hunterFactory.createEnemy(1100, 500));
        level.enemies.add(bruteFactory.createEnemy(1500, 900));
        level.enemies.add(spitterFactory.createEnemy(900, 1100));


        level.loot.add(new AmmoBox(450, 200));
        level.loot.add(new AmmoBox(1600, 600));
        level.loot.add(new AmmoBox(900, 500));

        return level;
    }

    private static LevelData createLevel4() {
        LevelData level = new LevelData(100, 100, new Rectangle(1800, 1800, 80, 80));

        level.walls.add(new Rectangle(500, 200, 500, 30));
        level.walls.add(new Rectangle(700, 600, 30, 500));
        level.walls.add(new Rectangle(1200, 1200, 400, 30));

        level.enemies.add(commonFactory.createEnemy(800, 400));
        level.enemies.add(hunterFactory.createEnemy(1100, 800));
        level.enemies.add(bruteFactory.createEnemy(1400, 1200));
        level.enemies.add(exploderFactory.createEnemy(1600, 900));
        level.enemies.add(spitterFactory.createEnemy(1500, 700));


        level.loot.add(new AmmoBox(600, 300));
        level.loot.add(new AmmoBox(900, 1100));
        level.loot.add(new AmmoBox(1300, 600));
        level.loot.add(new AmmoBox(400, 1400));

        return level;
    }
}
