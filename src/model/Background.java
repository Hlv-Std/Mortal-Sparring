package model;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Background {
    private final String name;
    private int animationFrameNumber;
    private Map<Integer, BufferedImage> frames;

    public Background(String name){
        this.name            = name;
        animationFrameNumber = 0;
        frames               = new HashMap<>();
    }

    public String getName(){ return name; }

    public void advanceFrame(){ animationFrameNumber = (animationFrameNumber + 1) % frames.size(); }
    public Map<Integer, BufferedImage> getFrames(){ return frames; }
    public BufferedImage getCurrentAnimationFrame(){ return frames.get(animationFrameNumber); }
}
