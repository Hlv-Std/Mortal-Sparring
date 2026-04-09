package view;

import controller.InputHandler;
import model.Background;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private final int WIDTH;
    private final int HEIGHT;
    private final double GROUND;

    private Graphics2D g2;
    private Background bg;
    private Player player1;
    private Player player2;

    private InputHandler input;

    public GamePanel(Player player1, Player player2, int width, int height, Background bg){
        this.player1 = player1;
        this.player2 = player2;
        this.WIDTH   = width;
        this.HEIGHT  = height;
        this.bg      = bg;
        GROUND       = (double) HEIGHT - 81;
        input        = new InputHandler(this);

        setFocusable(true);
        requestFocusInWindow();

        Timer t = getTimer(player1, player2);
        t.start();
    }

    private Timer getTimer(Player player1, Player player2) {
        double dt = (double) 1 / 60; // 60 FPS
        final double GRAVITY = 1500;
        final double FRICTION = 10;
        return new Timer(1000 / 60, (_) -> {
            // NOTE: Input -> Forces -> Friction -> Integrate -> Collide
            // Forces
            player1.velY += GRAVITY * dt;
            player2.velY += GRAVITY * dt;

            // Friction
            if (player1.isInAir()){
                player1.velX *= (1 - FRICTION/2.3 * dt);
            } else {
                player1.velX *= (1 - FRICTION * dt);
            }

            if (player2.isInAir()){
                player2.velX *= (1 - FRICTION/2.3 * dt);
            } else {
                player2.velX *= (1 - FRICTION * dt);
            }

            // Integrating
            player1.x += player1.velX * dt;
            player2.x += player2.velX * dt;
            player1.y += player1.velY * dt;
            player2.y += player2.velY * dt;

            // Collisions
            if (player1.x < 0){
                player1.x = 0;
                player1.velX = 0;
            } else if (player1.x + player1.hitbox.w >= WIDTH){
                player1.x = WIDTH - player1.hitbox.w;
                player1.velX = 0;
            }

            if (player1.y + player1.hitbox.h > GROUND){
                player1.velY = 0;
                player1.y = GROUND - player1.hitbox.h;
            }

            if (player2.x < 0){
                player2.x = 0;
                player2.velX = 0;
            } else if (player2.x + player2.hitbox.w >= WIDTH){
                player2.x = WIDTH - player2.hitbox.w;
                player2.velX = 0;
            }

            if (player2.y + player2.hitbox.h > GROUND){
                player2.velY = 0;
                player2.y = GROUND - player2.hitbox.h;
            }

            update();
            repaint();
        });
    }

    private void update(){
        // Position
        if (player1.y + player1.hitbox.h < GROUND) {
            player1.setFalling(player1.velY > 0);
            player1.setInAir(true);
        } else {
          player1.setInAir(false);
          player1.setFalling(false);
          player1.resetJumps();
        }

        if (player2.y + player2.hitbox.h < GROUND) {
            player2.setFalling(player2.velY > 0);
            player2.setInAir(true);
        } else {
            player2.setInAir(false);
            player2.setFalling(false);
            player2.resetJumps();
        }

        // Input handling
        if (input.isHeld(KeyEvent.VK_W)) player1.jump();
        if (input.isHeld(KeyEvent.VK_A)) player1.left();
        if (input.isHeld(KeyEvent.VK_S)) player1.duck();
        if (input.isHeld(KeyEvent.VK_D)) player1.right();
        if (input.isHeld(KeyEvent.VK_X)) assert false : "Punch not implemented";
        if (input.isHeld(KeyEvent.VK_C)) assert false : "Kick not implemented";
        if (input.isHeld(KeyEvent.VK_V)) assert false : "Special not implemented";

        if (input.isHeld(KeyEvent.VK_U)) player2.jump();
        if (input.isHeld(KeyEvent.VK_H)) player2.left();
        if (input.isHeld(KeyEvent.VK_J)) player2.duck();
        if (input.isHeld(KeyEvent.VK_K)) player2.right();
        if (input.isHeld(KeyEvent.VK_M)) assert false : "Punch not implemented";
        if (input.isHeld(KeyEvent.VK_COMMA)) assert false : "Kick not implemented";
        if (input.isHeld(KeyEvent.VK_PERIOD)) assert false : "Special not implemented";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        {
            bg.advanceFrame();
            g2.drawImage(
                    bg.getCurrentAnimationFrame(),
                    0,
                    0,
                    null
            );
        }

        // Player 1
        {
            player1.advanceFrame();
            // Sprite
            g2.drawImage(
                    player1.getCurrentFrame(),
                    (int) player1.x,
                    (int) player1.y,
                    null);
            g2.setColor(Color.BLUE);
            // Position
            // g2.drawRect((int) player1.x, (int) player1.y, 1, 1);
            // Hitbox
            // g2.drawRect(
            //         (int) player1.x,
            //         (int) player1.y,
            //         (int) player1.hitbox.w,
            //         (int) player1.hitbox.h
            // );
        }

        // Player 2
        {
            player2.advanceFrame();
            // Sprite
            g2.drawImage(
                    player2.getCurrentFrame(),
                    (int) player2.x,
                    (int) player2.y,
                    null);
            g2.setColor(Color.BLUE);
            // Position
            // g2.drawRect((int) player2.x, (int) player2.y, 1, 1);
            // Hitbox
            // g2.drawRect(
            //         (int) player2.x,
            //         (int) player2.y,
            //         (int) player2.hitbox.w,
            //         (int) player2.hitbox.h
            // );
        }

        // Ground
        // g2.drawLine(0, (int) GROUND, WIDTH, (int) GROUND);
    }
}
