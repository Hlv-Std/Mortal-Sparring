package model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;

public class Player {
    private String name;
    public double x,y;
    public double velX, velY;
    public Rect hitbox;
    public Deque<AttackHitbox> runningAttacks;
    private boolean isInAir, isFalling, isDucking;
    private int jumps;
    private String animationState;
    private int animationFrameNumber;
    private HashMap<String, List<BufferedImage>> animations;
    private HashMap<String, ActionListener> moveset;

    public Player(String name){
        super();
        this.name            = name;
        x                    = 0;
        y                    = 0;
        velX                 = 0;
        velY                 = 0;
        hitbox               = new Rect(100, 100);
        isInAir              = false;
        isFalling            = false;
        isDucking            = false;
        jumps                = 1;
        animationState       = PlayerAnimationState.IDLE;
        animationFrameNumber = 0;
        runningAttacks       = new ArrayDeque<>();
        animations           = new HashMap<>();
        moveset              = new HashMap<>();

        animations.put(PlayerAnimationState.IDLE, new ArrayList<>());
        animations.put(PlayerAnimationState.FALLING, new ArrayList<>());
        animations.put(PlayerAnimationState.JUMPING, new ArrayList<>());
        animations.put(PlayerAnimationState.PUNCHING, new ArrayList<>());

        moveset.put("Jump", (_) -> {
            if (!isInAir){
                velY = -600;
                isInAir = true;
                jumps -= 1;
            } else if (jumps > 0){
                if (isFalling) {
                    velY = -500;
                } else {
                    velY -= 400;
                }
                jumps -= 1;
            }
        });
        moveset.put("Duck", (_) -> {
            if (isInAir){
                velY = 800;
            }
        });
        moveset.put("Left", (_) -> velX = -300);
        moveset.put("Right", (_) -> velX = 300);
        moveset.put("Punch", (_) -> {
            runningAttacks.addLast(new AttackHitbox(x, y, new Rect(20, 20), 10, 8));
        });
        moveset.put("Kick", (_) -> {

        });
        moveset.put("Special", (_) -> {

        });
        // !DEBUG
        moveset.put("SDCombo", (_) -> velX = 800);
    }

    public String getName(){ return name; }
    public List<BufferedImage> getFrames(String animationName){ return animations.get(animationName); }
    public String getAnimationState(){ return animationState; }

    public void executeAction(String action){
        moveset.get(action).actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, action));
    }

    public boolean isInAir(){ return isInAir; }
    public boolean isFalling(){ return isFalling; }
    public boolean isDucking(){ return isDucking; }

    public void setInAir(boolean isInAir) { this.isInAir = isInAir; }
    public void setFalling(boolean isFalling) { this.isFalling = isFalling; }
    public void setDucking(boolean isDucking) { this.isDucking = isDucking; }
    public void resetJumps() { jumps = 1; }

    public void advanceFrame(){ animationFrameNumber = (animationFrameNumber++) % animations.get(animationState).size(); };
    public BufferedImage getCurrentFrame(){
        return getFrames(animationState).get(animationFrameNumber);
    }

    public Deque<AttackHitbox> getRunningAttacks(){ return runningAttacks; }
    public AttackHitbox getCurrentAttackHitbox(){
        if (runningAttacks.isEmpty())
            return null;
        return runningAttacks.getFirst(); }
}

interface PlayerAnimationState {
    String IDLE = "Idle";
    String JUMPING = "Jumping";
    String FALLING = "Falling";
    String PUNCHING = "Punching";
}
