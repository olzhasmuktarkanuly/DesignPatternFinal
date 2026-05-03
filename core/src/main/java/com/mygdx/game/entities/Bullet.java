package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private final Rectangle bounds;
    private final Vector2 direction;
    private final float speed;
    private final int damage;
    private boolean active = true;

    public Bullet(float x, float y, float dirX, float dirY) {
        this(x, y, dirX, dirY, 25);
    }

    public Bullet(float x, float y, float dirX, float dirY, int damage) {
        this.bounds = new Rectangle(x, y, 8, 8);
        this.direction = new Vector2(dirX, dirY);

        if (this.direction.isZero(0.001f)) {
            this.direction.set(1f, 0f);
        } else {
            this.direction.nor();
        }

        this.speed = 450f;
        this.damage = damage;
    }

    public void update(float delta) {
        bounds.x += direction.x * speed * delta;
        bounds.y += direction.y * speed * delta;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }
}
