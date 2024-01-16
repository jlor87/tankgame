package com.tank.game.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Projectiles {
    protected float direction;
    protected int speed;
    protected float xCoordinate;
    protected float yCoordinate;
    protected Texture picture;
    protected Sprite sprite;
    protected float width;
    protected float height;
    protected Rectangle sourceTank;

    public float getDirection(){
        return direction;
    }

    public float getHeight() {
        return height;
    }
    public Rectangle getSourceTank(){
        return sourceTank;
    }

    public Sprite getSprite(){
        return this.sprite;
    }

    public float getxCoordinate(){
        return this.xCoordinate;
    }

    public float getyCoordinate(){
        return this.yCoordinate;
    }

    public float getWidth() {
        return width;
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
    }
    public void updatexCoordinate(){
        if(direction < 360 && direction > 180){
            this.xCoordinate = (float) (xCoordinate + speed * Math.cos(Math.toRadians(direction + 90)));
        }
        else if(direction <= 180 && direction > 90) {
            this.xCoordinate = (float) (xCoordinate + speed * Math.cos(Math.toRadians(direction + 90)));
        }
        else if(direction <= 90 && direction >= 0) {
            this.xCoordinate = (float) (xCoordinate + speed * Math.cos(Math.toRadians(direction + 90)));
        }
    }
    public void updateyCoordinate(){
        if(direction > 270 || direction < 90){
            this.yCoordinate = (float) (yCoordinate + speed * Math.sin(Math.toRadians(direction + 90)));
        } else if (direction <= 270 && direction > 180){
            this.yCoordinate = (float) (yCoordinate + speed * Math.sin(Math.toRadians(direction + 90)));
        }
        else if(direction <= 180){
            this.yCoordinate = (float) (yCoordinate + speed * Math.sin(Math.toRadians(direction + 90)));
        }
    }
}
