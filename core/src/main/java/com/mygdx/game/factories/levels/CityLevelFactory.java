package com.mygdx.game.factories.levels;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.enemies.CommonZombie;
import com.mygdx.game.enemies.HunterZombie;
import com.mygdx.game.world.LevelData;

public class CityLevelFactory implements LevelFactory {

    @Override
    public LevelData createLevel() {
        LevelData level = new LevelData(
            150,
            150,
            new Rectangle(1750, 1750, 150, 150)
        );

        createBorders(level);
        createCityBlocks(level);
        createAbandonedVehicles(level);
        createCityEnemies(level);

        return level;
    }

    private void createBorders(LevelData level) {
        level.walls.add(new Rectangle(0, 0, 2000, 30));
        level.walls.add(new Rectangle(0, 1970, 2000, 30));
        level.walls.add(new Rectangle(0, 0, 30, 2000));
        level.walls.add(new Rectangle(1970, 0, 30, 2000));
    }

    private void createCityBlocks(LevelData level) {
        level.walls.add(new Rectangle(300, 300, 400, 300));
        level.walls.add(new Rectangle(900, 200, 300, 500));
        level.walls.add(new Rectangle(1400, 300, 400, 200));

        level.walls.add(new Rectangle(200, 800, 200, 400));
        level.walls.add(new Rectangle(600, 900, 500, 300));
        level.walls.add(new Rectangle(1300, 800, 500, 400));

        level.walls.add(new Rectangle(300, 1400, 600, 300));
        level.walls.add(new Rectangle(1100, 1400, 400, 400));
    }

    private void createAbandonedVehicles(LevelData level) {
        level.walls.add(new Rectangle(450, 650, 120, 60));
        level.walls.add(new Rectangle(750, 400, 60, 120));
        level.walls.add(new Rectangle(1250, 500, 120, 60));
        level.walls.add(new Rectangle(850, 1250, 120, 60));
        level.walls.add(new Rectangle(1600, 600, 60, 120));
    }

    private void createCityEnemies(LevelData level) {
        level.enemies.add(new CommonZombie(500, 200));
        level.enemies.add(new CommonZombie(800, 750));
        level.enemies.add(new CommonZombie(1200, 400));
        level.enemies.add(new CommonZombie(1600, 1000));
        level.enemies.add(new CommonZombie(500, 1250));

        level.enemies.add(new HunterZombie(750, 800));
        level.enemies.add(new HunterZombie(1300, 600));
        level.enemies.add(new HunterZombie(1000, 1300));
        level.enemies.add(new HunterZombie(1600, 1500));
    }
}
