package com.tank.game.CanBePickedUp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.tank.game.Tanks.Tank;

public class CanBePickedUp {
    protected float xCoordinate;

    protected float yCoordinate;
    protected Texture image;
    protected Sprite sprite;
    protected Rectangle boundingBox;

    public Rectangle getBoundingBox(){
        return boundingBox;
    }
    public Sprite getSprite(){
        return this.sprite;
    }
    public Texture getTexture(){
        return this.image;
    }
    public float getxCoordinate(){
        return this.xCoordinate;
    }
    public float getyCoordinate(){
        return this.yCoordinate;
    }
    public void performAction(Tank tank, Game game) {
    }
    public void setSprite(Sprite sprite){
        this.sprite = sprite;
    }
    public void setTexture(Texture image){
        this.image = image;
    }
    public void setxCoordinate(float xCoordinate){
        this.xCoordinate = xCoordinate;
    }
    public void setyCoordinate(float yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}
