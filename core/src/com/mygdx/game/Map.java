package com.mygdx.game;

import java.util.Random;

public class Map {
    int mapSizeX;
    int mapSizeY;
    int[][] mapBoard;
    int resourceMaximum;
    int resourceHarvested;
    
    // figure out what the fuck this warning about overridable calls is about
    Map(int x, int y) {
        mapSizeX = x;
        mapSizeY = y;
        mapBoard = new int[mapSizeX][mapSizeY];
        resourceMaximum = (mapSizeX * mapSizeY) / 10;
        resourceHarvested = 0;
        
        generateResources();
    }
    
    final public void generateResources() {
        Random ran = new Random();
        int location;
        int mapSize = mapSizeX * mapSizeY;
        int quantityToGenerate = resourceMaximum - resourceHarvested;
        
        while (quantityToGenerate > 0) {
            location = ran.nextInt(mapSize);
            
            if (mapBoard[location / mapSizeX][location % mapSizeY] == 0) {
                mapBoard[location / mapSizeX][location % mapSizeY] = ran.nextInt(2) + 2;
                quantityToGenerate--;
            }
        }
        
        resourceHarvested = 0;
    }
    
    public void setHarvested(int a) {
        resourceHarvested += a;
    }
    
    public boolean needToGenerateResource() {
        return (double) resourceHarvested / resourceMaximum >= 0.5;
    }
}
