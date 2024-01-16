package com.tank.game.Walls;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class VerticalWall extends Wall{
    public VerticalWall(float xCoordinate, float yCoordinate){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.texture = new Texture("verticalWall.png");
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(xCoordinate, yCoordinate);
        this.wallBoundingBox = new Rectangle(xCoordinate, yCoordinate, sprite.getWidth(), sprite.getHeight());
        this.HP = 30;
    }
}
