package com.mygdx.game.factories;

import com.mygdx.game.entities.AK47;
import com.mygdx.game.entities.Pistol;
import com.mygdx.game.entities.Sniper;
import com.mygdx.game.entities.Weapon;
import com.mygdx.game.items.PickupType;

public class WeaponFactory {

    public Weapon createWeapon(PickupType type) {
        switch (type) {
            case PRIMARY_AK47:
                return new AK47();

            case PRIMARY_SNIPER:
                return new Sniper();

            case PISTOL:
                return new Pistol();

            default:
                throw new IllegalArgumentException("Unsupported weapon type: " + type);
        }
    }
}
