package model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;

/*
 * TODO: Rewrite whole player
 * InputHandler must be per Giocatore and not for Player or GamePanel
 * Cleanup messy code and functions
 * Assert for player animations
 * Flip animations for player2
 */

public class Character {
    // Name
    private String name;
    // Position
    public double x,y;
    public double velX, velY;
    // Info
    public Rect hitbox;
    private boolean isInAir, isFalling, isDucking, isInAction;
    private int jumps;
    // Animations
    private HashMap<CharacterAnimationState, HashMap<Integer, BufferedImage>> animations;
    private CharacterAnimationState animationState;
    private int animationFrameNumber;
    private static final int RESET_ANIMATION_THRESHOLD = 5;
    public int animationThreshold = RESET_ANIMATION_THRESHOLD;
    // Moves
    private HashMap<String, ActionListener> moveset;
    public Deque<AttackHitbox> runningAttacks;

    public Character(String name){
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
        isInAction           = false;
        jumps                = 1;
        animations           = new HashMap<>();
        animationState       = CharacterAnimationState.Idle;
        animationFrameNumber = 0;
        moveset              = new HashMap<>();
        runningAttacks       = new ArrayDeque<>();

        animations.put(CharacterAnimationState.Idle,     new HashMap<>());
        animations.put(CharacterAnimationState.Running,  new HashMap<>());
        animations.put(CharacterAnimationState.Falling,  new HashMap<>());
        animations.put(CharacterAnimationState.Ducking,  new HashMap<>());
        animations.put(CharacterAnimationState.Jumping,  new HashMap<>());
        animations.put(CharacterAnimationState.Kicking,  new HashMap<>());
        animations.put(CharacterAnimationState.Special1, new HashMap<>());
        animations.put(CharacterAnimationState.Punching, new HashMap<>());

        moveset.put("Jump", (_) -> {
            if (!isInAir){
                velY = -600;
                isInAir = true;
                jumps -= 1;
                if (!animationState.equals(CharacterAnimationState.Jumping))
                    changeAnimation(CharacterAnimationState.Jumping);
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
                if (!animationState.equals(CharacterAnimationState.Falling))
                    changeAnimation(CharacterAnimationState.Falling);
            }else {
                isDucking = true;
                if (!animationState.equals(CharacterAnimationState.Ducking))
                    changeAnimation(CharacterAnimationState.Ducking);
            }
        });
        moveset.put("Left", (_) -> {
            velX = -300;
            if (!animationState.equals(CharacterAnimationState.Running) && !isInAir)
                changeAnimation(CharacterAnimationState.Running);
        });
        moveset.put("Right", (_) -> {
            velX = 300;
            if (!animationState.equals(CharacterAnimationState.Running) && !isInAir)
                changeAnimation(CharacterAnimationState.Running);
        });
        moveset.put("Punch", (_) -> {
            if (!isInAction){
                isInAction = true;
                runningAttacks.addLast(new AttackHitbox(x, y, new Rect(20, 20), 10, 8));
            }
            if (!animationState.equals(CharacterAnimationState.Punching))
                changeAnimation(CharacterAnimationState.Punching);
        });
        moveset.put("Kick", (_) -> {
            if (!animationState.equals(CharacterAnimationState.Kicking))
                changeAnimation(CharacterAnimationState.Kicking);
        });
        moveset.put("Special", (_) -> {
            if (!animationState.equals(CharacterAnimationState.Special1))
                changeAnimation(CharacterAnimationState.Special1);
        });
        // DEBUG: Add test combo
        moveset.put("SDCombo", (_) -> velX = 800);
    }

    public String getName(){ return name; }

    public boolean isInAir(){ return isInAir; }
    public boolean isDucking(){ return isDucking; }
    public boolean isInAction(){ return isInAction; }
    public void setInAir(boolean isInAir) { this.isInAir = isInAir; }
    public void setFalling(boolean isFalling) { this.isFalling = isFalling; }
    public void setDucking(boolean isDucking) { this.isDucking = isDucking; }
    public void setInAction(boolean isInAction){ this.isInAction = isInAction; }
    public void resetJumps() { jumps = 1; }

    public Map<Integer, BufferedImage> getFrames(CharacterAnimationState animationName){ return animations.get(animationName); }
    public CharacterAnimationState getAnimationState(){ return animationState; }
    public BufferedImage getCurrentFrame(){
        if (animationFrameNumber >= animations.get(animationState).size())
            return null;
        return getFrames(animationState).get(animationFrameNumber);
    }
    public void advanceFrame(){
        if (animationThreshold > 0){
            animationThreshold--;
        }else {
            int animationListSize = animations.get(animationState).size();
            if (animationListSize > 0){
                animationFrameNumber = (++animationFrameNumber) % animationListSize;
            }
            animationThreshold = RESET_ANIMATION_THRESHOLD;
        }
    }
    public void changeAnimation(CharacterAnimationState animationState){
        this.animationState = animationState;
        animationFrameNumber = 0;
    }
    public int getAnimationFrameNumber(){ return animationFrameNumber; }

    public Deque<AttackHitbox> getRunningAttacks(){ return runningAttacks; }
    public AttackHitbox getCurrentAttackHitbox(){
        if (runningAttacks.isEmpty())
            return null;
        return runningAttacks.getFirst();
    }
    public void executeAction(String action){
        moveset.get(action).actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, action));
    }
}

