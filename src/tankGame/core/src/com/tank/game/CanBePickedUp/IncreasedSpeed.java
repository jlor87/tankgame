package com.tank.game.CanBePickedUp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.tank.game.Tanks.Tank;

public class IncreasedSpeed extends CanBePickedUp {
    public IncreasedSpeed(){
        this.image = new Texture("extraSpeed.png");
        this.sprite= new Sprite(image);
        this.xCoordinate = 100;
        this.yCoordinate = 866;
        this.boundingBox = new Rectangle(xCoordinate, yCoordinate, sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void performAction(Tank tank, Game game) {
        tank.toggleSpeedBoost();
    }
}
