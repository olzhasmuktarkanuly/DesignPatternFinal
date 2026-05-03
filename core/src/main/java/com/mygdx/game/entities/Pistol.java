package com.mygdx.game.entities;
import com.badlogic.gdx.utils.Array;
public class Pistol implements Weapon {

    @Override
    public String getName() {
        return "Pistol";
    }

    @Override
    public float getCooldown() {
        return 0.35f;
    }

    @Override
    public int getAmmoPerShot() {
        return 1;
    }

    @Override
    public void shoot(float x, float y, float dirX, float dirY, Array<Bullet> bullets) {
        bullets.add(new Bullet(x, y, dirX, dirY, 20));
    }
}
