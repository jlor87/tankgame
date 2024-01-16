package com.tank.game.Walls;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Wall {
    protected float xCoordinate;
    protected float yCoordinate;
    protected Texture texture;
    protected Sprite sprite;
    protected Rectangle wallBoundingBox;
    protected int HP;

    public int getHP() {
        return this.HP;
    }

    public Sprite getSprite() {
        return this.sprite;
    }
    public Rectangle getWallBoundingBox() {
        return this.wallBoundingBox;
    }

    public float getyCoordinate() {
        return this.yCoordinate;
    }

    public float getxCoordinate() {
        return this.xCoordinate;
    }
    public void reduceHP(int code){
        if(code == 1){ // Wall is hit by a normal missile
            this.HP = this.HP - 25;
        }
        if(code == 2){ // Wall is hit by an upgraded missile
            this.HP = this.HP - 50;
        }
    }

}
