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
    public Vector2 hitbox;

    private boolean isInAir, isFalling, isDucking;
    private int jumps;

    private String animationState;
    private HashMap<String, List<BufferedImage>> animations;
    private HashSet<Integer> keysHeld;

    public Player(String name){
        super();
        this.name = name;
        x = 0;
        y = 0;
        velX = 0;
        velY = 0;
        hitbox = new Vector2(100, 100);
        isInAir = false;
        isFalling = false;
        isDucking = false;
        jumps = 2;
        animationState = PlayerAnimationState.IDLE;
        animations = new HashMap<>();
        animations.put(PlayerAnimationState.IDLE, new ArrayList<>());
        animations.put(PlayerAnimationState.FALLING, new ArrayList<>());
        animations.put(PlayerAnimationState.JUMPING, new ArrayList<>());
        animations.put(PlayerAnimationState.PUNCHING, new ArrayList<>());
        keysHeld = new HashSet<>();
    }

    public String getName(){ return name; }
    public List<BufferedImage> getFrames(String animationName){ return animations.get(animationName); }
    public String getAnimationState(){ return animationState; }

    public void jump(){
        if (!isInAir){
            velY = -80;
            isInAir = true;
            jumps -= 1;
        } else if (jumps > 0){
            if (isFalling) {
                velY = -50;
            } else {
                velY -= 40;
            }
            jumps -= 1;
        }
    }
    public void duck(){
        if (isInAir){
            velY = 80;
        }
    }
    public void left(){ velX = -20; }
    public void right(){ velX = 20; }

    public boolean isInAir(){ return isInAir; }
    public boolean isFalling(){ return isFalling; }
    public boolean isDucking(){ return isDucking; }

    public void setInAir(boolean isInAir) { this.isInAir = isInAir; }
    public void setFalling(boolean isFalling) { this.isFalling = isFalling; }
    public void setDucking(boolean isDucking) { this.isDucking = isDucking; }
    public void resetJumps() { jumps = 2; }

    public void addKeyCombo(int e){ keysHeld.add(e); }
    public void loadKeyCombo(){
        final int FPS = 60;
        double dt = (double) 1000 / FPS;
        Timer t = new Timer((int) (dt), (e) -> {
            if (keysHeld.contains(KeyEvent.VK_W)){
                jump();
                keysHeld.remove(KeyEvent.VK_W);
                return;
            }else if (keysHeld.contains(KeyEvent.VK_A)){
                left();
                keysHeld.remove(KeyEvent.VK_A);
                return;
            }else if (keysHeld.contains(KeyEvent.VK_S)){
                duck();
                keysHeld.remove(KeyEvent.VK_S);
                return;
            }else if (keysHeld.contains(KeyEvent.VK_D)){
                right();
                keysHeld.remove(KeyEvent.VK_D);
                return;
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
