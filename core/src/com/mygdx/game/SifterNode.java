package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class SifterNode {
    Button nodeButton;
    int index;
    int priority;
    int xShift;
    int yShift;
    
    public SifterNode(Button a, int index) {
        nodeButton = a;
        this.index = index;
    }
}
