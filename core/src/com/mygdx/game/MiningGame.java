package com.mygdx.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MiningGame {
    private final Stage gameStage;
    private final Table table;
    
    public MiningGame() {
        gameStage = new Stage();
        table = new Table();
        
        table.setWidth(100);
        table.setHeight(100);
        table.setPosition(200, 200);
        
        table.add(createOre()).prefWidth(100).prefHeight(100); 
        table.add(createOre()).prefWidth(100).prefHeight(100); 
        table.row();
        table.add(createOre()).prefWidth(100).prefHeight(100); 
        table.add(createOre()).prefWidth(100).prefHeight(100);
        
        table.setBackground(new TextureRegionDrawable(
                            new TextureRegion(
                            new Texture(
                            new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\ham\\blackBackground.png")))));        
        
        gameStage.addActor(table);
    }
    
    private Button createOre() {
        Button a;
        
        a = new Button(new ButtonStyle(null, null, null));
                                
        a.getStyle().up = new TextureRegionDrawable(
                            new TextureRegion(
                            new Texture(
                            new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\squares\\blue.png"))));    
        a.getStyle().down = new TextureRegionDrawable(
                            new TextureRegion(
                            new Texture(
                            new FileHandle("C:\\Users\\Matthew\\Desktop\\CraftingSimulator\\assets\\squares\\blue_active.png"))));
        
        return a;
    }
    
    public Stage getStage() {
        return gameStage;
    }
}
