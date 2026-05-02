package com.mygdx.game.world;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.world.Loot;
import com.mygdx.game.core.Assets;
import com.mygdx.game.entities.Player;

/**
 * Коробка с патронами — пополняет запас ammo игрока.
 */
public class AmmoBox extends Loot {

    private final int ammoAmount;

    public AmmoBox(float x, float y) {
        super(x, y, 24f);
        this.ammoAmount = 15;
    }

    @Override
    public void applyTo(Player player) {
        player.addAmmo(ammoAmount);
    }

    @Override
    public Texture getTexture() {
        return Assets.ammoTexture;
    }
}
