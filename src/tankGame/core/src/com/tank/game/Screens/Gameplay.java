package com.tank.game.Screens;


import com.badlogic.gdx.Screen;
import com.tank.game.TankGame;

public class Gameplay implements Screen {
    private TankGame game;
    public Gameplay(TankGame game){
        this.game = game;
        game.reset();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
