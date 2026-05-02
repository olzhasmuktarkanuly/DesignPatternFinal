package com.mygdx.game.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.entities.Player;


public abstract class Loot {

    protected final Rectangle bounds;
    private boolean pickedUp = false;

    public Loot(float x, float y, float size) {
        this.bounds = new Rectangle(x, y, size, size);
    }


    public abstract void applyTo(Player player);

    public abstract Texture getTexture();

    public Rectangle getBounds() { return bounds; }

    public boolean isPickedUp() { return pickedUp; }

    public void pickUp() { pickedUp = true; }
}
