package com.mygdx.game.entities;

import com.badlogic.gdx.utils.Array;

public class Sniper implements Weapon {

    @Override
    public String getName() {
        return "Sniper";
    }

    @Override
    public float getCooldown() {
        return 1.1f;
    }

    @Override
    public int getAmmoPerShot() {
        return 1;
    }

    @Override
    public void shoot(float x, float y, float dirX, float dirY, Array<Bullet> bullets) {
        bullets.add(new Bullet(x, y, dirX, dirY, 80));
    }
}
