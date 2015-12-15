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
import java.awt.Point;

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
    TextureRegion wall;
    TextureRegion ore;
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
    
    int movementSpeed;
    int inventorySizeOffset;
    Map currentMap;
    Entity[] colliders;
    Entity hamEntity;
    Entity wallEntity;

    int xChange = 0;
    int yChange = 0;
    
    int xCollisionModifier = 1;
    int yCollisionModifier = 1;

    @Override
    public void create() {
        KeyProcessor processor = new KeyProcessor();
        Gdx.input.setInputProcessor(processor);

        batch = new SpriteBatch();
        ham = new TextureRegion(new Texture(new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\ham (1).png")));
        wall = new TextureRegion(new Texture(new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\wall.png")));
        ore = new TextureRegion(new Texture(new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ore\\orangeOre.png")));
        font = new BitmapFont();
        hamAtlas = new TextureAtlas(Gdx.files.internal("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\hamswalk.atlas"));
        hamAnim = new Animation(1 / 6f, hamAtlas.getRegions());
        hamAnim.setPlayMode(Animation.PlayMode.LOOP);
        hamWalkUpAtlas = new TextureAtlas(Gdx.files.internal("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\hamwalkup.atlas"));
        hamWalkUpAnim = new Animation(1 / 4f, hamWalkUpAtlas.getRegions());
        hamWalkUpAnim.setPlayMode(Animation.PlayMode.LOOP);
        grass = new TextureRegion(new Texture(new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\grass.png")));
        apple = new TextureRegion(new Texture(new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\apple.png")));
        cheese = new TextureRegion(new Texture(new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\cheese.png")));
        fieldSizeX = 10;
        fieldSizeY = 10;
        playerX = 250;
        playerY = 250;
        height = Gdx.graphics.getHeight();
        width = Gdx.graphics.getWidth();
        heightSpacing = height / fieldSizeY;
        widthSpacing = width / fieldSizeX;
        movementSpeed = 3;
        inventorySizeOffset = 1;
        currentMap = new Map(fieldSizeX, fieldSizeY);
        colliders = new Entity[5]; // wouldn't this need to be dynamic?
        
        hamEntity = new Entity(ham, 200, 200, 50, 50);
        wallEntity = new Entity(wall, 100, 100, 50, 50);
        colliders[0] = hamEntity;
        colliders[1] = wallEntity;
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

    private void drawBounds() {
        for (int a = 0; a < fieldSizeX; a++) {
            batch.draw(wall, (a % fieldSizeX) * widthSpacing, height - heightSpacing, widthSpacing, heightSpacing);
            batch.draw(wall, (a % fieldSizeX) * widthSpacing, 0, widthSpacing, heightSpacing);              
        }
        
        for (int a = 0; a < fieldSizeY; a++) {
            batch.draw(wall, width - widthSpacing, (a % fieldSizeY) * heightSpacing, widthSpacing, heightSpacing);
            batch.draw(wall, 0, (a % fieldSizeY) * heightSpacing, widthSpacing, heightSpacing);              
        }   
        
        wallEntity.drawEntity(batch);
    }
    
    private void drawBackground() {
        int size = fieldSizeX * fieldSizeY;
        for (int a = 0; a < size; a++) {
            batch.draw(grass, (a % fieldSizeX) * widthSpacing, (a / fieldSizeY) * heightSpacing, widthSpacing, heightSpacing);
        }
    }

    private TextureRegion orientKeyFrame() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        TextureRegion temp = hamAnim.getKeyFrame(elapsedTime);
       
        if (xChange < 0) {
            if (!temp.isFlipX()) {
                temp.flip(true, false);

                if (!ham.isFlipX()) {
                    ham.flip(true, false);
                }
            }
            return temp;
        }

        if (xChange > 0) {
            if (temp.isFlipX()) {
                temp.flip(true, false);

                if (ham.isFlipX()) {
                    ham.flip(true, false);
                }
            }
            return temp;
        }

        if (yChange < 0) {
            if (!ham.isFlipX()) {
                if (temp.isFlipX()) {
                    temp.flip(true, false);
                    return temp;
                }
            }

            if (ham.isFlipX()) {
                if (!temp.isFlipX()) {
                    temp.flip(true, false);
                    return temp;
                }
            }
            
            return temp;
        }
        
        if (yChange > 0) {
            return hamWalkUpAnim.getKeyFrame(elapsedTime);
        }

        return ham;
    }

    private void animatePlayer() {
        /* this works, but is sticky
        if (xChange != 0 || yChange != 0) {          
            if (!detectCollision(hamEntity, wallEntity)) {
                hamEntity.x += xChange;
                hamEntity.y += yChange;
            }
        }        
        batch.draw(orientKeyFrame(), hamEntity.x, hamEntity.y, 50, 50);
        */
        
        checkCollision(hamEntity, wallEntity);
        hamEntity.x += xChange * xCollisionModifier;
        hamEntity.y += yChange * yCollisionModifier;
        xCollisionModifier = 1;
        yCollisionModifier = 1;        
        batch.draw(orientKeyFrame(), hamEntity.x, hamEntity.y, 50, 50);        
    }
    
    // I have no idea why a second button down makes you stick.
    private void checkCollision(Entity a, Entity b) {
        if (a.x + a.width + xChange > b.x && a.x + xChange < b.x + b.width) {
            if (a.y + a.height + yChange > b.y && a.y + yChange < b.y + b.height) {

                if (a.x + a.width + xChange > b.x) {
                    xCollisionModifier = 0;
                }
                
                if (a.x + xChange < b.x + b.width) {
                    xCollisionModifier = 0;                    
                }
                
                if (a.y + a.height + yChange > b.y) {
                    yCollisionModifier = 0;
                }
                
                if (a.y + yChange < b.y + b.height) {
                    yCollisionModifier = 0;
                }
            }
        }
    }
    
    private boolean detectCollision(Entity a, Entity b) { 
        // The arbitrary offsets are due to the Ham character sprite not having flush borders.
        if (a.x + a.width + xChange - 17 > b.x && a.y + a.height + yChange - 30 > b.y) {
            if (a.x + xChange + 17 < b.x + b.width && a.y + yChange + 2 < b.y + b.height) {
                return true;
            }
        }        
        return false;
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        
        drawBackground();
        drawBounds();
        //batch.draw(ore, 100, 100);        
        animatePlayer();
        
        batch.end();

        if (currentMap.needToGenerateResource()) {
            currentMap.generateResources();
        }
    }

    private class KeyProcessor extends InputAdapter {

        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Keys.W:
                case Keys.UP:
                    yChange += movementSpeed;
                    break;
                case Keys.A:
                case Keys.LEFT:
                    xChange += -movementSpeed;
                    break;
                case Keys.S:
                case Keys.DOWN:
                    yChange += -movementSpeed;
                    break;
                case Keys.D:
                case Keys.RIGHT:
                    xChange += movementSpeed;
                    break;
            }

            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            switch (keycode) {
                case Keys.W:
                case Keys.UP:
                    yChange += -movementSpeed;
                    break;
                case Keys.A:
                case Keys.LEFT:
                    xChange += movementSpeed;
                    break;
                case Keys.S:
                case Keys.DOWN:
                    yChange += movementSpeed;
                    break;
                case Keys.D:
                case Keys.RIGHT:
                    xChange += -movementSpeed;
                    break;
            }
            return true;
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

*/

/* THINGS TO DO:
    Non-sticky collision.  If you hold two directions, such as up-and-left, and
    collision for "up" is triggered, no movement occurs.  The left movement
    should still happen if there's no collision there.

    Collision currently stops ALL movement via a boolean.  In the future
    collision could simply zero out the change found in the directio of collision.
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

    ANIMATION:

    1.  My initial chess-like movement system of locking input and doing all
        character animation before accepting more input did not couple well with
        a robust animation system where I could turn the character around mid-
        movement.  It was also a bit of a fun challenge to find how to keep
        sprites flipped left/right.

        In the end I realized the failure of the movement system was really
        based on my poor (initial) conceptualization of the programming link 
        between animations and movement!  So I scrapped it and made a fluid
        system.

        The first system was a 10x10 array and animations between squares had to
        be completed before accepting new input.  The second system has no array
        for tracking the character or resource locations as those are all now
        objects and maintain their location data.  The character is drawn at
        its (x,y) and the field is simply set as the resolution size (640/480).

        Overall it's simpler to implement and more responsive for the player, 
        but some games could use a chess-like animation-locking system and I
        could implement that, too.

        Some problems I faced (and solved):

        If you move left and flip sprites left as you move through the array of
        frames, some of the frames might never get flipped!  Now, if you want
        your character to continue facing left while walking down, you can
        suddenly have left-to-right frame flips.

        Flipping an entire array of frames isn't a pleasant solution because you
        might then be flipping tens of frames for one characters, or thousands
        for multiple characters every time they turn left or right, and perhaps
        not even playing all those flipped frames before flipping them back.

    2.  I got some valuable insights into the problems with clipping and 
        characters getting stuck.

        Clipping and animating: at a stand-still your character can be flush
        with the wall.  If its arm swing against the wall when the character
        moves, how do you handle that?

        When the arm animates against the wall, maintaining strict collision
        could be achieved by sliding the rest of the model back, but that would
        look absurd.

        You could implement a complex animation system which accounts for the
        closeness and doesn't animate the arm, but turns the character.  But in
        a lot of cases that would be overkill and you'd probably not have the
        budget.

        You could also just let the character partially clip into the wall.
        While not ideal the game is still playable and you could mitigate it
        with some minor offsets to minimize the clipping.

        Getting Stuck: When collision is detected coordinate changes are 
        prevented, so if you're not careful with collision handling you can
        update a characters coordinates so that they're collision-positive,
        and the character can animate and appear to move, but will be prevented
        from moving away from the collision.

        You could program a safety system allowing movements in directions away
        from the detected collision.  It would benefit the player but take more
        previous programming time, and you're really programming a fail-safe
        into a system which someone will argue should work right to begin with.
        It could also get complex quick in a game with a z-axis to consider,
        and, if implemented poorly, could make things worse by facilitating
        exploits.
*/