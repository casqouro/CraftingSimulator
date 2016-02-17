package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SiftingGame {
    private Camera camera;
    private Viewport viewport;    
    private final Stage gameStage;    
    SpriteBatch batch;
    Image stageBackdrop;
    Texture texture;
    SifterNode[][] nodeSet;
    
    private final int prefW = 30;
    private final int prefH = 30;    
    
    /* If I rewrite my input.txt so that it matches the physical structure
       instead of the data structure I will be able to use the array structure
       and straight through it when calling update().  Right now I have SIX
       hard-coded, separate statements to go through each layer.    
    */

    public SiftingGame(SpriteBatch batch) throws IOException {
        camera = new OrthographicCamera();
        viewport = new ScalingViewport(Scaling.none, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameStage = new Stage(viewport, batch);        
        this.batch = batch;
        
        stageBackdrop = 
            new Image(
            new Texture(
            new FileHandle("..\\assets\\squares\\green.png")));
        stageBackdrop.setSize(330, 330);       
        //gameStage.addActor(stageBackdrop);        
        
        texture = 
            new Texture(
            new FileHandle("..\\assets\\squares\\blue_active.png"));
        
        nodeSetup();
    }
    
    private void nodeSetup() throws IOException {
        // must be a way to pull this out so it's not in my fucking game
        BufferedReader br = new BufferedReader(new FileReader("..\\assets\\SiftingGameAssets\\layout_multi.txt"));        
        String line = br.readLine();
        String[] token;
        
        nodeSet = new SifterNode[11][11];
        while (line != null) {   
            token = line.split(" ");
                        
            int index = Integer.parseInt(token[0]);
            int priority = Integer.parseInt(token[1]);
            
            if (priority > -1) { 
                int x = Integer.parseInt(token[2]);                
                int y = Integer.parseInt(token[3]);
                int row = index / 11;
                int col = index % 11;                
                
                Button button = new Button(new Button.ButtonStyle(null, null, null));  
                selectListener listener = new selectListener(button);
                button.addListener(listener);
                button.getStyle().up = 
                    new TextureRegionDrawable(
                    new TextureRegion(
                    new Texture(
                    new FileHandle("..\\assets\\SiftingGameAssets\\" + priority + "priority.png"))));
                button.getStyle().down = 
                    new TextureRegionDrawable(
                    new TextureRegion(
                    new Texture(
                    new FileHandle("..\\assets\\squares\\red.png"))));                 
                button.setWidth(prefW);
                button.setHeight(prefH);

                SifterNode node = new SifterNode(button, index);
                nodeSet[row][col] = node;                 
                nodeSet[row][col].nodeButton.setPosition(row * prefW, col * prefH);
                nodeSet[row][col].priority = priority;
                nodeSet[row][col].xShift = x;
                nodeSet[row][col].yShift = y;
                        
                gameStage.addActor(node.nodeButton);                
            }  
            
            line = br.readLine();            
        }
        
        hardCodedTest();
    }
    
    private void hardCodedTest() {
        nodeSet[5][5].nodeButton.getStyle().up = 
                new TextureRegionDrawable(
                new TextureRegion(
                new Texture(
                new FileHandle("..\\assets\\SiftingGameAssets\\0test.png"))));  
        
        nodeSet[5][4].nodeButton.getStyle().up = 
                new TextureRegionDrawable(
                new TextureRegion(
                new Texture(
                new FileHandle("..\\assets\\SiftingGameAssets\\1test.png")))); 
        
        nodeSet[5][3].nodeButton.getStyle().up = 
                new TextureRegionDrawable(
                new TextureRegion(
                new Texture(
                new FileHandle("..\\assets\\SiftingGameAssets\\2test.png")))); 
        
        nodeSet[6][3].nodeButton.getStyle().up = 
                new TextureRegionDrawable(
                new TextureRegion(
                new Texture(
                new FileHandle("..\\assets\\SiftingGameAssets\\3test.png")))); 
        
        nodeSet[5][2].nodeButton.getStyle().up = 
                new TextureRegionDrawable(
                new TextureRegion(
                new Texture(
                new FileHandle("..\\assets\\SiftingGameAssets\\4test.png")))); 
        
        nodeSet[5][1].nodeButton.getStyle().up = 
                new TextureRegionDrawable(
                new TextureRegion(
                new Texture(
                new FileHandle("..\\assets\\SiftingGameAssets\\5test.png"))));
        
        nodeSet[5][0].nodeButton.getStyle().up = 
                new TextureRegionDrawable(
                new TextureRegion(
                new Texture(
                new FileHandle("..\\assets\\SiftingGameAssets\\6test.png"))));    
        
        rotate();                
    }
    
    private void rotate() {    
        int zero = 1;
        int first = 4;
        int second = 8;
        int third = 12;
        int fourth = 16;
        int fifth = 20;
        int sixth = 20;  
        
        int sentinel;
        int currentRow;
        int currentCol;               
        
        Button swap;
        Button save;
        
        // could I do this as a recursive call, returning the displaced button?
        // it would be a good place to demonstrate ability to use recursion
      
        sentinel = 0;
        currentRow = 5;
        currentCol = 6;              
        save = nodeSet[4][5].nodeButton;
        while (sentinel < first) {
            swap = nodeSet[currentRow][currentCol].nodeButton; // why isn't this working?
            nodeSet[currentRow][currentCol].nodeButton = save;
            nodeSet[currentRow][currentCol].nodeButton.setPosition(currentRow * prefW, currentCol * prefH);
            save = swap;
            
            int row = currentRow;
            int col = currentCol;
            
            currentRow = currentRow + nodeSet[row][col].xShift; // this modification affects the next line of code!
            currentCol = currentCol + nodeSet[row][col].yShift;
            sentinel++;                 
        }    
                
        sentinel = 0;
        currentRow = 5;
        currentCol = 7;              
        save = nodeSet[4][6].nodeButton;
        while (sentinel < second) {
            swap = nodeSet[currentRow][currentCol].nodeButton; // why isn't this working?
            nodeSet[currentRow][currentCol].nodeButton = save;
            nodeSet[currentRow][currentCol].nodeButton.setPosition(currentRow * prefW, currentCol * prefH);
            save = swap;
            
            int row = currentRow;
            int col = currentCol;
            
            currentRow = currentRow + nodeSet[row][col].xShift; // this modification affects the next line of code!
            currentCol = currentCol + nodeSet[row][col].yShift;
            sentinel++;                 
        }         
              
        sentinel = 0;
        currentRow = 6;
        currentCol = 7;              
        save = nodeSet[4][7].nodeButton;
        while (sentinel < third) {
            swap = nodeSet[currentRow][currentCol].nodeButton; // why isn't this working?
            nodeSet[currentRow][currentCol].nodeButton = save;
            nodeSet[currentRow][currentCol].nodeButton.setPosition(currentRow * prefW, currentCol * prefH);
            save = swap;
            
            int row = currentRow;
            int col = currentCol;
            
            currentRow = currentRow + nodeSet[row][col].xShift; // this modification affects the next line of code!
            currentCol = currentCol + nodeSet[row][col].yShift;
            sentinel++;                 
        } 
                      
        sentinel = 0;
        currentRow = 5;
        currentCol = 8;              
        save = nodeSet[4][9].nodeButton;
        while (sentinel < fourth) {
            swap = nodeSet[currentRow][currentCol].nodeButton; // why isn't this working?
            nodeSet[currentRow][currentCol].nodeButton = save;
            nodeSet[currentRow][currentCol].nodeButton.setPosition(currentRow * prefW, currentCol * prefH);
            save = swap;
            
            int row = currentRow;
            int col = currentCol;
            
            currentRow = currentRow + nodeSet[row][col].xShift; // this modification affects the next line of code!
            currentCol = currentCol + nodeSet[row][col].yShift;
            sentinel++;                 
        }  
        
        sentinel = 0;
        currentRow = 5;
        currentCol = 9;              
        save = nodeSet[4][8].nodeButton;
        while (sentinel < fifth) {
            swap = nodeSet[currentRow][currentCol].nodeButton; // why isn't this working?
            nodeSet[currentRow][currentCol].nodeButton = save;
            nodeSet[currentRow][currentCol].nodeButton.setPosition(currentRow * prefW, currentCol * prefH);
            save = swap;
            
            int row = currentRow;
            int col = currentCol;
            
            currentRow = currentRow + nodeSet[row][col].xShift; // this modification affects the next line of code!
            currentCol = currentCol + nodeSet[row][col].yShift;
            sentinel++;                 
        }          
        
        sentinel = 0;
        currentRow = 0;
        currentCol = 4;              
        save = nodeSet[1][3].nodeButton;
        while (sentinel < sixth) {
            swap = nodeSet[currentRow][currentCol].nodeButton; // why isn't this working?
            nodeSet[currentRow][currentCol].nodeButton = save;
            nodeSet[currentRow][currentCol].nodeButton.setPosition(currentRow * prefW, currentCol * prefH);
            save = swap;
            
            int row = currentRow;
            int col = currentCol;
            
            currentRow = currentRow + nodeSet[row][col].xShift; // this modification affects the next line of code!
            currentCol = currentCol + nodeSet[row][col].yShift;
            sentinel++;                 
        }           
    }
    
    public Stage getStage() {
        return gameStage;
    } 
    
    /* Very interesting.  If the listener is added from here, it deletes the
       button.
    
       If it's added from the SifterNode, deletion doesn't work correctly after
       rotating (which includes swapping buttons).
    
       There must be something going on with object references?
    */    
    private class selectListener extends ClickListener {    
        Button button;
        
        public selectListener(Button button) {
            this.button = button;
        }
        
        @Override
        public void clicked(InputEvent event, float x, float y) {
            button.remove();
        }
    }     
}

/*  Originally the selectListener was located inside of the SifterNode class.
    
    However, after calling rotate() a call to button.remove() would not work
    on the intended button.  It seems like the selectListener was, for some
    reason, attached to the node and not the button, despite being added to the
    button!  It would be interesting to investigate why, but for now this works.
*/