package com.tank.game.Explosion;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {
    private float xCoordinate;
    private float yCoordinate;
    private int timer;

    private float alphaValue;

    private TextureRegion currentFrame;

    public Explosion(float xCoordinate, float yCoordinate){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.timer = 0;
        this.alphaValue = 1.0f;
    }

    public float getAlphaValue() {
        return alphaValue;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public float getxCoordinate() {
        return xCoordinate;
    }

    public float getyCoordinate() {
        return yCoordinate;
    }

    public void incrementTimer(){
        this.timer = this.timer + 1;
    }

    public int getTimerValue(){
        return this.timer;
    }

    public void setAlphaValue(float alphaValue) {
        this.alphaValue = alphaValue;
    }

    public void setCurrentFrame(TextureRegion currentFrame) {
        this.currentFrame = currentFrame;
    }

}
