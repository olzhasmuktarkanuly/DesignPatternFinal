package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.core.Assets;
import com.mygdx.game.entities.Enemy;

public class CommonZombie extends Enemy {

    public CommonZombie(float x, float y) {
        super(x, y, 28, 28, 95f, 60, 10);
    }

    @Override
    public Texture getTexture() {
        return Assets.zombieTexture;
    }
}
