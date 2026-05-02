package com.mygdx.game.core;
import com.mygdx.game.entities.Player;



public class HealEffect implements PlayerEffect {

    private final float duration;
    private final float tickInterval;
    private final int healPerTick;

    private float elapsed = 0f;
    private float tickTimer = 0f;
    private boolean finished = false;

    public HealEffect(float duration, float tickInterval, int healPerTick) {
        this.duration = duration;
        this.tickInterval = tickInterval;
        this.healPerTick = healPerTick;
    }

    public static HealEffect fromMedkit() {
        return new HealEffect(3f, 0.5f, 10);
    }

    @Override
    public void update(Player player, float delta) {
        elapsed += delta;
        tickTimer += delta;

        if (tickTimer >= tickInterval) {
            tickTimer -= tickInterval;
            player.heal(healPerTick);
        }

        if (elapsed >= duration) {
            finished = true;
        }
    }

    @Override
    public boolean isFinished() { return finished; }

    @Override
    public String getName() { return "HEALING"; }
}
