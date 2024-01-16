package com.tank.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tank.game.TankGame;

public class GameOverLoss implements Screen {
    Stage stage;
    SpriteBatch batch;
    private final TankGame game;
    private final Texture screenImage;

    public GameOverLoss(TankGame game) {
        screenImage = new Texture("gameOverLoss.png");
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
    }

    @Override
    public void show() { // Called when this screen becomes the current screen.
        render(0);
    }

    @Override
    public void render(float delta) {
        resize(1440, 900);
        // Draw the image
        batch.begin();
        batch.draw(screenImage, 0, 0);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() { // Called when the game is paused.
    }

    @Override
    public void resume() { // Called when the game is resumed from a paused state.
    }

    @Override
    public void hide() { // Called when this screen is no longer the current screen.
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
