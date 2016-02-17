package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameBoard {
    private final SpriteBatch batch;
    private int fieldSizeX = 10;
    private int fieldSizeY = 10;
    private final int height = Gdx.graphics.getHeight();
    private final int width = Gdx.graphics.getWidth();
    private final int heightSpacing = height / fieldSizeY;
    private final int widthSpacing = width / fieldSizeX; 
    
    private final int mapSizeX;
    private final int mapSizeY;
    int[][] mapBoard;
    int resourceMaximum;
    int resourceHarvested;    
    
    TextureRegion ham;
    TextureRegion wall;
    TextureRegion ore;
    TextureRegion grass;
    TextureRegion apple;
    TextureRegion cheese;
    TextureRegion mask;
    
    TextureAtlas hamAtlas;
    Animation hamAnim;
    
    TextureAtlas hamWalkUpAtlas;
    Animation hamWalkUpAnim; 
    
    Entity[] colliders;
    Entity hamEntity;
    Entity wallEntity;
    Entity oreEntity;    
    
    public GameBoard(SpriteBatch a, int x, int y) {
        batch = a;
        
        fieldSizeX = 10;
        fieldSizeY = 10;
        mapSizeX = 10;
        mapSizeY = 10;
        mapBoard = new int[mapSizeX][mapSizeY];        
        
        ham = new TextureRegion(new Texture(new FileHandle("..\\assets\\ham\\ham (1).png")));
        wall = new TextureRegion(new Texture(new FileHandle("..\\assets\\ham\\wall.png")));
        ore = new TextureRegion(new Texture(new FileHandle("..\\assets\\ore\\orangeOre.png")));
        grass = new TextureRegion(new Texture(new FileHandle("..\\assets\\ham\\grass.png")));
        apple = new TextureRegion(new Texture(new FileHandle("..\\assets\\ham\\apple.png")));
        cheese = new TextureRegion(new Texture(new FileHandle("..\\assets\\ham\\cheese.png")));
        mask = new TextureRegion(new Texture(new FileHandle("..\\assets\\ham\\mask_640_480.png")));        
        
        hamAtlas = new TextureAtlas(Gdx.files.internal("..\\assets\\ham\\hamswalk.atlas"));
        hamAnim = new Animation(1 / 6f, hamAtlas.getRegions());
        hamAnim.setPlayMode(Animation.PlayMode.LOOP);
        
        hamWalkUpAtlas = new TextureAtlas(Gdx.files.internal("..\\assets\\ham\\hamwalkup.atlas"));
        hamWalkUpAnim = new Animation(1 / 4f, hamWalkUpAtlas.getRegions());
        hamWalkUpAnim.setPlayMode(Animation.PlayMode.LOOP);  
        
        colliders = new Entity[3]; // wouldn't this need to be dynamic?
        
        hamEntity = new Entity(ham, 200, 200, 50, 50);
        wallEntity = new Entity(wall, 100, 100, 50, 50);
        oreEntity = new Entity(ore, 300, 300, 50, 50);
        colliders[0] = hamEntity;
        colliders[1] = wallEntity;
        colliders[2] = oreEntity;        
    }
    
    public void drawGameBoard() {
        drawBackground();
        drawBounds();  
        drawOre(); // not working?
    }
    
    private void drawResources() {
        int justify = (widthSpacing - apple.getRegionWidth()) / 2;
        for (int a = 0; a < fieldSizeX; a++) {
            for (int b = 0; b < fieldSizeY; b++) {
                if (mapBoard[a][b] == 2) {
                    batch.draw(apple, (a * widthSpacing) + justify, b * heightSpacing);
                }
                if (mapBoard[a][b] == 3) {
                    batch.draw(cheese, (a * widthSpacing) + justify, b * heightSpacing);
                }
            }
        }
    }
    
    private void drawOre() {
        batch.draw(oreEntity.getAppearance(), 300, 300);
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
    
    private void drawMask() {
        // Pre-sized masks are simplest but take up space (1.2mbs for one).
        batch.draw(mask, 0, 0);
    }      
}
