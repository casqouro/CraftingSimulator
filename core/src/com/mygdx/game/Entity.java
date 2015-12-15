package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Entity {    
    private TextureRegion appearance;
    int x;
    int y;
    int width;
    int height;
    
    // Should the data members be private, with accessor methods like:
        // setX/setY, for changes?
        // getBounds, for collision?
    
    public Entity(TextureRegion a, int xLoc, int yLoc) {
        appearance = a;
        
        x = xLoc;
        y = yLoc;
        
        width = appearance.getRegionWidth();
        height = appearance.getRegionHeight();
    }    
    
    public Entity(TextureRegion a, int xLoc, int yLoc, int scaleWidth, int scaleHeight) {
        appearance = a;
        
        x = xLoc;
        y = yLoc;
        
        width = scaleWidth;
        height = scaleHeight;
    }
    
    public void drawEntity(SpriteBatch toDraw) {
        toDraw.draw(appearance, x, y, width, height);
    }
    
    // This method doesn't seem healthy.  Something about object references
    // without cleaning up...
    public void setAppearance(TextureRegion newAppearance) {
        appearance = newAppearance;
    }
}