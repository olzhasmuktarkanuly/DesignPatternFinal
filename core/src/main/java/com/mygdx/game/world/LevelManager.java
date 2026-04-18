package com.mygdx.game.world;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.enemies.*;

public class LevelManager {

    public static LevelData createLevel(int levelIndex) {
        switch (levelIndex) {
            case 0:
                return createLevel1();
            case 1:
                return createLevel2();
            case 2:
                return createLevel3();
            case 3:
                return createLevel4();
            default:
                return createLevel1();
        }
    }

    private static LevelData createLevel1() {
        LevelData level = new LevelData(100, 100, new Rectangle(1700, 1700, 80, 80));

        level.walls.add(new Rectangle(300, 300, 400, 30));
        level.walls.add(new Rectangle(800, 500, 30, 300));
        level.walls.add(new Rectangle(1200, 200, 300, 30));

        level.enemies.add(new CommonZombie(700, 500));
        level.enemies.add(new CommonZombie(900, 700));
        level.enemies.add(new CommonZombie(1100, 600));

        return level;
    }

    private static LevelData createLevel2() {
        LevelData level = new LevelData(150, 150, new Rectangle(1600, 1600, 80, 80));

        level.walls.add(new Rectangle(200, 600, 500, 30));
        level.walls.add(new Rectangle(900, 200, 30, 400));
        level.walls.add(new Rectangle(1200, 900, 350, 30));

        level.enemies.add(new CommonZombie(600, 500));
        level.enemies.add(new CommonZombie(1000, 800));
        level.enemies.add(new HunterZombie(1300, 1000));

        return level;
    }

    private static LevelData createLevel3() {
        LevelData level = new LevelData(120, 120, new Rectangle(1750, 1500, 80, 80));

        level.walls.add(new Rectangle(400, 400, 30, 500));
        level.walls.add(new Rectangle(700, 900, 400, 30));
        level.walls.add(new Rectangle(1300, 300, 30, 500));

        level.enemies.add(new CommonZombie(800, 300));
        level.enemies.add(new HunterZombie(1100, 500));
        level.enemies.add(new BruteZombie(1500, 900));
        level.enemies.add(new SpitterZombie(900, 1100));

        return level;
    }

    private static LevelData createLevel4() {
        LevelData level = new LevelData(100, 100, new Rectangle(1800, 1800, 80, 80));

        level.walls.add(new Rectangle(500, 200, 500, 30));
        level.walls.add(new Rectangle(700, 600, 30, 500));
        level.walls.add(new Rectangle(1200, 1200, 400, 30));

        level.enemies.add(new CommonZombie(800, 400));
        level.enemies.add(new HunterZombie(1100, 800));
        level.enemies.add(new BruteZombie(1400, 1200));
        level.enemies.add(new ExploderZombie(1600, 900));
        level.enemies.add(new SpitterZombie(1500, 700));

        return level;
    }
}
