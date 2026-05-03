package com.mygdx.game.items;

import com.badlogic.gdx.math.Rectangle;

public class Pickup {
    public final PickupType type;
    public final Rectangle bounds;

    public Pickup(PickupType type, float x, float y) {
        this.type = type;
        this.bounds = new Rectangle(x, y, 28, 28);
    }
}
