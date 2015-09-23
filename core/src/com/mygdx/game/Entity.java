package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

public class Entity {
    public Color myColor;
    public int locX;
    public int locY;
    
    public void Entity(Color c, int x, int y) {
        myColor = c;
        locX = x;
        locY = y;
    }
}
