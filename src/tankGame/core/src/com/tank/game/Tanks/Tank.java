package com.tank.game.Tanks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Tank {
    private int HP;
    private int lives;
    private float speed;
    private float xCoordinate;
    private float yCoordinate;
    private float direction;
    private final Sprite tankSprite;
    private final Texture texture;
    private final Rectangle tankBoundingBox;
    private boolean hasSpeedBoost;
    private boolean invincibilityOn;
    private boolean tempPowerUpActive;
    private boolean upgradedMissile;
    private int powerUpCounter;
    private float rotationSpeed;

    public Tank(float xCoordinate, float yCoordinate, Texture texture){
        this.HP = 100;
        this.speed = 2.5f; // Original: 2.5f
        this.rotationSpeed = 2; // Original: 2
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.direction = 0;
        this.texture = texture;
        this.tankSprite = new Sprite(texture);
        this.tankSprite.setPosition(xCoordinate, yCoordinate);
        this.lives = 3;
        this.upgradedMissile = false;
        this.tempPowerUpActive = false;
        this.invincibilityOn = false;
        powerUpCounter = 1500;
        tankBoundingBox = new Rectangle(xCoordinate, yCoordinate, tankSprite.getWidth(), tankSprite.getHeight());
    }
    public void activateInvincibility(){
        this.invincibilityOn = true;
        setTempPowerUpActive();
    }
    public void determineSpeed(){
        if(hasSpeedBoost){
            setSpeed(3.75f);
            setRotationSpeed(3f);
        }
        else{
            setSpeed(2.5f); // original 2.5f
            setRotationSpeed(2f);
        }
    }
    public int getLives(){
        return this.lives;
    }
    public void gainLife(){
        this.lives = this.lives + 1;
    }
    public float getDirection(){
        return this.direction;
    }
    public int getHP() {
        return HP;
    }
    public int getPowerUpCounter(){
        return this.powerUpCounter;
    }

    public Texture getTexture(){
        return this.texture;
    }

    public float getxCoordinate(){
        return this.xCoordinate;
    }

    public float getyCoordinate(){
        return this.yCoordinate;
    }
    public void deleteLife() {
        this.lives = this.lives - 1;
    }
    public float getRotationSpeed(){
        return this.rotationSpeed;
    }
    public float getSpeed(){
        return this.speed;
    }
    public Rectangle getTankBoundingBox(){
        return tankBoundingBox;
    }
    public Sprite getTankSprite(){
        return this.tankSprite;
    }
    public boolean isMissileUpgraded(){
        return upgradedMissile;
    }
    public boolean isTempPowerUpActive(){
        return tempPowerUpActive;
    }
    public void reduceHP(int code) {
        if (!invincibilityOn) {
            if (code == 1) { // Hit by a zombie
                this.HP = this.HP - 5; // original: 5
            } else if (code == 2) { // Hit by other player's missile
                this.HP = this.HP - 15; // original: 15
            } else if (code == 3) { // Hit by other player's upgraded missile
                this.HP = this.HP - 30;
            }
        }
    }
    public void reduceTimer(){
        this.powerUpCounter--;
    }
    public void setLives(int amount){
        this.lives = amount;
    }

    public void setxCoordinate(float newXCoordinate){
        this.xCoordinate = newXCoordinate;
    }

    public void setyCoordinate(float newYCoordinate){
        this.yCoordinate = newYCoordinate;
    }
    public void setTempPowerUpActive(){
        this.tempPowerUpActive = !tempPowerUpActive;
        if(!tempPowerUpActive){
            if(invincibilityOn){
                this.invincibilityOn = false;
            }
            this.powerUpCounter = 1500; // Reset the duration for invincibility
        }
    }
    public void setRotationSpeed(float rotationSpeed){
        this.rotationSpeed = rotationSpeed;
    }
    public void setSpeed(float newSpeed){
        this.speed = newSpeed;
    }
    public void setDirection(float rotationAngle){
        this.direction = rotationAngle;
    }
    public void setHP(int newHP){
        this.HP = newHP;
    }
    public void toggleSpeedBoost(){
        this.hasSpeedBoost = !hasSpeedBoost;
        determineSpeed();
    }
    public void upgradeMissile(){
        this.upgradedMissile = true;
    }
}
