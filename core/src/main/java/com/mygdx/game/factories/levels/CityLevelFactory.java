package com.mygdx.game.factories.levels;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.enemies.CommonZombie;
import com.mygdx.game.enemies.HunterZombie;
import com.mygdx.game.world.LevelData;

public class CityLevelFactory implements LevelFactory {

    @Override
    public LevelData createLevel() {
        LevelData level = new LevelData(150, 150, new Rectangle(1600, 1600, 80, 80));

        level.walls.add(new Rectangle(200, 600, 500, 30));
        level.walls.add(new Rectangle(900, 200, 30, 400));
        level.walls.add(new Rectangle(1200, 900, 350, 30));

        level.enemies.add(new CommonZombie(600, 500));
        level.enemies.add(new CommonZombie(1000, 800));
        level.enemies.add(new HunterZombie(1300, 1000));

        return level;
    }
}
