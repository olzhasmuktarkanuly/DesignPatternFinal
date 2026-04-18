package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.core.Assets;
import com.mygdx.game.core.MainGame;

public class MenuScreen extends ScreenAdapter {
    private final MainGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;

    public MenuScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(Assets.menuBackground, 0, 0, 800, 600);
        font.setColor(Color.WHITE);
        font.draw(batch, "THE LAST STAND", 300, 420);
        font.draw(batch, "Press ENTER to Start", 295, 360);
        font.draw(batch, "Survive 4 maps and reach the final safe zone", 210, 320);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
