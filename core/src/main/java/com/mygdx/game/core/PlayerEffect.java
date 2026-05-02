package com.mygdx.game.core;

import com.mygdx.game.entities.Player;

/**
 * Паттерн Decorator — базовый интерфейс для эффектов на игроке.
 * Каждый эффект знает как обновить себя и когда завершиться.
 */
public interface PlayerEffect {
    void update(Player player, float delta);
    boolean isFinished();
    String getName(); // для отображения в HUD
}
