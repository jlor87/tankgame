package com.tank.game.CanBePickedUp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.tank.game.TankGame;
import com.tank.game.Tanks.Tank;
import org.w3c.dom.Text;

public class Ingredient1 extends CanBePickedUp{
    public Ingredient1(){
        this.image = new Texture("ingredient1.png");
        this.sprite= new Sprite(image);
        this.xCoordinate = 1328;
        this.yCoordinate = 1431;
        this.boundingBox = new Rectangle(xCoordinate, yCoordinate, sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void performAction(Tank tank, Game game) {
        TankGame tankGame = (TankGame) game;
        ((TankGame) game).activateEvent1(tank);
        tankGame.obtainedIngredient1();
    }
}
