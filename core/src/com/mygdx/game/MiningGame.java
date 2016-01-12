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
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Random;

public class MiningGame {
    private Camera camera;
    private Viewport viewport;    
    private final Stage gameStage;
    private final Image stageBackdrop;    
    private ButtonCompanion[] buttonBoard;    
    private final int prefW = 30;
    private final int prefH = 30;  
    private final int boardSize;
    private final int depth = 3;
    
/* DEPTH:
   Use a darker mask on each successive layer to portray depth?
   I could attach the mask to the background of each button on a level.
    
   LOCATION:
   The display location of the game is currently hard-coded by adding an offset
   when drawing the tiles.
*/
    public MiningGame(SpriteBatch batch, int buttonBoardSize) {
        camera = new OrthographicCamera();
        viewport = new ScalingViewport(Scaling.none, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameStage = new Stage(viewport, batch);
        boardSize = buttonBoardSize;
        buttonBoard = new ButtonCompanion[depth * (boardSize * boardSize)];
        
        // use this as a non-clickable button or something at the end of the array?
        stageBackdrop = new Image(
                        new Texture(
                        new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\squares\\blue.png")));
        stageBackdrop.setSize(prefW * buttonBoardSize, prefH * buttonBoardSize);       
        gameStage.addActor(stageBackdrop);
        
        for (int a = 0; a < buttonBoard.length; a++) {               
            buttonBoard[a] = createCompanion(a);  
        }            
    }
    
    private ButtonCompanion createCompanion(int index) {
        ButtonCompanion a = new ButtonCompanion(createOre(), index);
        a.newButton.addListener(new actionClickListener(a));
        a.newButton.setWidth(prefW);
        a.newButton.setHeight(prefW);
        
        /*  The original code:
            a.newButton.setPosition((index % boardSize) * prefW, (index / boardSize) * prefH); 
        
            The above draws a single layer.  The updated code draws multiple
            layers, one on top of the other.   
        */
        
        // Do I care that the end of the array is drawing on top of the front?
        int layerIndex = index % (boardSize * boardSize);
        int xLoc = (layerIndex % boardSize * prefW) + 100;
        int yLoc = (layerIndex / boardSize * prefH) + 100;
        
        a.newButton.setPosition(xLoc, yLoc);
        
        gameStage.addActor(a.newButton);
        
        return a;
    }
    
    private Button createOre() {
        Button a;
        
        a = new Button(new ButtonStyle(null, null, null));
        
        String color = "";
        String color_active = "";
        
        Random rand = new Random();
        int num = rand.nextInt(3);
        
        if (num == 0) {
            color = "blue";
            color_active = "blue_active";
        }
        
        if (num == 1) {
            color = "red";
            color_active = "red_active";
        }
        
        if (num == 2) {
            color = "green";
            color_active = "green_active";
        }
                                        
        a.getStyle().up = new TextureRegionDrawable(
                            new TextureRegion(
                            new Texture(
                            new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\squares\\" + color + ".png"))));    
        a.getStyle().down = new TextureRegionDrawable(
                            new TextureRegion(
                            new Texture(
                            new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\squares\\" + color_active + ".png"))));
        
        return a;
    }
        
    public Stage getStage() {
        return gameStage;
    }  
    
    private class actionClickListener extends ClickListener {
        ButtonCompanion c;
        
        public actionClickListener(ButtonCompanion reference) {
            c = reference;
        }
        
        @Override
        public void clicked(InputEvent event, float x, float y) {  
            System.out.println(c.index);
            c.newButton.remove();
            
            //buttonBoard[c.index] = createCompanion(c.index);
        }
    }    
}

// actionListener goes in MiningGame so I can directly reference the array
// 