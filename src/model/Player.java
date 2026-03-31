package model;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

public class Player {
    private String name;
    private HashMap<String, List<BufferedImage>> animations;

    public Player(String name){
        this.name = name;
        animations = new HashMap<>();
    }
}
