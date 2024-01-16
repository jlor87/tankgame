package com.tank.game.CanBePickedUp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.tank.game.Tanks.Tank;

public class HPBoost extends CanBePickedUp {
    public HPBoost(){
        this.image = new Texture("HPBoost.jpg");
        this.sprite= new Sprite(image);
        this.xCoordinate = 1600;
        this.yCoordinate = 502;
        this.boundingBox = new Rectangle(xCoordinate, yCoordinate, sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void performAction(Tank tank, Game game) {
        tank.setHP(tank.getHP() + 75);
    }
}
