package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.core.Assets;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.Player;

public class ExploderZombie extends Enemy {

    private final float explosionRadius = 90f;
    private final int explosionDamage = 25;

    public ExploderZombie(float x, float y) {
        super(x, y, 30, 30, 85f, 45, 8);
    }

    @Override
    public void onDeath(Player player) {
        float dx = player.getCenterX() - getCenterX();
        float dy = player.getCenterY() - getCenterY();

        if (dx * dx + dy * dy <= explosionRadius * explosionRadius) {
            player.takeDamage(explosionDamage);
        }
    }

    @Override
    public Texture getTexture() {
        return Assets.exploderTexture;
    }
}
