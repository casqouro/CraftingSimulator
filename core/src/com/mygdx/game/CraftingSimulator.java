package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.util.LinkedList;
import java.util.Random;

// investigate disabling continuous rendering
// investigate game loops

/*  Looping through an array to draw stuff is wasteful.  Have resources exist as
objects which maintain their color and location.  Use a linked list to track 
every object, with the first entry always being the player.  When generating a
resource add it to the list.  When consuming a resource delete it from the list.

Right now it's looping through and checking 100 things, 97 of which are empty!
*/

public class CraftingSimulator extends ApplicationAdapter {
        ShapeRenderer jack;	
        SpriteBatch batch;   
        
        int fieldSizeX;
        int fieldSizeY;
        int[][] gameBoard = new int[fieldSizeX][fieldSizeY];      
        int playerX;
        int playerY;        
        int height;
        int width;
        int heightSpacing;
       	int widthSpacing;
        LinkedList gameObjects;
        
        boolean simpleState = false;
        
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
            gameObjects = new LinkedList();
            
            //gameBoard[1][1] = 2;
            //gameBoard[3][9] = 3;
	}
        
        // need to have some sort of state so this only happens once during the render cycle
        // otherwise it's going to call generateResources() like a fucking madman, constantly
        private void generateResources() {
            int fieldSize = fieldSizeX * fieldSizeY;            
            int quantity = fieldSize / 10;                      
            int number;
            Random ran = new Random();
            
            for (int a = 0; a < quantity; a++) {                
                number = ran.nextInt(fieldSize);
                                
                if (gameBoard[number / fieldSizeX][number % fieldSizeY] == 0) {
                    gameBoard[number / fieldSizeX][number % fieldSizeY] = ran.nextInt(2) + 2; // 2-3
                }
            }
            
            simpleState = true;
        }
        
        private void drawResources() {
            jack.begin(ShapeType.Filled);
            for (int a = 0; a < fieldSizeX; a++) {
                for (int b = 0; b < fieldSizeY; b++) {
                    if (gameBoard[a][b] == 2) {
                        jack.setColor(Color.GRAY);
                        jack.rect(a * widthSpacing, b * heightSpacing, widthSpacing, heightSpacing);
                    }
                    if (gameBoard[a][b] == 3) {
                        jack.setColor(Color.BLUE);
                        jack.rect(a * widthSpacing, b * heightSpacing, widthSpacing, heightSpacing);
                    }
                }
            }
            jack.end();
        }
        
        private void drawPlayer() {
            jack.begin(ShapeType.Filled);
            jack.setColor(Color.GREEN);
            jack.rect(playerX * widthSpacing, playerY * heightSpacing, widthSpacing, heightSpacing);
            jack.end();
        }
        
        private void drawField() {
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
            jack.end();            
        }
        
	@Override
	public void render () {            
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
                
            if (!simpleState) {
                generateResources();
            }
            drawResources();
            drawPlayer();
            drawField();
                        
            batch.begin();
            batch.end();
	}
        
        private class KeyProcessor extends InputAdapter {
            
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.W:
                    case Keys.UP:
                        if (playerY + 1 < fieldSizeY) {
                            playerY += 1; }
                        break;
                    case Keys.A:
                    case Keys.LEFT:
                        if (playerX - 1 >= 0) {
                            playerX -= 1; }
                        break;
                    case Keys.S:
                    case Keys.DOWN:
                        if (playerY - 1 >= 0) {
                            playerY -= 1; }          
                        break;
                    case Keys.D:
                    case Keys.RIGHT:
                        if (playerX + 1 < fieldSizeX) {
                            playerX += 1; }
                        break;
                }
                return true;
            }
        }
}