package com.tank.game.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tank.game.Tanks.Tank;

public class UserInterface {
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Tank currentTank;

    // Render the health bar background (e.g., a simple rectangle)
    public UserInterface(Tank currentTank, SpriteBatch batch){
        shapeRenderer = new ShapeRenderer();
        this.currentTank = currentTank;
        this.batch = batch;
    }

    public void renderHPandLivesOnUI(){
        float xPosition = 930;
        float yPosition = 626;
        for(int i = 0; i < currentTank.getLives(); i++){
            batch.draw(currentTank.getTexture(), xPosition, yPosition);
            xPosition = xPosition + 80;
        }


        // Convert the tank's HP into the width of the health bar
        float calculatedHP;
        if(currentTank.getHP() > 100){
            calculatedHP = 360f;
        }
        else{
            calculatedHP = currentTank.getHP() * 3.6f;
        }

        // Background to the health bar
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(900, 495, 360, 75);
        shapeRenderer.end();

        // Actual HP
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(900, 495, calculatedHP, 80);
        shapeRenderer.end();
    }

}
