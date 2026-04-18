package com.mygdx.game.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.core.Assets;

public class SafeZone {
    private final Rectangle bounds;

    public SafeZone(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Texture getTexture() {
        return Assets.safeZoneTexture;
    }
}
