package com.mygdx.game.core;

import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.entities.Player;
import com.mygdx.game.enemies.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;

public class GameFacade {

    private final GameManager gameManager;

    public GameFacade() {
        this.gameManager = GameManager.getInstance();
    }

    public void startLevel(int levelIndex) {
        gameManager.setCurrentLevel(levelIndex);
    }

    public int getCurrentLevel() {
        return gameManager.getCurrentLevel();
    }


    public void updatePlayerHp(int hp) {
        gameManager.setPlayerHp(hp);
    }

    public int getPlayerHp() {
        return gameManager.getPlayerHp();
    }


    public void registerKill() {
        gameManager.addKill();
    }

    public int getTotalKills() {
        return gameManager.getTotalKills();
    }

    public void resetGame() {
        gameManager.reset();
    }
}
