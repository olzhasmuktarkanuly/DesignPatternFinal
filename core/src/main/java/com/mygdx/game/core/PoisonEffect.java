package com.mygdx.game.core;

import com.mygdx.game.core.*;
import com.mygdx.game.entities.Player;

/**
 * Паттерн Decorator — эффект яда.
 * Наносит урон каждые tickInterval секунд в течение duration секунд.
 * Применяется от SpitterZombie (лужа кислоты).
 */
public class PoisonEffect implements PlayerEffect {

    private final float duration;      // общая длительность яда
    private final float tickInterval;  // как часто наносить урон
    private final int damagePerTick;   // урон за каждый тик

    private float elapsed = 0f;
    private float tickTimer = 0f;
    private boolean finished = false;

    public PoisonEffect(float duration, float tickInterval, int damagePerTick) {
        this.duration = duration;
        this.tickInterval = tickInterval;
        this.damagePerTick = damagePerTick;
    }

    /** Стандартный яд: 5 сек, тик каждую секунду, 5 урона */
    public static PoisonEffect standard() {
        return new PoisonEffect(5f, 1f, 5);
    }

    @Override
    public void update(Player player, float delta) {
        elapsed += delta;
        tickTimer += delta;

        if (tickTimer >= tickInterval) {
            tickTimer -= tickInterval;
            player.takeDamage(damagePerTick);
        }

        if (elapsed >= duration) {
            finished = true;
        }
    }

    @Override
    public boolean isFinished() { return finished; }

    @Override
    public String getName() { return "POISON"; }
}
