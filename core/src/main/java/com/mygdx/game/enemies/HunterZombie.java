package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.core.Assets;
import com.mygdx.game.entities.Enemy;

public class HunterZombie extends Enemy {

    public HunterZombie(float x, float y) {
        super(x, y, 24, 24, 145f, 40, 12);
        this.attackCooldown = 0.7f;
    }

    @Override
    public Texture getTexture() {
        return Assets.hunterTexture;
    }
}
