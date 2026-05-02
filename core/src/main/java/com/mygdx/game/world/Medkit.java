package com.mygdx.game.world;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.core.Assets;
import com.mygdx.game.core.HealEffect;
import com.mygdx.game.entities.Player;


public class Medkit extends Loot {

    public Medkit(float x, float y) {
        super(x, y, 24f);
    }

    @Override
    public void applyTo(Player player) {
        player.applyEffect(HealEffect.fromMedkit());
    }

    @Override
    public Texture getTexture() {
        return Assets.medkitTexture;
    }
}
