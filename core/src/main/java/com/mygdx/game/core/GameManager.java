package com.mygdx.game.core;

public class GameManager {
    private static GameManager instance;

    private int totalKills;
    private int currentLevel;
    private int playerHp;

    private GameManager() {
        totalKills = 0;
        currentLevel = 0;
        playerHp = 100;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public int getTotalKills() { return totalKills; }
    public void addKill() { totalKills++; }

    public int getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(int level) { currentLevel = level; }

    public int getPlayerHp() { return playerHp; }
    public void setPlayerHp(int hp) { playerHp = hp; }

    public void reset() {
        totalKills = 0;
        currentLevel = 0;
        playerHp = 100;
    }
}
