package com.mygdx.game.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entities.Player;

public class Zombie {
    private final Rectangle bounds;
    private final float speed;
    private int hp;
    private final int damage;
    private final boolean brute;

    private float attackCooldown = 1.0f;
    private float attackTimer = 0f;

    public Zombie(float x, float y, float width, float height, float speed, int hp, int damage, boolean brute) {
        this.bounds = new Rectangle(x, y, width, height);
        this.speed = speed;
        this.hp = hp;
        this.damage = damage;
        this.brute = brute;
    }

    public void update(Player player, float delta) {
        attackTimer -= delta;

        Vector2 direction = new Vector2(
            player.getBounds().x - bounds.x,
            player.getBounds().y - bounds.y
        );

        if (direction.len() > 1f) {
            direction.nor();
            bounds.x += direction.x * speed * delta;
            bounds.y += direction.y * speed * delta;
        }
    }

    public Rectangle getBounds() {
        return bounds;
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

    public boolean isBrute() {
        return brute;
    }
}
