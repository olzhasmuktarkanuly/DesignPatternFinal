package com.mygdx.game.factories.levels;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.enemies.CommonZombie;
import com.mygdx.game.world.LevelData;

public class AbandonedDistrictFactory implements LevelFactory {

    @Override
    public LevelData createLevel() {
        LevelData level = new LevelData(
            120,
            120,
            new Rectangle(1700, 1650, 120, 120)
        );

        createOuterBrokenBorders(level);
        createAbandonedBuildings(level);
        createStreetBarricades(level);
        createTrainingEnemies(level);

        return level;
    }

    private void createOuterBrokenBorders(LevelData level) {
        level.walls.add(new Rectangle(0, 0, 2000, 30));
        level.walls.add(new Rectangle(0, 1970, 2000, 30));
        level.walls.add(new Rectangle(0, 0, 30, 2000));
        level.walls.add(new Rectangle(1970, 0, 30, 2000));
    }

    private void createAbandonedBuildings(LevelData level) {
        // left ruined building
        level.walls.add(new Rectangle(280, 260, 360, 40));
        level.walls.add(new Rectangle(280, 260, 40, 320));
        level.walls.add(new Rectangle(280, 540, 240, 40));

        // middle broken building
        level.walls.add(new Rectangle(760, 420, 420, 40));
        level.walls.add(new Rectangle(760, 420, 40, 360));
        level.walls.add(new Rectangle(1140, 420, 40, 260));
        level.walls.add(new Rectangle(880, 740, 300, 40));

        // right abandoned block
        level.walls.add(new Rectangle(1350, 250, 360, 40));
        level.walls.add(new Rectangle(1350, 250, 40, 360));
        level.walls.add(new Rectangle(1350, 570, 260, 40));

        // upper ruined building
        level.walls.add(new Rectangle(420, 1200, 500, 40));
        level.walls.add(new Rectangle(420, 1200, 40, 360));
        level.walls.add(new Rectangle(880, 1200, 40, 260));
    }

    private void createStreetBarricades(LevelData level) {
        // broken cars / barricades
        level.walls.add(new Rectangle(520, 850, 220, 35));
        level.walls.add(new Rectangle(980, 980, 260, 35));
        level.walls.add(new Rectangle(1320, 1180, 300, 35));

        // small debris
        level.walls.add(new Rectangle(350, 760, 120, 30));
        level.walls.add(new Rectangle(720, 1080, 130, 30));
        level.walls.add(new Rectangle(1220, 1450, 140, 30));
    }

    private void createTrainingEnemies(LevelData level) {
        level.enemies.add(new CommonZombie(650, 650));
        level.enemies.add(new CommonZombie(950, 900));
        level.enemies.add(new CommonZombie(1300, 1100));
        level.enemies.add(new CommonZombie(1550, 1450));
    }
}
