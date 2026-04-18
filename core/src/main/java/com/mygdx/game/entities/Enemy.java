package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy {
    protected final Rectangle bounds;
    protected float speed;
    protected int hp;
    protected int damage;

    protected float attackCooldown = 1.0f;
    protected float attackTimer = 0f;

    public Enemy(float x, float y, float width, float height, float speed, int hp, int damage) {
        this.bounds = new Rectangle(x, y, width, height);
        this.speed = speed;
        this.hp = hp;
        this.damage = damage;
    }

    public void update(Player player, float delta) {
        attackTimer -= delta;
        chasePlayer(player, delta);
    }

    protected void chasePlayer(Player player, float delta) {
        Vector2 direction = new Vector2(
            player.getCenterX() - getCenterX(),
            player.getCenterY() - getCenterY()
        );

        if (direction.len() > 1f) {
            direction.nor();
            bounds.x += direction.x * speed * delta;
            bounds.y += direction.y * speed * delta;
        }
    }

    public void onDeath(Player player) {
    }

    public abstract Texture getTexture();

    public Rectangle getBounds() {
        return bounds;
    }

    public float getCenterX() {
        return bounds.x + bounds.width / 2f;
    }

    public float getCenterY() {
        return bounds.y + bounds.height / 2f;
    }

    public int getDamage() {
        return damage;
    }

    public boolean canAttack() {
        return attackTimer <= 0f;
    }

    public void resetAttackTimer() {
        attackTimer = attackCooldown;
    }

    public void takeDamage(int amount) {
        hp -= amount;
    }

    public boolean isDead() {
        return hp <= 0;
    }
}
