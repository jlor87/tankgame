package com.tank.game.CanBePickedUp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.tank.game.Tanks.Tank;

public class ExtraLife extends CanBePickedUp {
    public ExtraLife(){
        this.image = new Texture("extraLife.png");
        this.sprite= new Sprite(image);
        this.xCoordinate = 1280;
        this.yCoordinate = 1154;
        this.boundingBox = new Rectangle(xCoordinate, yCoordinate, sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void performAction(Tank tank, Game game) {
        tank.gainLife();
    }
}
