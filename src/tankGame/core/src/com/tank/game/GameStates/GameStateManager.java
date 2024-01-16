package com.tank.game.GameStates;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.tank.game.Screens.*;
import com.tank.game.TankGame;

public class GameStateManager {
    private Game game;
    private Screen currentScreen;
    private GameState currentState;

    public GameStateManager(Game game) {
        this.game = game;

        // Set the initial screen to the prologue when the game opens
        setState(GameState.PROLOGUE);
    }

    public Screen getCurrentScreen(){
        return currentScreen;
    }

    public GameState getCurrentState(){
        return currentState;
    }

    public void setState(GameState newState) {
        // Dispose the current screen to release resources
        if(currentScreen != null) {
            currentScreen.dispose();
        }

        // Change the state and set the new screen
        currentState = newState;
        switch (currentState) {
            case PROLOGUE:
                this.currentScreen = new Prologue((TankGame) game);
                break;
            case START_MENU:
                this.currentScreen = new StartMenu((TankGame) game);
                break;
            case GAMEPLAY:
                this.currentScreen = new Gameplay((TankGame) game);
                break;
            case GAME_OVER_LOSE:
                this.currentScreen = new GameOverLoss((TankGame) game);
                break;
            case GAME_OVER_WIN:
                this.currentScreen = new GameOverWin((TankGame) game);
                break;
        }

        // Set the new screen in the TankGame class
        game.setScreen(currentScreen);
    }
}