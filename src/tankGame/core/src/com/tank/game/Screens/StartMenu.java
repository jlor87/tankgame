package com.tank.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tank.game.GameStates.GameState;
import com.tank.game.TankGame;

public class StartMenu implements Screen {
    Stage stage;
    private final TankGame game;
    Texture screenImage;
    Image backgroundImage;
    private final TextButton startNormalButton;
    private final TextButton startBrutalModeButton;
    private final TextButton exitButton;
    private final BitmapFont font;

    Color normalFontColor = Color.WHITE; // Default color
    Color hoverFontColor = Color.RED;

    public StartMenu(final TankGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());

        screenImage = new Texture("startMenu.jpg");
        backgroundImage = new Image(screenImage);
        stage.addActor(backgroundImage);

        // Making buttons
        font = new BitmapFont();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = normalFontColor;
        textButtonStyle.overFontColor = hoverFontColor;

        //Add UI elements to the stage (buttons, labels, etc.)
        startNormalButton = new TextButton("Start Game: Normal Mode", textButtonStyle);
        startNormalButton.setWidth(200f);
        startNormalButton.setHeight(40f);
        startNormalButton.setPosition(20, 120);
        stage.addActor(startNormalButton);

        startBrutalModeButton = new TextButton("Start Game: Brutal Mode", textButtonStyle);
        startBrutalModeButton.setWidth(200f);
        startBrutalModeButton.setHeight(40f);
        startBrutalModeButton.setPosition(20, 70);
        stage.addActor(startBrutalModeButton);

        exitButton = new TextButton("Exit", textButtonStyle);
        exitButton.setWidth(200f);
        exitButton.setHeight(40f);
        exitButton.setPosition(20, 20);
        stage.addActor(exitButton);

        // Add a click listener to the start button
        startNormalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setBrutalMode(false);
                game.changeGameState(GameState.GAMEPLAY);
            }
        });

        startBrutalModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setBrutalMode(true);
                game.changeGameState(GameState.GAMEPLAY);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() { // Called when this screen becomes the current screen.
        resize(1440, 900);
        render(0);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the stage
        stage.act();
        stage.draw();
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
        screenImage.dispose();
        stage.dispose();
    }
}
