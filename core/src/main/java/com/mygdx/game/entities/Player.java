package com.mygdx.game.entities;

import com.badlogic.gdx.math.Rectangle;

public class Player {
    private final Rectangle bounds;
    private final float speed;
    private int hp;

    public Player(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
        this.speed = 220f;
        this.hp = 100;
    }

    public void move(float dx, float dy, float delta) {
        bounds.x += dx * speed * delta;
        bounds.y += dy * speed * delta;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getCenterX() {
        return bounds.x + bounds.width / 2f;
    }

    public float getCenterY() {
        return bounds.y + bounds.height / 2f;
    }

    public int getHp() {
        return hp;
    }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp < 0) {
            hp = 0;
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }
}
