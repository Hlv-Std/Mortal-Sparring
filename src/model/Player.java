package model;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Player {
    private String name;
    public double x,y;
    public double velX, velY;
    private boolean isInAir, isFalling, isDucking;
    public Vector2 hitbox;

    private String animationState;
    private HashMap<String, List<BufferedImage>> animations;
    private Array<Integer> keysHeld;

    public Player(String name){
        super();
        this.name = name;
        velX = 0;
        velY = 0;
        hitbox = new Vector2(100, 100);
        animationState = PlayerAnimationState.IDLE;
        animations = new HashMap<>();
        // Set up player animations
        animations.put(PlayerAnimationState.IDLE, new ArrayList<>());
        animations.put(PlayerAnimationState.FALLING, new ArrayList<>());
        animations.put(PlayerAnimationState.JUMPING, new ArrayList<>());
        animations.put(PlayerAnimationState.PUNCHING, new ArrayList<>());
        keysHeld = new Array<>(4);
    }

    public String getName(){ return name; }
    public List<BufferedImage> getFrames(String animationName){
        return animations.get(animationName);
    }
    public String getAnimationState(){ return animationState; }

    public void jump(){
        if (!isInAir){
            velY = -7f;
            isInAir = true;
        }
    }
    public void duck(){
        if (isInAir){
            velY = 7f;
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

    public void addKeyCombo(int e){ keysHeld.add(e); }
    public void loadKeyCombo(){
        // TODO: Recalculate these values
        final int FPS = 60;
        final int delay = 1000 / FPS;
        double dt = (double) 10 / FPS;
        Timer t = new Timer(delay, (e) -> {
            while(true){
                if (keysHeld.contains(KeyEvent.VK_W)){
                    jump();
                    keysHeld.remove(KeyEvent.VK_W);
                    continue;
                }else if (keysHeld.contains(KeyEvent.VK_A)){
                    left();
                    keysHeld.remove(KeyEvent.VK_A);
                    continue;
                }else if (keysHeld.contains(KeyEvent.VK_S)){
                    duck();
                    keysHeld.remove(KeyEvent.VK_S);
                    continue;
                }else if (keysHeld.contains(KeyEvent.VK_D)){
                    right();
                    keysHeld.remove(KeyEvent.VK_D);
                    continue;
                }
                break;
            }

            // TODO: Use Threads instead of timers
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            keysHeld.clear();
        });
        t.start();
    }
}

interface PlayerAnimationState {
    String IDLE = "Idle";
    String JUMPING = "Jumping";
    String FALLING = "Falling";
    String PUNCHING = "Punching";
}
