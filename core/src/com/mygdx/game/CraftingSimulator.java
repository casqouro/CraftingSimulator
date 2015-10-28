package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

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
        Texture ham;
        Sprite hamSprite;
        BitmapFont font;
        
        int fieldSizeX;
        int fieldSizeY;    
        int[] inventory = new int[5];
        int playerX;
        int playerY;        
        int height;
        int width;
        int heightSpacing;
       	int widthSpacing;
        int inventorySizeOffset;
        Map currentMap;
        
        boolean resourceInventoryState = true; // well, the values used when rendering would differ based on true/false
        
	@Override
	public void create () {
            KeyProcessor processor = new KeyProcessor();
            Gdx.input.setInputProcessor(processor);
           
            jack = new ShapeRenderer();
            batch = new SpriteBatch();   
            ham = new Texture(new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\ham (1).png")); // C:\Users\Matthew\Desktop\CraftingSimulator\assets\ham
            hamSprite = new Sprite(ham);
            font = new BitmapFont();
            fieldSizeX = 10;
            fieldSizeY = 10;
            playerX = 0;
            playerY = 0;
            height = Gdx.graphics.getHeight();
            width = Gdx.graphics.getWidth();   
            heightSpacing = height / fieldSizeY;
            widthSpacing = (width - 128) / fieldSizeX;
            inventorySizeOffset = 1;
            currentMap = new Map(fieldSizeX, fieldSizeY);
	}
        
        private void drawResources() {
            jack.begin(ShapeType.Filled);
            for (int a = 0; a < fieldSizeX; a++) {
                for (int b = 0; b < fieldSizeY; b++) {
                    if (currentMap.mapBoard[a][b] == 2) {
                        jack.setColor(Color.GRAY);
                        jack.rect(a * widthSpacing, b * heightSpacing, widthSpacing, heightSpacing);
                    }
                    if (currentMap.mapBoard[a][b] == 3) {
                        jack.setColor(Color.BLUE);
                        jack.rect(a * widthSpacing, b * heightSpacing, widthSpacing, heightSpacing);
                    }
                }
            }
            jack.end();
        }
        
        /*
        private void drawPlayer() {
            jack.begin(ShapeType.Filled); 
            jack.setColor(Color.GREEN);
            jack.rect(playerX * widthSpacing, playerY * heightSpacing, widthSpacing, heightSpacing);
            jack.end();
        }
        */
        
        private void drawField() {
            jack.begin(ShapeType.Line);
            jack.setColor(Color.WHITE);
            
            // this draws the vertical lines
            for (int col = 0; col < fieldSizeX; col++) {
                jack.line((col * (width - (128 * inventorySizeOffset)))/fieldSizeX, 0, 
                          (col * (width - (128 * inventorySizeOffset)))/fieldSizeX, height);
            }
            // this draws the horizontal lines
            for (int row = 0; row < fieldSizeY; row++) {
                jack.line(0, row * height/fieldSizeY,
                          width - (129 * inventorySizeOffset), row * height/fieldSizeY); // 129, instead of 128?  Has to do with drawing the lines/resources.
            }  
            jack.end();            
        }
        
        public void drawResourceInterface() {
            // 640 by 480
            // so 140 by 480?
            if (resourceInventoryState) {
                jack.begin(ShapeType.Line);
                jack.setColor(Color.WHITE);
                jack.line(511, 0, 511, 480); // When set to 512, instead of 511, there was a one pixel gap when drawing resources.  Why?
                jack.end();
            }   
        }
                
        private void movePlayer(int x, int y) {
            playerX += x;
            playerY += y;
            
            int locationValue = currentMap.mapBoard[playerX][playerY];
            
            // if the player is implemented with this array, setting it to 0 could be bad!)
            if (locationValue != 0) {
                inventory[locationValue] += 1;
                currentMap.setHarvested(1);
                currentMap.mapBoard[playerX][playerY] = 0;
            }
        }
        
        private void fieldResizeUtility() {
            resourceInventoryState = !resourceInventoryState;
            
            if (resourceInventoryState) {
                widthSpacing = (width - 128) / fieldSizeX;
                inventorySizeOffset = 1;
            } else {
                widthSpacing = (width / fieldSizeX);
                inventorySizeOffset = 0;
            }
        }
        
	@Override
	public void render () {            
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
            
            if (currentMap.needToGenerateResource()) {
                currentMap.generateResources();
            }
            
            drawResources();
            drawResourceInterface();            
            //drawPlayer();
            drawField();
            
            batch.begin();
            if (resourceInventoryState) {
                for (int a = 0; a < inventory.length; a++) {
                    font.draw(batch, "HEYA: " + inventory[a], 522, 48 * (a + 2));
                }
            }
            batch.draw(hamSprite, playerX * widthSpacing, playerY * heightSpacing, 50, 50);
            batch.end();
	}
        
        private class KeyProcessor extends InputAdapter {
            
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.W:
                    case Keys.UP:
                        if (playerY + 1 < fieldSizeY) {
                            movePlayer(0, 1); }
                        break;
                    case Keys.A:
                    case Keys.LEFT:
                        if (playerX - 1 >= 0) {
                            movePlayer(-1, 0); }
                        break;
                    case Keys.S:
                    case Keys.DOWN:
                        if (playerY - 1 >= 0) {
                            movePlayer(0, -1); }          
                        break;
                    case Keys.D:
                    case Keys.RIGHT:
                        if (playerX + 1 < fieldSizeX) {
                            movePlayer(1, 0); }
                        break;
                    case Keys.I:
                        fieldResizeUtility();
                        break;
                }
                return true;
            }
        }
}

/* FEATURE REQUESTS...
    * Don't generate resource on current player location
    * Toggle the resource screen
    * Regenerate resources
*/

/* UPGRADES

   * Holding keys to move
   * Pointing with the mouse to auto-move to a location
   * Being able to store more types of resources
   * Being able to store more of each type of resource
   * Graphical upgrades
*/

/* Lessons Learned

   LIBGDX:
    1) Textures have a fixed height/width and can't be scaled up, but a sprite
        can be scaled up very easily.
*/