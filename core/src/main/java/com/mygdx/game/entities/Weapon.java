package com.mygdx.game.entities;

import com.badlogic.gdx.utils.Array;

public interface Weapon {
    String getName();

    float getCooldown();

    int getAmmoPerShot();

    void shoot(float x, float y, float dirX, float dirY, Array<Bullet> bullets);
}
