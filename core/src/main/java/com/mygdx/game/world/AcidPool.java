package com.mygdx.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.core.PoisonEffect;
import com.mygdx.game.entities.Player;

/**
 * Лужа кислоты от SpitterZombie.
 * Существует N секунд. Пока игрок в ней — применяет PoisonEffect (Decorator).
 */
public class AcidPool {

    private static Texture poolTexture;

    private final Rectangle bounds;
    private float lifetime;
    private boolean expired = false;

    // Чтобы не накладывать яд каждый кадр — кулдаун
    private float poisonCooldown = 1.5f;
    private float poisonTimer = 0f;

    public AcidPool(float centerX, float centerY) {
        float size = 60f;
        this.bounds = new Rectangle(centerX - size / 2f, centerY - size / 2f, size, size);
        this.lifetime = 6f;
    }

    public void update(Player player, float delta) {
        lifetime -= delta;
        if (lifetime <= 0f) {
            expired = true;
            return;
        }

        poisonTimer -= delta;

        if (bounds.overlaps(player.getBounds())) {
            if (poisonTimer <= 0f && !player.hasEffect(PoisonEffect.class)) {
                player.applyEffect(PoisonEffect.standard());
                poisonTimer = poisonCooldown;
            }
        }
    }

    public boolean isExpired() { return expired; }

    public Rectangle getBounds() { return bounds; }

    public Texture getTexture() {
        if (poolTexture == null) {
            Pixmap pixmap = new Pixmap(60, 60, Pixmap.Format.RGBA8888);
            pixmap.setColor(new Color(0.1f, 0.9f, 0.1f, 0.45f));
            pixmap.fillCircle(30, 30, 28);
            poolTexture = new Texture(pixmap);
            pixmap.dispose();
        }
        return poolTexture;
    }

    public static void disposeTexture() {
        if (poolTexture != null) {
            poolTexture.dispose();
            poolTexture = null;
        }
    }
}
