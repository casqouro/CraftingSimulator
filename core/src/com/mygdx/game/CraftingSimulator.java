package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

// investigate disabling continuous rendering
// investigate game loops

public class CraftingSimulator extends ApplicationAdapter {
	SpriteBatch batch;   
        ShapeRenderer jack;
        
        int fieldSizeX;
        int fieldSizeY;
        int[][] gameBoard = new int[fieldSizeX][fieldSizeY];      
        int playerX = 5;
        int playerY = 5;        
        int height;
        int width;
        int heightSpacing;
       	int widthSpacing;
        
	@Override
	public void create () {
            KeyProcessor processor = new KeyProcessor();
            Gdx.input.setInputProcessor(processor);
            
            jack = new ShapeRenderer();
            batch = new SpriteBatch();            
            fieldSizeX = 10;
            fieldSizeY = 10;
            gameBoard = new int[fieldSizeX][fieldSizeY];
            playerX = 0;
            playerY = 0;
            height = Gdx.graphics.getHeight();
            width = Gdx.graphics.getWidth();      
            heightSpacing = height / fieldSizeY;
            widthSpacing = width / fieldSizeX;
            
	}
        
	@Override
	public void render () {            
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
                
            jack.begin(ShapeType.Filled);
            jack.setColor(Color.GREEN);
            jack.rect(playerX * widthSpacing, playerY * heightSpacing, widthSpacing, heightSpacing);
            jack.end();
            
            jack.begin(ShapeType.Line);
            jack.setColor(Color.WHITE);
            
            for (int col = 0; col < fieldSizeX; col++) {
                jack.line(col * width/fieldSizeX, 0, 
                          col * width/fieldSizeX, height);
            }
            
            for (int row = 0; row < fieldSizeY; row++) {
                jack.line(0, row * height/fieldSizeY,
                          width, row * height/fieldSizeY);
            }            
            

            
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                
            }           
                      
            jack.end();
                
            batch.begin();
            batch.end();
	}
        
        private class KeyProcessor extends InputAdapter {

            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.W:
                        if (playerY + 1 < fieldSizeY) {
                            playerY += 1; }
                        break;
                    case Keys.A:
                        if (playerX - 1 >= 0) {
                            playerX -= 1; }
                        break;
                    case Keys.S:
                        if (playerY - 1 >= 0) {
                            playerY -= 1; }          
                        break;
                    case Keys.D:
                        if (playerX + 1 < fieldSizeX) {
                            playerX += 1; }
                        break;
                }
                return true;
            }
        }
}
