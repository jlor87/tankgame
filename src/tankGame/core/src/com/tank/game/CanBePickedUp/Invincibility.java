package com.tank.game.CanBePickedUp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.tank.game.TankGame;
import com.tank.game.Tanks.Tank;

public class Invincibility extends CanBePickedUp {
    public Invincibility(){
        this.image = new Texture("invincibility.jpg");
        this.sprite= new Sprite(image);
        this.xCoordinate = 1715;
        this.yCoordinate = 384;
        this.boundingBox = new Rectangle(xCoordinate, yCoordinate, sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void performAction(Tank tank, Game game) {
        tank.activateInvincibility();
    }
}
