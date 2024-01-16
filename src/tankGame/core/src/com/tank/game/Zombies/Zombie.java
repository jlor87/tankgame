package com.tank.game.Zombies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.tank.game.Projectiles.Missile;
import com.tank.game.Projectiles.Projectiles;
import com.tank.game.Projectiles.UpgradedMissile;
import com.tank.game.Tanks.Tank;
import com.tank.game.Walls.Wall;

import java.util.List;
import java.util.Random;

public class Zombie {
    private int HP;
    private float speed;
    private float xCoordinate;
    private float yCoordinate;
    private int direction;
    private boolean killMode;
    private Tank nearestTarget;
    private TextureRegion currentFrame;
    private int idleWalkingCounter = 81;
    private final float width;
    private final float height;
    private float stateTime;

    // Zombie walking animations
    Animation<TextureRegion> zombieDLWalkingAnimation;
    Animation<TextureRegion> zombieLWalkingAnimation;
    Animation<TextureRegion> zombieULWalkingAnimation;
    Animation<TextureRegion> zombieUWalkingAnimation;
    Animation<TextureRegion> zombieURWalkingAnimation;
    Animation<TextureRegion> zombieRWalkingAnimation;
    Animation<TextureRegion> zombieDRWalkingAnimation;
    Animation<TextureRegion> zombieDWalkingAnimation;

    // Zombie running animations
    Animation<TextureRegion> zombieDLRunningAnimation;
    Animation<TextureRegion> zombieLRunningAnimation;
    Animation<TextureRegion> zombieULRunningAnimation;
    Animation<TextureRegion> zombieURunningAnimation;
    Animation<TextureRegion> zombieURRunningAnimation;
    Animation<TextureRegion> zombieRRunningAnimation;
    Animation<TextureRegion> zombieDRRunningAnimation;
    Animation<TextureRegion> zombieDRunningAnimation;
    Rectangle zombieBoundingBox;

    public Zombie(Tank player1, Tank player2, float zombieSpeed, int HP, Animation<TextureRegion> zombieUWalkingAnimation, Animation<TextureRegion> zombieULWalkingAnimation, Animation<TextureRegion> zombieLWalkingAnimation, Animation<TextureRegion> zombieDLWalkingAnimation, Animation<TextureRegion> zombieDWalkingAnimation, Animation<TextureRegion> zombieDRWalkingAnimation, Animation<TextureRegion> zombieRWalkingAnimation, Animation<TextureRegion> zombieURWalkingAnimation, Animation<TextureRegion> zombieURunningAnimation, Animation<TextureRegion> zombieULRunningAnimation, Animation<TextureRegion> zombieLRunningAnimation, Animation<TextureRegion> zombieDLRunningAnimation, Animation<TextureRegion> zombieDRunningAnimation, Animation<TextureRegion> zombieDRRunningAnimation, Animation<TextureRegion> zombieRRunningAnimation, Animation<TextureRegion> zombieURRunningAnimation, List<Rectangle> obstacles, List<Wall> walls){
        this.height = 96;
        this.width = 96;
        Random randomXCoordinate = new Random();
        Random randomYCoordinate = new Random();
        float x = 0;
        float y = 0;
        boolean canSpawnHere = false;
        while(!canSpawnHere){
            x = randomXCoordinate.nextInt(1900);
            y = randomYCoordinate.nextInt(1400);
            // Check to ensure that zombies do not spawn in the safe zone or in/on obstacles
            canSpawnHere = checkAreaIfOkayToSpawn(x, y, obstacles, walls, player1, player2);
        }
        this.xCoordinate = x;
        this.yCoordinate = y;
        this.HP = HP;
        this.speed = zombieSpeed;
        this.killMode = false;
        this.stateTime = 0f;
        this.nearestTarget = null;
        zombieBoundingBox = new Rectangle();
        zombieBoundingBox.set(this.xCoordinate, this.yCoordinate, this.width, this.height);
        idleWalking(); // Sets the initial direction of the zombie


        // Load all the walking and running animations into the Zombie class's private variables
        this.zombieUWalkingAnimation = zombieUWalkingAnimation;
        this.zombieULWalkingAnimation = zombieULWalkingAnimation;
        this.zombieLWalkingAnimation = zombieLWalkingAnimation;
        this.zombieDLWalkingAnimation = zombieDLWalkingAnimation;
        this.zombieDWalkingAnimation = zombieDWalkingAnimation;
        this.zombieDRWalkingAnimation = zombieDRWalkingAnimation;
        this.zombieRWalkingAnimation = zombieRWalkingAnimation;
        this.zombieURWalkingAnimation = zombieURWalkingAnimation;
        this.zombieURunningAnimation = zombieURunningAnimation;
        this.zombieULRunningAnimation = zombieULRunningAnimation;
        this.zombieLRunningAnimation = zombieLRunningAnimation;
        this.zombieDLRunningAnimation = zombieDLRunningAnimation;
        this.zombieDRunningAnimation = zombieDRunningAnimation;
        this.zombieDRRunningAnimation = zombieDRRunningAnimation;
        this.zombieRRunningAnimation = zombieRRunningAnimation;
        this.zombieURRunningAnimation = zombieURRunningAnimation;

        switch (direction){
            case 1: // up
                currentFrame = zombieUWalkingAnimation.getKeyFrame(stateTime, true);
                break;
            case 2: // up-left
                currentFrame = zombieULWalkingAnimation.getKeyFrame(stateTime, true);
                break;
            case 3: // left
                currentFrame = zombieLWalkingAnimation.getKeyFrame(stateTime, true);
                break;
            case 4: // down-left
                currentFrame = zombieDLWalkingAnimation.getKeyFrame(stateTime, true);
                break;
            case 5: // down
                currentFrame = zombieDWalkingAnimation.getKeyFrame(stateTime, true);
                break;
            case 6: // down-right
                currentFrame = zombieDRWalkingAnimation.getKeyFrame(stateTime, true);
                break;
            case 7: // right
                currentFrame = zombieRWalkingAnimation.getKeyFrame(stateTime, true);
                break;
            case 8: // up-right
                currentFrame = zombieURWalkingAnimation.getKeyFrame(stateTime, true);
                break;
        }
    }

    public boolean checkAreaIfOkayToSpawn(float xCoordinate, float yCoordinate, List<Rectangle> obstacles, List<Wall> walls, Tank player1, Tank player2){
        if(xCoordinate < 100 && yCoordinate < 305){
            return false;
        }
        Rectangle potentialNewZombie = new Rectangle(xCoordinate, yCoordinate, this.width, this.height);
        for(Rectangle obstacle : obstacles){
            if(potentialNewZombie.overlaps(obstacle)){
                return false;
            }
        }
        for(Wall wall : walls){
            if(potentialNewZombie.overlaps(wall.getWallBoundingBox())){
                return false;
            }
        }
        return (!(Math.abs(xCoordinate - player1.getxCoordinate()) < 200) || !(Math.abs(yCoordinate - player1.getyCoordinate()) < 200)) && (!(Math.abs(xCoordinate - player2.getxCoordinate()) < 200) || !(Math.abs(yCoordinate - player2.getyCoordinate()) < 200));
    }
    public boolean checkIfPlayersAreInSafeZone(Tank player1, Tank player2){
        // Both players in the safe zone
        if((player1.getxCoordinate() < 100 && player1.getyCoordinate() < 305) && (player2.getxCoordinate() < 100 && player2.getyCoordinate() < 305)){
            revertToIdleWalking();
            return true;
        }
        else {
            return false;
        }
    }
    public boolean checkIfTargetIsInSafeZone(){
        if (nearestTarget.getyCoordinate() < 305 && nearestTarget.getxCoordinate() < 100) {
            revertToIdleWalking();
            return true;
        } else {
            return false;
        }
    }
    public void findDirectionToPlayer(){
        if(xCoordinate == nearestTarget.getxCoordinate() || Math.abs(xCoordinate - nearestTarget.getxCoordinate()) < 16){
            if(yCoordinate < nearestTarget.getyCoordinate()){
                direction = 1;  // Up
            }
            else{
                direction = 5; // Down
            }
        }
        else if(xCoordinate > nearestTarget.getxCoordinate()){
            if(yCoordinate == nearestTarget.getyCoordinate() || Math.abs(yCoordinate - nearestTarget.getyCoordinate()) < 16){
                direction = 3; // Left
            }
            else if(yCoordinate < nearestTarget.getyCoordinate()){
                direction = 2; // Up-Left
            }
            else {
                direction = 4; // Down-Left
            }
        }
        else{
            if(yCoordinate == nearestTarget.getyCoordinate() || Math.abs(yCoordinate - nearestTarget.getyCoordinate()) < 16){
                direction = 7; // Right
            }
            else if(yCoordinate < nearestTarget.getyCoordinate()){
                direction = 8; // Up-Right
            }
            else{
                direction = 6; // Down-Right
            }
        }
    }
    public TextureRegion getCurrentFrame(){
        return currentFrame;
    }
    public int getDirection(){
        return this.direction;
    }
    public float getDistance(Tank currentTank){
        return (float) Math.sqrt(Math.abs(currentTank.getxCoordinate()-xCoordinate)*Math.abs(currentTank.getxCoordinate()-xCoordinate) + Math.abs(currentTank.getyCoordinate()-yCoordinate)*Math.abs(currentTank.getyCoordinate()-yCoordinate));
    }

    public float getHeight() {
        return height;
    }
    public int getHP(){
        return this.HP;
    }
    public Tank getTarget(){
        return this.nearestTarget;
    }

    public float getWidth() {
        return width;
    }

    public float getxCoordinate() {
        return xCoordinate;
    }

    public float getyCoordinate(){
        return yCoordinate;
    }
    public Rectangle getZombieBoundingBox(){
        return zombieBoundingBox;
    }
    public boolean hasTarget(){
        return nearestTarget != null;
    }
    public void idleWalking(){
        this.nearestTarget = null;
        if(idleWalkingCounter > 70) {
            Random random = new Random();
            int direction = random.nextInt(7) + 1;
            setDirection(direction);
            idleWalkingCounter = 0;
        } else{
            idleWalkingCounter++;
        }
    }
    public boolean isInKillMode(){
        return killMode;
    }
    public void move(float newX, float newY){
        this.xCoordinate = newX;
        this.yCoordinate = newY;
        zombieBoundingBox.setPosition(xCoordinate, yCoordinate);
    }
    public float potentialXMove() {
        float diagonalSpeed = (float) (speed * 0.6);
        float potentialXCoordinate = xCoordinate;
        switch (direction) {
            case 1: // up
                break;
            case 2: // up-left
            case 4: // down-left
                potentialXCoordinate = xCoordinate - diagonalSpeed;
                break;
            case 3: // left
                potentialXCoordinate = xCoordinate - speed;
                break;
            case 5: // down
                break;
            case 6: // down-right
            case 8: // up-right
                potentialXCoordinate = xCoordinate + diagonalSpeed;
                break;
            case 7: // right
                potentialXCoordinate = xCoordinate + speed;
                break;
        }
        if(potentialXCoordinate < 0 || potentialXCoordinate > 2020){
            return xCoordinate;
        } else {
            return potentialXCoordinate;
        }
    }
    public float potentialYMove() {
        float diagonalSpeed = (float) (speed * 0.6);
        float potentialYPosition = yCoordinate;
        switch(direction){
            case 1: // up
                potentialYPosition = yCoordinate + speed;
                break;
            case 2: // up-left
            case 8: // up-right
                potentialYPosition = yCoordinate + diagonalSpeed;
                break;
            case 3: // left
                break;
            case 4: // down-left
            case 6: // down-right
                potentialYPosition = yCoordinate - diagonalSpeed;
                break;
            case 5: // down
                potentialYPosition = yCoordinate - speed;
                break;
            case 7: // right
                break;
        }
        if(potentialYPosition < 0 || potentialYPosition > 1484){
            return yCoordinate;
        } else {
            return potentialYPosition;
        }
    }
    public void reduceHealth(Projectiles typeOfProjectile){
        if(typeOfProjectile instanceof Missile){
            this.HP = HP - 20;
        }
        else if(typeOfProjectile instanceof UpgradedMissile){
            this.HP = HP - 40;
        }
    }
    public void revertToIdleWalking(){
        this.killMode = false;
        this.nearestTarget = null;
        this.speed = 1.4f;
    }
    public void scanForPlayers(Tank player1, Tank player2){
        float distanceFromPlayer1 = getDistance(player1);
        float distanceFromPlayer2 = getDistance(player2);

        // Zombies are biased toward Player 1 due to scanning for Player 1 first. So this random generator scrambles that priority
        Random random = new Random();
        int number = random.nextInt(2);
        if(number == 0) {
            if (!(player1.getxCoordinate() < 100 && player1.getyCoordinate() < 305)) { // If player 1 is not in the safe zone, then player 1 can be targeted
                if (distanceFromPlayer1 < 230) {
                    setKillMode();
                    setNearestTarget(player1);
                }
            } else if (!(player2.getxCoordinate() < 100 && player2.getyCoordinate() < 305)) { // If player 2 is not in the safe zone, then player 2 can be targeted
                if (distanceFromPlayer2 < 230) {
                    setKillMode();
                    setNearestTarget(player2);
                }
            }
        } else{
            if (!(player2.getxCoordinate() < 100 && player2.getyCoordinate() < 305)) { // If player 2 is not in the safe zone, then player 2 can be targeted
                if (distanceFromPlayer2 < 230) {
                    setKillMode();
                    setNearestTarget(player2);
                }
            }
            else if (!(player1.getxCoordinate() < 100 && player1.getyCoordinate() < 305)) { // If player 1 is not in the safe zone, then player 1 can be targeted
                if (distanceFromPlayer1 < 230) {
                    setKillMode();
                    setNearestTarget(player1);
                }
            }
        }
    }
    public void setDirection(int direction){
        this.direction = direction;
    }
    public void setKillMode(){
        killMode = true;
        speed = 2.15f;
    }
    public void setNearestTarget(Tank nearestTank){
        this.nearestTarget = nearestTank;
    }
    public void setPosition(float xCoordinate, float yCoordinate){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }
    public void updateAnimation(){
        if(!isInKillMode()){ // Getting the animation frame when the zombie is walking, per direction
            switch (direction){
                case 1: // up
                    currentFrame = zombieUWalkingAnimation.getKeyFrame(stateTime, true);
                    break;
                case 2: // up-left
                    currentFrame = zombieULWalkingAnimation.getKeyFrame(stateTime, true);
                    break;
                case 3: // left
                    currentFrame = zombieLWalkingAnimation.getKeyFrame(stateTime, true);
                    break;
                case 4: // down-left
                    currentFrame = zombieDLWalkingAnimation.getKeyFrame(stateTime, true);
                    break;
                case 5: // down
                    currentFrame = zombieDWalkingAnimation.getKeyFrame(stateTime, true);
                    break;
                case 6: // down-right
                    currentFrame = zombieDRWalkingAnimation.getKeyFrame(stateTime, true);
                    break;
                case 7: // right
                    currentFrame = zombieRWalkingAnimation.getKeyFrame(stateTime, true);
                    break;
                case 8: // up-right
                    currentFrame = zombieURWalkingAnimation.getKeyFrame(stateTime, true);
                    break;
            }
        }
        else{ // Getting the animation frame when the zombie is running (i.e. in kill mode)
            switch (direction){
                case 1: // up
                    currentFrame = zombieURunningAnimation.getKeyFrame(stateTime, true);
                    break;
                case 2: // up-left
                    currentFrame = zombieULRunningAnimation.getKeyFrame(stateTime, true);
                    break;
                case 3: // left
                    currentFrame = zombieLRunningAnimation.getKeyFrame(stateTime, true);
                    break;
                case 4: // down-left
                    currentFrame = zombieDLRunningAnimation.getKeyFrame(stateTime, true);
                    break;
                case 5: // down
                    currentFrame = zombieDRunningAnimation.getKeyFrame(stateTime, true);
                    break;
                case 6: // down-right
                    currentFrame = zombieDRRunningAnimation.getKeyFrame(stateTime, true);
                    break;
                case 7: // right
                    currentFrame = zombieRRunningAnimation.getKeyFrame(stateTime, true);
                    break;
                case 8: // up-right
                    currentFrame = zombieURRunningAnimation.getKeyFrame(stateTime, true);
                    break;
            }
        }
    }
    public void updateStateTime(float deltaTime) {
        stateTime += deltaTime;
    }
}
