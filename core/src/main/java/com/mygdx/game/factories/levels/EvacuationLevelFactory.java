package com.mygdx.game.factories.levels;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.enemies.BruteZombie;
import com.mygdx.game.enemies.CommonZombie;
import com.mygdx.game.enemies.ExploderZombie;
import com.mygdx.game.enemies.HunterZombie;
import com.mygdx.game.enemies.SpitterZombie;
import com.mygdx.game.world.LevelData;

public class EvacuationLevelFactory implements LevelFactory {

    @Override
    public LevelData createLevel() {
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
