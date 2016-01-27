package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    SifterNode[] nodes;    
    SifterNode[][] nodeSet;
    
    private final int prefW = 30;
    private final int prefH = 30;    
    
    // The button's style, determined by "priority" (right now), won't change when the button is moved.
    // Each node draws the button it currently holds at its x/y, so it'll have to call setPosition
    // When the game is updated each node will pass its button
    
    public SiftingGame(SpriteBatch batch) throws IOException {
        camera = new OrthographicCamera();
        viewport = new ScalingViewport(Scaling.none, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameStage = new Stage(viewport, batch);        
        this.batch = batch;
        
        stageBackdrop = 
            new Image(
            new Texture(
            new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\squares\\green.png")));
        stageBackdrop.setSize(330, 330);       
        //gameStage.addActor(stageBackdrop);        
        
        texture = 
            new Texture(
            new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\squares\\blue_active.png"));
        
        nodeSetup();
    }
    
    private void nodeSetup() throws IOException {
        // must be a way to pull this out so it's not in my fucking game
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\SiftingGameAssets\\layout.txt"));
        String line = br.readLine();
        String[] token;
        
        nodes = new SifterNode[81];
        while (line != null) {   
            token = line.split(" ");
            
            int index = Integer.parseInt(token[0]);
            int x = Integer.parseInt(token[1]);
            int y = Integer.parseInt(token[2]);
            int priority = Integer.parseInt(token[3]);
            int xShift = Integer.parseInt(token[4]);
            int yShift = Integer.parseInt(token[5]);
            
            Button button = new Button(new Button.ButtonStyle(null, null, null));
            
            button.getStyle().up = 
                new TextureRegionDrawable(
                new TextureRegion(
                new Texture(
                new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\SiftingGameAssets\\" + priority + "priority.png"))));
            
            button.getStyle().down = 
                new TextureRegionDrawable(
                new TextureRegion(
                new Texture(
                new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\squares\\red.png"))));            

            SifterNode node = new SifterNode(button, index);
            button.setWidth(prefW);
            button.setHeight(prefH);
            node.nodeButton.setPosition(x * prefW, y * prefH);
            node.priority = priority;
            nodes[index] = node;
                        
            gameStage.addActor(node.nodeButton);
            line = br.readLine();
        }
    }
    
    private void update() {
        Button transfer;
        
        for (int a = 0; a < nodes.length; a++) {
            transfer = nodes[a].nodeButton;
        }
    }
    
    public Stage getStage() {
        return gameStage;
    } 
}