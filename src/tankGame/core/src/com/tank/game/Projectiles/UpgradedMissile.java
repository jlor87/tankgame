package com.tank.game.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class UpgradedMissile extends Projectiles{

    public UpgradedMissile(Rectangle sourceTank, float xCoordinate, float yCoordinate, float direction){
        this.sourceTank = sourceTank;
        this.direction = direction;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.speed = 8;
        this.picture = new Texture("missile2.jpg");
        this.sprite = new Sprite(picture);
        this.sprite.setRotation(direction);
        this.height = 30;
        this.width = 4;
    }
}
