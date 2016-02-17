package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class ButtonCompanion {
    Button newButton;
    int index;
    int health = 4;
    
    public ButtonCompanion(Button a, int b) {
        newButton = a;
        index = b;
    }
}