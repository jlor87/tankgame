package com.tank.game.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Missile extends Projectiles{

    public Missile(Rectangle sourceTank, float xCoordinate, float yCoordinate, float direction){
        this.sourceTank = sourceTank;
        this.direction = direction;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.speed = 6;
        this.picture = new Texture("missile.jpg");
        this.sprite = new Sprite(picture);
        this.sprite.setRotation(direction - 90);
        this.height = 30;
        this.width = 4;
    }
}
