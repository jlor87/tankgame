package com.tank.game.CanBePickedUp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.tank.game.Tanks.Tank;

public class DoubleFirepower extends CanBePickedUp {

    public DoubleFirepower(){
        this.image = new Texture("2Xfirepower.jpg");
        this.sprite= new Sprite(image);
        this.xCoordinate = 243;
        this.yCoordinate = 1416;
        this.boundingBox = new Rectangle(xCoordinate, yCoordinate, sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void performAction(Tank tank, Game game) {
        tank.upgradeMissile();
    }
}
