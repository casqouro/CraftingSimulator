package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

// investigate disabling continuous rendering
// investigate game loops

/* resizing the window manually should be disabled
   resizing the window manually fucks up the input...will that happen with a menu option for resizing?
*/
public class CraftingSimulator extends ApplicationAdapter {

    SpriteBatch batch;
    GameBoard test;
    PixelMiningGame one; 
    SiftingGame sifting;

    private float elapsedTime = 0;
    int playerX;
    int playerY;

    boolean moveUp;
    boolean moveLeft;
    boolean moveDown;
    boolean moveRight;
    
    int xChange = 0;
    int yChange = 0;

    @Override
    public void create() {                  
        KeyProcessor processor = new KeyProcessor();
        //Gdx.input.setInputProcessor(processor);

        batch = new SpriteBatch();
        test = new GameBoard(batch, 10, 10);        
        playerX = 250;
        playerY = 250;

        one = new PixelMiningGame(batch, 5);
        try {
            sifting = new SiftingGame(batch);
        } catch (IOException ex) {
            Logger.getLogger(CraftingSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        Gdx.input.setInputProcessor(sifting.getStage());
    }
    
    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                           
        batch.begin();            
        batch.end();
        
        sifting.getStage().act();        
        sifting.getStage().draw();         
    }    
    
    private class KeyProcessor extends InputAdapter {

        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Keys.W:
                case Keys.UP:
                    moveUp = true;
                    break;
                case Keys.A:
                case Keys.LEFT:
                    moveLeft = true;
                    break;
                case Keys.S:
                case Keys.DOWN:
                    moveDown = true;
                    break;
                case Keys.D:
                case Keys.RIGHT:
                    moveRight = true;
                    break;
            }

            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            switch (keycode) {
                case Keys.W:
                case Keys.UP:
                    moveUp = false;
                    break;
                case Keys.A:
                case Keys.LEFT:
                    moveLeft = false;
                    break;
                case Keys.S:
                case Keys.DOWN:
                    moveDown = false;
                    break;
                case Keys.D:
                case Keys.RIGHT:
                    moveRight = false;
                    break;
            }
            return true;
        }
    }    

    private void deprecated () {
    /*
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
        calculateMovementSpeed();
        
        if (isCollision(hamEntity, wallEntity)) {    
            modifyMovement();  
        }
        
        hamEntity.x += xChange;
        hamEntity.y += yChange;

        batch.draw(orientKeyFrame(), hamEntity.x, hamEntity.y, 50, 50);        
    }
    
    // the "move" booleans should only be changed on keyDown / keyUp!
    private void calculateMovementSpeed() {
        xChange = 0;
        yChange = 0;
        
        if (moveUp) {
            yChange += 3;
        }
        
        if (moveLeft) {
            xChange -= 3;
        }
        
        if (moveDown) {
            yChange -= 3;
        }
        
        if (moveRight) {
            xChange += 3;
        }
    }    
        
    private boolean isCollision(Entity a, Entity b) {
        if (a.x + a.width + xChange - 17 >= b.x && a.x + xChange + 17 <= b.x + b.width) {
            if (a.y + a.height + yChange - 45 >= b.y && a.y + yChange + 2 <= b.y + b.height) {
                return true;
            }
        }
        return false;
    }    
    
    private void modifyMovement() {
    // (X-17) (X+17) (Y-45) (Y+2) --- ham sprite offsets  
        
        if (hamEntity.x + hamEntity.width - 17 < wallEntity.x &&
            hamEntity.x + hamEntity.width + xChange - 17 >= wallEntity.x) {
            xChange = 0;
        }
        
        if (hamEntity.x + 17 > wallEntity.x + wallEntity.width &&
            hamEntity.x + xChange + 17 <= wallEntity.x + wallEntity.width) {
            xChange = 0;
        }
        
        if (hamEntity.y + hamEntity.height - 45 < wallEntity.y  &&
            hamEntity.y + hamEntity.height + yChange - 45 >= wallEntity.y) {
            yChange = 0;
        }
        
        if (hamEntity.y + 2 > wallEntity.y + wallEntity.height &&
            hamEntity.y + yChange + 2 <= wallEntity.y + wallEntity.height) {
            yChange = 0;
        }               
    }    
    */
    
    /* This cannot be a simple numerical addition/subtraction because I need to
        be able to check if keyDown is happening.
    
        If a keyDown EVENT adds +5 to movement, and collision subtracts 5 from
        movement, the net sum is 0.  But if collision ends the net sum is STILL
        0, even though the keyDown event has not been terminated.  That means
        the intent is for the character to still be at +5, and still be moving.
    */
    }
}

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

    3) stage.act() fires Enter/Exit events, such as those used for hovering over
        a button.  If you don't call stage.act(), a button will change its
        appearance when clicked, but not when hovering over.

    4) For resizing a component in a layout (button on a table on a stage) you
        need to set any TWO OR MORE of min/max/pref.  Setting just one lets the
        actor fill.  It might bear a little investigating into the source.

        Note: using .size() is an easy way to set all three at once.

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

/* deprecated
    // maybe it should be "if the current position doesn't work...don't do this"
    private void one(Entity a, Entity b) {
        if (a.x + xChange >= b.x && a.x + xChange <= b.x + b.width ||
            a.x + a.width + xChange >= b.x && a.x + a.width + xChange <= b.x + b.width) {
            
            if (a.y + a.height + yChange >= b.y && a.y + yChange <= b.y + b.height) {
                yChange = 0;
            }
        }
        
        if (a.y + yChange >= b.y && a.y + yChange <= b.y + b.height ||
            a.y + a.height + yChange >= b.y && a.y + a.height + yChange <= b.y + b.height) {
            
            if (a.x + a.width + xChange >= b.x && a.x + xChange <= b.x + b.width) {
                xChange = 0;
            }
        }
    }
*/