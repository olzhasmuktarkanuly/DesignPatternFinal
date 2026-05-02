package com.mygdx.game.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Enemy;

public class LevelData {
    public final float playerStartX;
    public final float playerStartY;
    public final Array<Rectangle> walls;
    public final Array<Enemy> enemies;
    public final Array<AmmoBox> loot;       // аптечки и патроны
    public final Rectangle exitZone;

    public LevelData(float playerStartX, float playerStartY, Rectangle exitZone) {
        this.playerStartX = playerStartX;
        this.playerStartY = playerStartY;
        this.exitZone = exitZone;
        this.walls   = new Array<>();
        this.enemies = new Array<>();
        this.loot    = new Array<AmmoBox>();
    }
}
