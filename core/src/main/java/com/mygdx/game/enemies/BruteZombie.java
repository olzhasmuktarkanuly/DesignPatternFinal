package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.core.Assets;
import com.mygdx.game.entities.Enemy;

public class BruteZombie extends Enemy {

    public BruteZombie(float x, float y) {
        super(x, y, 42, 42, 60f, 160, 22);
        this.attackCooldown = 1.2f;
    }

    @Override
    public Texture getTexture() {
        return Assets.bruteTexture;
    }
}
