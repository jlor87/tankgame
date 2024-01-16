package com.tank.game.CanBePickedUp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.tank.game.TankGame;
import com.tank.game.Tanks.Tank;

public class Ingredient2 extends CanBePickedUp{
    public Ingredient2(float xCoordinate, float yCoordinate){
        this.image = new Texture("ingredient2.png");
        this.sprite= new Sprite(image);
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.boundingBox = new Rectangle(xCoordinate, yCoordinate, sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void performAction(Tank tank, Game game) {
        TankGame tankGame = (TankGame) game;
        tankGame.obtainedIngredient2();
    }
}