package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player {
    private String name;
    public double x,y;
    public double velX, velY;
    private boolean isInAir, isFalling, isDucking;
    public Vector2 hitbox;

    private String animationState;
    private HashMap<String, List<BufferedImage>> animations;

    public Player(String name){
        super();
        this.name = name;
        velX = 0;
        velY = 0;
        animationState = PlayerAnimationState.IDLE;
        animations = new HashMap<>();
        // Set up player animations
        animations.put(PlayerAnimationState.IDLE, new ArrayList<>());
    }

    public String getName(){ return name; }
    public List<BufferedImage> getFrames(String animationName){
        return animations.get(animationName);
    }
    public String getAnimationState(){ return animationState; }

    public void jump(){
        if (!isInAir){
            velY = -20f;
            isInAir = true;
        }
    }
    public void duck(){
        if (isInAir){
            velY = 40f;
        }
    }
    public void left(){ velX = -20f; }
    public void right(){ velX = 20f; }

    public boolean isInAir(){ return isInAir; }
    public boolean isFalling(){ return isFalling; }
    public boolean isDucking(){ return isDucking; }

    public void setInAir(boolean isInAir) { this.isInAir = isInAir; }
    public void setFalling(boolean isFalling) { this.isFalling = isFalling; }
    public void setDucking(boolean isDucking) { this.isDucking = isDucking; }
}

interface PlayerAnimationState {
    String IDLE = "Idle";
    String JUMPING = "Jumping";
    String FALLING = "Falling";
    String PUNCH = "Punch";
}
