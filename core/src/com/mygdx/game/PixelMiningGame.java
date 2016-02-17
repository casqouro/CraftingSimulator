package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PixelMiningGame {

    private SpriteBatch batch;
    private Camera camera;
    private Viewport viewport;    
    private final Stage gameStage;
    private final int boardSize; 
    Image image;
    Texture texture;
    Pixmap pixmap;
    
    public PixelMiningGame(SpriteBatch batch, int buttonBoardSize) {
        this.batch = batch;
        camera = new OrthographicCamera();
        viewport = new ScalingViewport(Scaling.none, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameStage = new Stage(viewport, batch);
        boardSize = buttonBoardSize;  
        
        pixmap = new Pixmap(
                 new FileHandle("..\\assets\\squares\\blue_active.png"));  
        texture = new Texture(pixmap);
        image = new Image(texture);
        
        image.setSize(200, 200);
        image.addListener(new pickaxListener());
        
        gameStage.addActor(image);
    } 
    
    private void updateTexture() {
        texture = new Texture(pixmap); 
        image = new Image(texture);
    }
    
    public void drawTexture() {
        batch.draw(texture, 0, 0, 200, 200);
    }
    
    private void nukeImage(float xF, float yF) {
        // a pixmap's 0,0 is the fucking upper left corner?
        
        // It's only doing any kind of modification when it gives -1.0
        // and it gives -1.0 in very weird circumstances
        int x = (int) (xF + 0.5);
        int y = (int) (yF + 0.5);
        pixmap.setColor(100, 50, 30, 100);
        pixmap.fillRectangle(x, y, 25, 25);
        //pixmap.fillCircle(0, 0, 30);
        //pixmap.fill();
        
        texture = new Texture(pixmap);
        
        image.setDrawable(new TextureRegionDrawable(
                          new TextureRegion(texture)));
        //updateTexture();
    }
                
    private class pickaxListener extends ClickListener {    
        @Override
        public void clicked(InputEvent event, float x, float y) {
            System.out.println(this.getTouchDownX() + " " + this.getTouchDownY());
            nukeImage(this.getTouchDownX(), this.getTouchDownY());
        }
    }
    
    public Stage getStage() {
        return gameStage;
    }    
}