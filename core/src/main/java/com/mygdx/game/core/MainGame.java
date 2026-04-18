package com.mygdx.game.core;

import com.badlogic.gdx.Game;
import com.mygdx.game.screens.MenuScreen;

public class MainGame extends Game {
    @Override
    public void create() {
        Assets.load();
        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.dispose();
    }
}
