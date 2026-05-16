package com.mygdx.game.factories.levels;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.enemies.BruteZombie;
import com.mygdx.game.enemies.CommonZombie;
import com.mygdx.game.enemies.HunterZombie;
import com.mygdx.game.enemies.SpitterZombie;
import com.mygdx.game.world.LevelData;

public class InfectedZoneFactory implements LevelFactory {

    @Override
    public LevelData createLevel() {
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
}
