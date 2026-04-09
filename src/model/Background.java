package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Background {
    private String name;
    private String type;
    private int animationFrameNumber;
    private List<BufferedImage> sprites;

    public Background(String name, String type){
        this.name            = name;
        this.type            = type;
        animationFrameNumber = 0;
        sprites              = new ArrayList<>();
    }

    public String getName(){ return name; }
    public String getType(){ return type; }

    public void advanceFrame(){ animationFrameNumber = (animationFrameNumber + 1) % sprites.size(); }
    public List<BufferedImage> getSprites(){ return sprites; }
    public BufferedImage getCurrentAnimationFrame(){ return sprites.get(animationFrameNumber); }
}
