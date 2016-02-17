package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SifterNode {
    Button nodeButton;
    int index;
    int priority;
    int xShift;
    int yShift;
    
    /* (1) There's a problem with index being reported and how I perceive it.
       (2) Index reports the top-left gold spot as "6", but when I call rotate()
           I specifically reference it as (0, 4) - so why is that happening?
    
           LibGdx uses top-left reference sometimes, and bottom-left others.
    */
    
    public SifterNode(Button a, int index) {
        nodeButton = a;       
        //a.addListener(new selectListener());
        this.index = index;
    }
}