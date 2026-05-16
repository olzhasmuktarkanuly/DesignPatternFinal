package com.mygdx.game.world;

import com.mygdx.game.factories.levels.AbandonedDistrictFactory;
import com.mygdx.game.factories.levels.CityLevelFactory;
import com.mygdx.game.factories.levels.EvacuationLevelFactory;
import com.mygdx.game.factories.levels.InfectedZoneFactory;
import com.mygdx.game.factories.levels.LevelFactory;

public class LevelManager {

    public static LevelData createLevel(int levelIndex) {
        LevelFactory factory = getFactory(levelIndex);
        return factory.createLevel();
    }

    private static LevelFactory getFactory(int levelIndex) {
        switch (levelIndex) {
            case 0:
                return new AbandonedDistrictFactory();

            case 1:
                return new CityLevelFactory();

            case 2:
                return new InfectedZoneFactory();

            case 3:
                return new EvacuationLevelFactory();

            default:
                return new AbandonedDistrictFactory();
        }
    }
}
