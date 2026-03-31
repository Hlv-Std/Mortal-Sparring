package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player extends Vector2{
    private String name;
    private HashMap<String, List<BufferedImage>> animations;

    public Player(String name){
        super();
        this.name = name;
        animations = new HashMap<>();
        // Set up player animations
        animations.put("Idle", new ArrayList<>());
    }

    public String getName(){ return name; }

    public List<BufferedImage> getFrames(String animationName){
        return animations.get(animationName);
    }
}
