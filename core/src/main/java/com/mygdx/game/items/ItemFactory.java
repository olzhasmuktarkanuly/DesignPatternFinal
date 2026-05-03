package com.mygdx.game.items;

public class ItemFactory {

    private ItemFactory() {
    }

    public static Pickup createPickup(PickupType type, float x, float y) {
        return new Pickup(type, x, y);
    }
}
