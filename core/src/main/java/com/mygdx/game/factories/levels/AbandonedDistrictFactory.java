package com.mygdx.game.factories.levels;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.enemies.CommonZombie;
import com.mygdx.game.world.LevelData;

public class AbandonedDistrictFactory implements LevelFactory {

    @Override
    public LevelData createLevel() {
        LevelData level = new LevelData(100, 100, new Rectangle(1700, 1700, 80, 80));

        level.walls.add(new Rectangle(300, 300, 400, 30));
        level.walls.add(new Rectangle(800, 500, 30, 300));
        level.walls.add(new Rectangle(1200, 200, 300, 30));

        level.enemies.add(new CommonZombie(700, 500));
        level.enemies.add(new CommonZombie(900, 700));
        level.enemies.add(new CommonZombie(1100, 600));

        return level;
    }
}
