package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;

/*
 * TODO: Make attacks actually do something
 * TODO: Create an Hit animation state and character state
 * TODO: Create a Stunned animation state and character state
 * TODO: Implement an AttackTrajectory: Follows a series of relative positions in determinated instants
 */
public class Character {
    // Name
    private final String name;
    // Position
    public double x,y;
    public double velx, vely;
    public Rect hitbox;
    // Info
    private boolean isInAir, isFalling, isDucking, isInAction, isInvincible, isAlive;
    private boolean blinking;
    private int iframesCounter;
    private static final int RESET_IFRAMES = 10;
    private int jumps;
    private int health;
    private final boolean leftPlayer;
    // Animations
    private final HashMap<CharacterAnimationState, HashMap<Integer, BufferedImage>> animations;
    private CharacterAnimationState animationState;
    private int animationFrameNumber;
    private static final int RESET_ANIMATION_THRESHOLD = 5;
    public int animationThreshold = RESET_ANIMATION_THRESHOLD;
    // Moves
    private final HashMap<String, ActionListener> moveset;
    public Deque<Attack> runningAttacks;

    public Character(String name, boolean leftPlayer){
        super();
        this.name            = name;
        this.leftPlayer      = leftPlayer;
        x                    = 0;
        y                    = 0;
        velx                 = 0;
        vely                 = 0;
        hitbox               = new Rect(100, 100);
        isInAir              = false;
        isFalling            = false;
        isDucking            = false;
        isInAction           = false;
        isInvincible         = false;
        isAlive              = true;
        blinking             = false;
        iframesCounter       = 0;
        jumps                = 1;
        health               = 100;
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
        animations.put(CharacterAnimationState.Dead,     new HashMap<>());

        moveset.put("Jump", (_) -> {
            if (!isInAir){
                vely = -600;
                isInAir = true;
                jumps -= 1;
                if (!animationState.equals(CharacterAnimationState.Jumping))
                    changeAnimation(CharacterAnimationState.Jumping);
            } else if (jumps > 0){
                if (isFalling) {
                    vely = -500;
                } else {
                    vely -= 400;
                }
                jumps -= 1;
            }
        });
        moveset.put("Duck", (_) -> {
            if (isInAir){
                vely = 800;
                if (!animationState.equals(CharacterAnimationState.Falling))
                    changeAnimation(CharacterAnimationState.Falling);
            }else {
                isDucking = true;
                if (!animationState.equals(CharacterAnimationState.Ducking))
                    changeAnimation(CharacterAnimationState.Ducking);
            }
        });
        moveset.put("Left", (_) -> {
            velx = -300;
            if (!animationState.equals(CharacterAnimationState.Running) && !isInAir)
                changeAnimation(CharacterAnimationState.Running);
        });
        moveset.put("Right", (_) -> {
            velx = 300;
            if (!animationState.equals(CharacterAnimationState.Running) && !isInAir)
                changeAnimation(CharacterAnimationState.Running);
        });
        moveset.put("Punch", (_) -> {
            if (!isInAction){
                isInAction = true;
                // TODO: move in variables parameters that change between left player and right player
                if (leftPlayer){
                    runningAttacks.addLast(new Attack(
                            this,
                            hitbox.w - 10,
                            (hitbox.h/2) - 20,
                            new Rect(20, 20),
                            5,
                            16));
                }else {
                    runningAttacks.addLast(new Attack(
                            this,
                            -10,
                            (hitbox.h/2) - 20,
                            new Rect(20, 20),
                            5,
                            16));
                }
                if (!animationState.equals(CharacterAnimationState.Punching))
                    changeAnimation(CharacterAnimationState.Punching);
            }
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
        moveset.put("SDCombo", (_) -> {
            if (!isInAction){
                isInAction = true;
                // TODO: move in variables parameters that change between left player and right player
                if (leftPlayer){
                    runningAttacks.addLast(new Attack(
                            this,
                            hitbox.w/2,
                            (hitbox.h/2) - 20,
                            new Rect(80, 10),
                            10,
                            20));
                    velx = 8000;
                }else {
                    runningAttacks.addLast(new Attack(
                            this,
                            -hitbox.w/2,
                            (hitbox.h/2) - 20,
                            new Rect(80, 10),
                            10,
                            20));
                    velx = -8000;
                }
                if (!animationState.equals(CharacterAnimationState.Running) && !isInAir)
                    changeAnimation(CharacterAnimationState.Running);
            }
        });
    }

    public String getName(){ return name; }

    public boolean isInAir(){ return isInAir; }
    public boolean isFalling(){ return isFalling; }
    public boolean isDucking(){ return isDucking; }
    public boolean isInAction(){ return isInAction; }
    public boolean isAlive(){ return isAlive; }
    public boolean isInvincible(){ return isInvincible; }
    public boolean isBlinking(){ return blinking; }
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

        if (iframesCounter > 0){
            iframesCounter--;
            blinking = !blinking;
        }else {
            isInvincible = false;
            blinking = false;
        }
    }
    public void changeAnimation(CharacterAnimationState animationState){
        if (this.animationState.equals(animationState)) return;
        this.animationState = animationState;
        animationFrameNumber = 0;
    }
    public int getAnimationFrameNumber(){ return animationFrameNumber; }

    public Deque<Attack> getRunningAttacks(){ return runningAttacks; }
    public Attack getCurrentAttackHitbox(){
        if (runningAttacks.isEmpty())
            return null;
        return runningAttacks.getFirst();
    }
    public void executeAction(String action){
        moveset.get(action).actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, action));
    }
    public void damage(int damage){
        if (!isInvincible){
            health = Math.max(health - damage, 0);
            if (leftPlayer)
                velx = -400;
            else
                velx = 400;
            isInvincible = true;
            iframesCounter = RESET_IFRAMES;
        }
        if (health == 0)
            isAlive = false;
    }

    public void reset(){
        if (leftPlayer){
            x = (double) GameState.getInstance().getWindowWidth() / 3;
        }else {
            x = (double) GameState.getInstance().getWindowWidth()  / 1.5;
        }
        y = (double) GameState.getInstance().getWindowHeight() / 1.5;
        velx                 = 0;
        vely                 = 0;
        isInAir              = false;
        isFalling            = false;
        isDucking            = false;
        isInAction           = false;
        isInvincible         = false;
        isAlive              = true;
        iframesCounter       = 0;
        jumps                = 1;
        health               = 100;
        animationState       = CharacterAnimationState.Idle;
        animationFrameNumber = 0;
    }
}

