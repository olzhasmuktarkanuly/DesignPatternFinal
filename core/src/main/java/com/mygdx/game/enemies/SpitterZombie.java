package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.world.AcidPool;
import com.mygdx.game.core.Assets;
import com.mygdx.game.entities.Enemy;
import com.mygdx.game.entities.Player;

/**
 * Плевальщица — держится на дистанции и периодически плюёт кислотой,
 * создавая AcidPool на позиции игрока.
 */
public class SpitterZombie extends Enemy {

    private final float preferredDistance = 180f;

    // Список луж передаётся снаружи (из GameScreen)
    private Array<AcidPool> acidPools;

    public SpitterZombie(float x, float y) {
        super(x, y, 30, 30, 70f, 75, 10);
        this.attackCooldown = 2.5f; // плюётся раз в 2.5 сек
    }

    /** GameScreen передаёт общий список луж при создании уровня */
    public void setAcidPools(Array<AcidPool> acidPools) {
        this.acidPools = acidPools;
    }

    @Override
    public void update(Player player, float delta) {
        attackTimer -= delta;

        Vector2 direction = new Vector2(
            player.getCenterX() - getCenterX(),
            player.getCenterY() - getCenterY()
        );
        float distance = direction.len();

        // Держится на дистанции preferredDistance
        if (distance > preferredDistance) {
            direction.nor();
            bounds.x += direction.x * speed * delta;
            bounds.y += direction.y * speed * delta;
        }

        // Атака — плюнуть кислотой (создать лужу под игроком)
        if (attackTimer <= 0f && distance <= preferredDistance + 50f) {
            attackTimer = attackCooldown;
            if (acidPools != null) {
                acidPools.add(new AcidPool(player.getCenterX(), player.getCenterY()));
            }
        }
    }

    @Override
    public Texture getTexture() {
        return Assets.spitterTexture;
    }
}
