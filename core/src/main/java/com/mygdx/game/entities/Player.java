package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.core.PlayerEffect;

public class Player {
    private final Rectangle bounds;
    private final float speed;
    private int hp;
    private final int maxHp = 100;
    private int ammo;
    private final int maxAmmo = 60;

    private final Array<PlayerEffect> effects = new Array<>();

    public Player(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
        this.speed = 220f;
        this.hp = 100;
        this.ammo = 30;
    }

    public void update(float delta) {
        for (int i = effects.size - 1; i >= 0; i--) {
            PlayerEffect effect = effects.get(i);
            effect.update(this, delta);
            if (effect.isFinished()) {
                effects.removeIndex(i);
            }
        }
    }

    public void move(float dx, float dy, float delta) {
        bounds.x += dx * speed * delta;
        bounds.y += dy * speed * delta;
    }


    public void applyEffect(PlayerEffect effect) {
        effects.add(effect);
    }

    public boolean hasEffect(Class<? extends PlayerEffect> type) {
        for (PlayerEffect e : effects) {
            if (e.getClass() == type) return true;
        }
        return false;
    }


    public void takeDamage(int amount) {
        hp -= amount;
        if (hp < 0) hp = 0;
    }

    public void heal(int amount) {
        hp += amount;
        if (hp > maxHp) hp = maxHp;
    }

    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }


    public boolean hasAmmo() { return ammo > 0; }

    public void useAmmo(int amount) {
        ammo -= amount;
        if (ammo < 0) ammo = 0;
    }

    public void addAmmo(int amount) {
        ammo += amount;
        if (ammo > maxAmmo) ammo = maxAmmo;
    }

    public int getAmmo() { return ammo; }
    public int getMaxAmmo() { return maxAmmo; }


    public Rectangle getBounds() { return bounds; }

    public float getCenterX() { return bounds.x + bounds.width / 2f; }
    public float getCenterY() { return bounds.y + bounds.height / 2f; }

    public boolean isDead() { return hp <= 0; }
}
