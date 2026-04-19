package view;

import controller.InputHandler;
import model.*;
import model.Character;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Deque;


/*
 * TODO: Pause Screen (Independent from who): Freeze dt
 */
public class GamePanel extends JPanel {
    private final int WIDTH;
    private final int HEIGHT;
    private final double GROUND;

    private Graphics2D g2;
    private final Background bg;
    private final Player player1;
    private final Player player2;
    private final Character character1;
    private final Character character2;
    private double lastPosX = 0;
    private double lastPosY = 0;

    private final InputHandler input;

    public GamePanel(Player player1, Player player2, int width, int height, Background bg){
        this.player1 = player1;
        this.player2 = player2;
        this.character1 = player1.getCharacter();
        this.character2 = player2.getCharacter();
        this.WIDTH   = width;
        this.HEIGHT  = height;
        this.bg      = bg;
        GROUND       = (double) HEIGHT - 81;
        input        = new InputHandler(this, player1, player2);

        setFocusable(true);
        requestFocusInWindow();

        Timer t = getTimer();
        t.start();
        input.start();
    }

    private Timer getTimer() {
        final int FPS = 60;
        final double dt = (double) 1 / FPS;
        final double GRAVITY = 1500;
        final double FRICTION = 10;
        final double MAX_VELOCITY = 900;
        return new Timer(1000 / FPS, (_) -> {
            // NOTE: Input -> Forces -> Friction -> Integrate -> Collide
            // Forces
            character1.velY += GRAVITY * dt;
            character2.velY += GRAVITY * dt;

            // Friction
            if (character1.isInAir()){
                character1.velX *= (1 - FRICTION/2.3 * dt);
            } else {
                character1.velX *= (1 - FRICTION * dt);
            }
            if (Math.abs(character1.velX) < 10) character1.velX = 0;

            if (character2.isInAir()){
                character2.velX *= (1 - FRICTION/2.3 * dt);
            } else {
                character2.velX *= (1 - FRICTION * dt);
            }
            if (Math.abs(character2.velX) < 10) character2.velX = 0;

            if (character1.x > character2.x){
                character1.velX = 0;
                character1.x--;
            }

            if (character2.x < character1.x){
                character2.velX = 0;
                character2.x++;
            }

            // Integrating
            character1.x += character1.velX * dt;
            character1.y += character1.velY * dt;
            character2.x += character2.velX * dt;
            character2.y += character2.velY * dt;

            // Collisions
            if (character1.x < 0){
                character1.x = 0;
                character1.velX = 0;
            } else if (character1.x + character1.hitbox.w >= WIDTH){
                character1.x = WIDTH - character1.hitbox.w;
                character1.velX = 0;
            }
            if (character1.y + character1.hitbox.h > GROUND){
                character1.velY = 0;
                character1.y = GROUND - character1.hitbox.h;
            }

            if (character2.x < 0){
                character2.x = 0;
                character2.velX = 0;
            } else if (character2.x + character2.hitbox.w >= WIDTH){
                character2.x = WIDTH - character2.hitbox.w;
                character2.velX = 0;
            }
            if (character2.y + character2.hitbox.h > GROUND){
                character2.velY = 0;
                character2.y = GROUND - character2.hitbox.h;
            }

            update();
            repaint();
        });
    }

    private void update(){
        // Position
        if (character1.y + character1.hitbox.h < GROUND) {
            character1.setFalling(character1.velY > 0);
            character1.setInAir(true);
        } else {
            character1.setInAir(false);
            character1.setFalling(false);
            character1.resetJumps();
            character1.setDucking(false);
        }

        if (character2.y + character2.hitbox.h < GROUND) {
            character2.setFalling(character2.velY > 0);
            character2.setInAir(true);
        } else {
            character2.setInAir(false);
            character2.setFalling(false);
            character2.resetJumps();
            character2.setDucking(false);
        }

        // Input handling
        if (input.isHeld(KeyEvent.VK_W)) character1.executeAction("Jump");
        if (input.isHeld(KeyEvent.VK_A)) character1.executeAction("Left");
        if (input.isHeld(KeyEvent.VK_S)) character1.executeAction("Duck");
        if (input.isHeld(KeyEvent.VK_D)) character1.executeAction("Right");
        if (input.isHeld(KeyEvent.VK_X)) character1.executeAction("Punch");
        if (input.isHeld(KeyEvent.VK_C)) assert false : "Kick not implemented";
        if (input.isHeld(KeyEvent.VK_V)) assert false : "Special not implemented";
        // DEBUG: Remove this later
        if (player1.getInputProcesser().combo(KeyEvent.VK_S, KeyEvent.VK_D)) character1.executeAction("SDCombo");

        if (input.isHeld(KeyEvent.VK_U)) character2.executeAction("Jump");
        if (input.isHeld(KeyEvent.VK_H)) character2.executeAction("Left");
        if (input.isHeld(KeyEvent.VK_J)) character2.executeAction("Duck");
        if (input.isHeld(KeyEvent.VK_K)) character2.executeAction("Right");
        if (input.isHeld(KeyEvent.VK_M)) character2.executeAction("Punch");
        if (input.isHeld(KeyEvent.VK_COMMA)) assert false : "Kick not implemented";
        if (input.isHeld(KeyEvent.VK_PERIOD)) assert false : "Special not implemented";
        // DEBUG: Remove this later
        if (player2.getInputProcesser().combo(KeyEvent.VK_J, KeyEvent.VK_K)) character2.executeAction("SDCombo");

        // Attack TTL
        Deque<Attack> runningAttacks = character1.getRunningAttacks();
        if (!runningAttacks.isEmpty()){
            Attack hb = runningAttacks.getFirst();
            if (hb.isAlive()){
                hb.decrease();
            }else {
                character1.setInAction(false);
                runningAttacks.removeFirst();
            }
        }

        runningAttacks = character2.getRunningAttacks();
        if (!runningAttacks.isEmpty()){
            Attack hb = runningAttacks.getFirst();
            if (hb.isAlive()){
                hb.decrease();
            }else {
                character2.setInAction(false);
                runningAttacks.removeFirst();
            }
        }

        // Reset Idle animation
        if (!character1.isInAir()    &&
                !character1.isDucking()  &&
                !character1.isInAction() &&
                character1.velX == 0     &&
                character1.velY == 0     &&
                !character1.getAnimationState().equals(CharacterAnimationState.Idle))
            character1.changeAnimation(CharacterAnimationState.Idle);

        if (!character2.isInAir()    &&
                !character2.isDucking()  &&
                !character2.isInAction() &&
                character2.velX == 0     &&
                character2.velY == 0     &&
                !character2.getAnimationState().equals(CharacterAnimationState.Idle))
            character2.changeAnimation(CharacterAnimationState.Idle);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        {
            bg.advanceFrame();
            BufferedImage sprite = bg.getCurrentAnimationFrame();
            if (sprite != null){
                g2.drawImage(
                        sprite,
                        0,
                        0,
                        null
                );
            }
        }

        // Player 1
        {
            character1.advanceFrame();
            // Sprite
            BufferedImage sprite = character1.getCurrentFrame();
            if (sprite != null){
                g2.drawImage(
                        sprite,
                        (int) character1.x,
                        (int) character1.y,
                        null);
            }
            g2.setColor(Color.BLUE);
            // Position
            g2.drawRect((int) character1.x, (int) character1.y, 1, 1);
            // Hitbox
            g2.drawRect(
                    (int) character1.x,
                    (int) character1.y,
                    (int) character1.hitbox.w,
                    (int) character1.hitbox.h
            );

            g2.setColor(Color.ORANGE);
            if (character1.isInAction()){
                Attack hb = character1.getCurrentAttackHitbox();
                if (hb != null){
                    g2.drawRect(
                            (int) hb.x,
                            (int) hb.y,
                            (int) hb.hitbox.w,
                            (int) hb.hitbox.h
                    );
                }
            }
        }

        // Player 2
        {
            character2.advanceFrame();
            // Sprite
            BufferedImage sprite = character2.getCurrentFrame();
            if (sprite != null){
                g2.drawImage(
                        sprite,
                        (int) character2.x,
                        (int) character2.y,
                        null);
            }
            g2.setColor(Color.BLUE);
            // Position
            g2.drawRect((int) character2.x, (int) character2.y, 1, 1);
            // Hitbox
            g2.drawRect(
                    (int) character2.x,
                    (int) character2.y,
                    (int) character2.hitbox.w,
                    (int) character2.hitbox.h
            );

            g2.setColor(Color.ORANGE);
            if (character2.isInAction()){
                Attack hb = character2.getCurrentAttackHitbox();
                if (hb != null){
                    g2.drawRect(
                            (int) hb.x,
                            (int) hb.y,
                            (int) hb.hitbox.w,
                            (int) hb.hitbox.h
                    );
                }
            }
        }

        // Ground
        // g2.drawLine(0, (int) GROUND, WIDTH, (int) GROUND);
    }
}
