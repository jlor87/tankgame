package com.tank.game.MiniMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.tank.game.TankGame;
import com.tank.game.Tanks.Tank;

public class MiniMap extends Actor {
    private final Texture mapTexture;
    private final Texture tankTexture;
    private final Sprite tankIconSprite1;
    private final Sprite tankIconSprite2;
    private final TankGame game;

    public MiniMap(TankGame game) {
        this.game = game;
        this.mapTexture = new Texture("miniMap.jpg");
        this.tankTexture = new Texture("redDot.jpg");
        this.tankIconSprite1 = new Sprite(tankTexture);
        this.tankIconSprite2 = new Sprite(tankTexture);
    }
    public float calculateX(float tankXCoordinate){
        return (tankXCoordinate / 8);
    }
    public float calculateY(float tankYCoordinate){
        return tankYCoordinate / 8;
    }
    public Texture getMapTexture(){
        return mapTexture;
    }
    public Sprite getTankIconSprite1(){
        return tankIconSprite1;
    }
    public Sprite getTankIconSprite2(){
        return tankIconSprite2;
    }
    public void setBothMiniMapPositions(Tank tank1, Tank tank2){
        tankIconSprite1.setPosition(calculateX(tank1.getxCoordinate()), calculateY(tank1.getyCoordinate()));
        tankIconSprite2.setPosition(calculateX(tank2.getxCoordinate()), calculateY(tank2.getyCoordinate()));
    }
}