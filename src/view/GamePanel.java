package view;

import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private final int WIDTH;
    private final int HEIGHT;
    private final double GROUND;

    private Graphics2D g2;
    private Player player1;
    private Player player2;

    public GamePanel(Player player1, Player player2, int width, int height){
        this.player1 = player1;
        this.player2 = player2;
        this.WIDTH   = width;
        this.HEIGHT  = height;
        GROUND       = (double) HEIGHT - 20;

        // TODO: Finish implementing InputMap/ActionMap
        InputMap inputMap = getInputMap();
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("w"), "player1_jump");
        inputMap.put(KeyStroke.getKeyStroke("a"), "player1_left");
        inputMap.put(KeyStroke.getKeyStroke("s"), "player1_duck");
        inputMap.put(KeyStroke.getKeyStroke("d"), "player1_right");

        inputMap.put(KeyStroke.getKeyStroke("i"), "player2_jump");
        inputMap.put(KeyStroke.getKeyStroke("j"), "player2_left");
        inputMap.put(KeyStroke.getKeyStroke("k"), "player2_duck");
        inputMap.put(KeyStroke.getKeyStroke("l"), "player2_right");

        actionMap.put("player1_jump", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.jump();
            }
        });
        actionMap.put("player1_left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.left();
            }
        });
        actionMap.put("player1_duck", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.duck();
            }
        });
        actionMap.put("player1_right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.right();
            }
        });

        actionMap.put("player2_jump", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2.jump();
            }
        });
        actionMap.put("player2_left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2.left();
            }
        });
        actionMap.put("player2_duck", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2.duck();
            }
        });
        actionMap.put("player2_right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2.right();
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        Timer t = getTimer(player1, player2);
        t.start();
    }

    private Timer getTimer(Player player1, Player player2) {
        double dt = (double) 1 / 60; // 60 FPS
        final double GRAVITY = 1500;
        final double FRICTION = 10;
        Timer t = new Timer(1000 / 60, (_) -> {
            // NOTE: Input -> Forces -> Friction -> Integrate -> Collide
            // Forces
            player1.velY += GRAVITY * dt;
            player2.velY += GRAVITY * dt;

            // Friction
            if (player1.isInAir()){
                player1.velX *= (1 - FRICTION/2.3 * dt);
            }else {
                player1.velX *= (1 - FRICTION * dt);
            }

            if (player2.isInAir()){
                player2.velX *= (1 - FRICTION/2.3 * dt);
            }else {
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
            }else if (player1.x + player1.hitbox.w >= WIDTH){
                player1.x = WIDTH - player1.hitbox.w;
                player1.velX = 0;
            }

            if (player1.y + player1.hitbox.h > GROUND){
                player1.velY = 0;
                player1.y = GROUND - player1.hitbox.h;
                player1.setInAir(false);
                player1.setFalling(false);
            }

            if (player2.x < 0){
                player2.x = 0;
                player2.velX = 0;
            }else if (player2.x + player2.hitbox.w >= WIDTH){
                player2.x = WIDTH - player2.hitbox.w;
                player2.velX = 0;
            }

            if (player2.y + player2.hitbox.h > GROUND){
                player2.velY = 0;
                player2.y = GROUND - player2.hitbox.h;
                player2.setInAir(false);
                player2.setFalling(false);
            }

            update();
            repaint();
        });
        return t;
    }

    private void update(){
        // Position
        if (!player1.isInAir() && !player1.isFalling()){
            player1.resetJumps();
        } else if (player1.isInAir() && player1.velY > 0){
            player1.setFalling(true);
        } else {
            player1.setInAir(true);
            player1.setFalling(false);
        }

        if (!player2.isInAir() && !player2.isFalling()){
            player2.resetJumps();
        } else if (player2.isInAir() && player2.velY > 0){
            player2.setFalling(true);
        } else {
            player2.setInAir(true);
            player2.setFalling(false);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (BufferedImage frame : player1.getFrames(player1.getAnimationState())){
            // Sprite
            g2.drawImage(
                    frame,
                    (int) player1.x,
                    (int) player1.y ,
                    null);
        }
        g2.setColor(Color.BLUE);
        // Position
        g2.drawRect((int) player1.x, (int) player1.y, 1, 1);
        // Hitbox
        g2.drawRect(
                (int) player1.x,
                (int) player1.y,
                (int) player1.hitbox.w,
                (int) player1.hitbox.h
        );

        for (BufferedImage frame : player2.getFrames(player2.getAnimationState())){
            // Sprite
            g2.drawImage(
                    frame,
                    (int) player2.x,
                    (int) player2.y ,
                    null);
        }
        g2.setColor(Color.BLUE);
        // Position
        g2.drawRect((int) player2.x, (int) player2.y, 1, 1);
        // Hitbox
        g2.drawRect(
                (int) player2.x,
                (int) player2.y,
                (int) player2.hitbox.w,
                (int) player2.hitbox.h
        );
        // Ground
        g2.drawLine(0, (int) GROUND, WIDTH, (int) GROUND);
    }
}
