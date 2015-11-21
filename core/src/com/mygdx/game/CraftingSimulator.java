package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// investigate disabling continuous rendering
// investigate game loops

/*  Looping through an array to draw stuff is wasteful.  Have resources exist as
objects which maintain their color and location.  Use a linked list to track 
every object, with the first entry always being the player.  When generating a
resource add it to the list.  When consuming a resource delete it from the list.

Right now it's looping through and checking 100 things, 97 of which are empty!
*/

public class CraftingSimulator extends ApplicationAdapter {	
        SpriteBatch batch;   
        TextureRegion ham;
        TextureRegion grass;
        TextureRegion apple;
        TextureRegion cheese;
        BitmapFont font;
        
        TextureAtlas hamAtlas;
        Animation hamAnim;
        TextureAtlas hamWalkUpAtlas;
        Animation hamWalkUpAnim;
        private float elapsedTime = 0;
        
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
        String direction;
        
        boolean resourceInventoryState = false;
        boolean inputLockedState = false;
        
	@Override
	public void create () {
            KeyProcessor processor = new KeyProcessor();
            Gdx.input.setInputProcessor(processor);
            
            batch = new SpriteBatch();   
            ham = new TextureRegion(new Texture(new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\ham (1).png")));
            font = new BitmapFont();
            hamAtlas = new TextureAtlas(Gdx.files.internal("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\hamswalk.atlas"));
            hamAnim = new Animation(1/6f, hamAtlas.getRegions());
            hamAnim.setPlayMode(Animation.PlayMode.NORMAL);
            hamWalkUpAtlas = new TextureAtlas(Gdx.files.internal("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\hamwalkup.atlas"));            
            hamWalkUpAnim = new Animation(1/4f, hamWalkUpAtlas.getRegions());
            hamWalkUpAnim.setPlayMode(Animation.PlayMode.NORMAL);
            grass = new TextureRegion(new Texture(new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\grass.png")));
            apple = new TextureRegion(new Texture(new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\apple.png")));
            cheese = new TextureRegion(new Texture(new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\cheese.png")));
            fieldSizeX = 10;
            fieldSizeY = 10;
            playerX = 5;
            playerY = 5;
            height = Gdx.graphics.getHeight();
            width = Gdx.graphics.getWidth();   
            heightSpacing = height / fieldSizeY;
            widthSpacing = width / fieldSizeX;
            inventorySizeOffset = 1;
            currentMap = new Map(fieldSizeX, fieldSizeY);
	}
        
        private void drawResources() {
            int justify = (widthSpacing - apple.getRegionWidth()) / 2;
            for (int a = 0; a < fieldSizeX; a++) {
                for (int b = 0; b < fieldSizeY; b++) {
                    if (currentMap.mapBoard[a][b] == 2) {
                        batch.draw(apple, (a * widthSpacing) + justify, b * heightSpacing);
                    }
                    if (currentMap.mapBoard[a][b] == 3) {
                        batch.draw(cheese, (a * widthSpacing) + justify, b * heightSpacing);
                    }
                }
            }
        }
        
        private void drawBackground() {
            int size = fieldSizeX * fieldSizeY;
            for (int a = 0; a < size; a++ ) {
                batch.draw(grass, (a % fieldSizeX) * widthSpacing, (a / fieldSizeY) * heightSpacing, widthSpacing, heightSpacing);
            }            
        }        
               
        public void drawResourceInterface() {
            if (resourceInventoryState) {
            }   
        }
            
        private void movePlayer(int x, int y) {
            playerX += x;
            playerY += y;
            
            int locationValue = currentMap.mapBoard[playerX][playerY];
            
            if (locationValue != 0) {
                inventory[locationValue] += 1;
                currentMap.setHarvested(1);
                currentMap.mapBoard[playerX][playerY] = 0;
            }
        }
        
	@Override
	public void render () {            
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
            
            //drawResourceInterface();                        
            batch.begin();
            drawBackground();
            drawResources();
            
                if (inputLockedState) {  // (location * spacing) + (spacing * currentFrame / totalFrames)
                    TextureRegion temp;
                    float nextPosition;
                    elapsedTime += Gdx.graphics.getDeltaTime();
                    switch (direction) {
                        case "W":
                            temp = hamWalkUpAnim.getKeyFrame(elapsedTime);
                            nextPosition = (playerY * heightSpacing) + (heightSpacing * (float) hamWalkUpAnim.getKeyFrameIndex(elapsedTime) / hamWalkUpAnim.getKeyFrames().length);
                            batch.draw(temp, (playerX * widthSpacing), nextPosition + 2, 50, 50);
                            break;
                        case "A":
                            temp = hamAnim.getKeyFrame(elapsedTime);
                            nextPosition = (playerX * widthSpacing) - (widthSpacing * (float) hamAnim.getKeyFrameIndex(elapsedTime) / hamAnim.getKeyFrames().length);
                            if (!temp.isFlipX()) {
                                temp.flip(true, false);
                            }
                            batch.draw(temp, nextPosition, (playerY * heightSpacing) + 2, 50, 50);                           
                            break;
                        case "S":    
                            temp = hamAnim.getKeyFrame(elapsedTime);
                            nextPosition = (playerY * heightSpacing) - (heightSpacing * (float) hamWalkUpAnim.getKeyFrameIndex(elapsedTime) / hamWalkUpAnim.getKeyFrames().length);
                            batch.draw(temp, (playerX * widthSpacing), nextPosition + 2, 50, 50);
                            break;
                        case "D":
                            temp = hamAnim.getKeyFrame(elapsedTime);
                            nextPosition = (playerX * widthSpacing) + (widthSpacing * (float) hamAnim.getKeyFrameIndex(elapsedTime) / hamAnim.getKeyFrames().length);
                            if (temp.isFlipX()) {
                                temp.flip(true, false);
                            }               
                            batch.draw(temp, nextPosition, (playerY * heightSpacing) + 2, 50, 50);
                            break;
                    }
                     
                    if (hamAnim.isAnimationFinished(elapsedTime)) {
                        elapsedTime = 0;
                        
                        switch (direction) {
                        case "W": 
                            movePlayer(0, 1);
                            break;
                        case "A":  
                            if (!ham.isFlipX()) {
                                ham.flip(true, false);
                            }
                            movePlayer(-1, 0);                            
                            break;
                        case "S":   
                            movePlayer(0, -1);                            
                            break;
                        case "D": 
                            if (ham.isFlipX()) {
                                ham.flip(true, false);
                            }     
                            movePlayer(1, 0);                                                        
                            break;                            
                        }
                        
                        inputLockedState = false;                    
                    }
                } else {
                    batch.draw(ham, playerX * widthSpacing, (playerY * heightSpacing) + 2, 50, 50);
                }
                 
                if (resourceInventoryState) {
                    for (int a = 0; a < inventory.length; a++) {
                        font.draw(batch, "HEYA: " + inventory[a], 522, 48 * (a + 2));
                    }
                }  
            batch.end();
       
            if (currentMap.needToGenerateResource()) {
                currentMap.generateResources();
            }
	}
        
        private class KeyProcessor extends InputAdapter {

            @Override
            public boolean keyDown(int keycode) {
                if (!inputLockedState) {
                    switch (keycode) {
                        case Keys.W:
                        case Keys.UP:
                            if (playerY + 1 < fieldSizeY) {
                                inputLockedState = true;
                                direction = "W"; }
                            break;
                        case Keys.A:
                        case Keys.LEFT:
                            if (playerX - 1 >= 0) {
                                inputLockedState = true;                                
                                direction = "A"; }                                
                            break;
                        case Keys.S:
                        case Keys.DOWN:
                            if (playerY - 1 >= 0) {
                                inputLockedState = true;                                
                                direction = "S"; }                                
                            break;
                        case Keys.D:
                        case Keys.RIGHT:
                            if (playerX + 1 < fieldSizeX) {
                                inputLockedState = true;                                
                                direction = "D"; }                                
                            break;
                        case Keys.I:
                            resourceInventoryState = !resourceInventoryState;
                            break;
                    }  
                    return true;
                }
                return false;
            }
        }
}

/* FEATURE REQUESTS...
    * Don't generate resource on current player location
        ^<--- How should the Map class know where the player is?  Every time
                generateResources() is called, should it pass (x, y)?  Or maybe
                it should take an array of (x, y) so that multiple no-generate
                positions could be passed?

                For efficiency, instead of brute-forcing the location of 
                resources (100 spaces, 20 are no-resource spaces, you keep
                randomly trying to place resources...could be bad?) I could map
                overall map array to a smaller array of open spaces, randomly
                choose among them, then pass the value into the map array.

    * Inventory screen.
        a) Static, doesn't move
        b) Toggle-able with 'I' key
        c) Appearance?  Textures + Background?  How to write text?

    * Movement tweaks:
        "A" is actually a significant change, definitely positive as it'll 
        require, perhaps, a more mature approach to handling movement.

        Read here: https://github.com/libgdx/libgdx/wiki/Event-handling

        The rough idea:
        Create a boolean flag for each direction
        On KeyDown set that flag to true
        On KeyUp set that flag to false
        When KeyDown is true, you get your delta time and do your animating.

        Right now I'm using a simple grid, and reconciling the grid with the
        level of movement finesse I want is the problem, I think.  This will be
        a step forward so that in future projects I should be comfortable with
        moving a sprite around fluidly, rather than stepping from grid to grid
        while running an animation.

        a) Holding a key down should continue input
        b) Movement queue, so input receiving WHILE moving is then processed?
        c) Reversing movement WHILE movement is in process.  If you press right
            and the animation is playing, pressing left during the animation
            should immediately turn the character around and head him the other
            direction.
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

    2) TextureRegion.flip(bool-for-x, bool-for-y) is permanent.  If you flip 
        textureRegion's so you can animate to the left, you need to flip them
        back when you decide to animate to the right again.

        Also, it's a relative switch.  So using:
        tr.flip(true, false) 
        tr.flip(false, false) does not reverse the flip.

        tr.flip(true, false)
        tr.flip(true, false) is a full set of flips, back to the original.

   JAVA:
    1) float x = int a / int b ---> returns 0.  You must explicitly cast that
        operation as FLOAT division, because otherwise it's INTEGER division,
        regardless of the recipient variable being declared as a float.

        The question becomes "Why not have automatic casts in Java?"  Answers
        indicates that (a) this would violate type-safety, which is a feature of
        the language, so casts much be EXPLICIT, and (b) it would move errors
        from compile-time to run-time (some people like, some don't!)
*/