package com.tank.game.CanBePickedUp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.tank.game.TankGame;
import com.tank.game.Tanks.Tank;

public class Ingredient3 extends CanBePickedUp{
    public Ingredient3(){
        this.image = new Texture("ingredient3.png");
        this.sprite= new Sprite(image);
        this.xCoordinate = 2000;
        this.yCoordinate = 50;
        this.boundingBox = new Rectangle(xCoordinate, yCoordinate, sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void performAction(Tank tank, Game game) {
        TankGame tankGame = (TankGame) game;
        tankGame.obtainedIngredient3();
    }
}
