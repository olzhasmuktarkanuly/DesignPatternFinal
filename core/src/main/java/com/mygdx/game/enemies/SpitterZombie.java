package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.core.Assets;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.Player;

public class SpitterZombie extends Enemy {

    private final float preferredDistance = 180f;

    public SpitterZombie(float x, float y) {
        super(x, y, 30, 30, 70f, 75, 10);
        this.attackCooldown = 1.0f;
    }

    @Override
    public void update(Player player, float delta) {
        attackTimer -= delta;

        Vector2 direction = new Vector2(
            player.getCenterX() - getCenterX(),
            player.getCenterY() - getCenterY()
        );

        float distance = direction.len();

        if (distance > preferredDistance) {
            if (distance > 1f) {
                direction.nor();
                bounds.x += direction.x * speed * delta;
                bounds.y += direction.y * speed * delta;
            }
        }
    }

    @Override
    public Texture getTexture() {
        return Assets.spitterTexture;
    }
}
