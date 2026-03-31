package view;

import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements KeyListener {
    private final int FPS = 60;
    private final double GRAVITY = 1f;
    private final double FRICTION = 0.5f;
    private final double MAX_FALL = 20f;
    private final double MAX_SPEED = 20f;
    private int BOUNDX;
    private int BOUNDY;
    private double GROUND;

    private Player player1;
    private Graphics2D g2;
    private Timer t;

    public GamePanel(Player player1, int width, int height){
        this.player1 = player1;
        BOUNDX = width;
        BOUNDY = HEIGHT;
        GROUND = (double) height - 20f;
        addKeyListener(this);
        final int delay = 10 / FPS;
        double dt = (double) 10 / FPS;
        t = new Timer(delay, (e) -> {
            if (!player1.isInAir())
                player1.velX *= FRICTION;
            player1.velX = Math.clamp(player1.velX, -MAX_SPEED, MAX_SPEED);

            player1.velY += GRAVITY * dt;
            player1.velY = Math.min(player1.velY, MAX_FALL);

            player1.x += player1.velX * dt;
            player1.y += player1.velY * dt;

            if (player1.y >= GROUND){
                player1.y = GROUND;
                player1.velY = 0;
                player1.setInAir(false);
            }
        });
        t.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (BufferedImage frame : player1.getFrames(player1.getAnimationState())){
            Image scaledFrame = frame.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            // Sprite
            g2.drawImage(
                    scaledFrame,
                    (int) (player1.x - (player1.hitbox.x / 2)),
                    (int) (player1.y - player1.hitbox.y),
                    null);
            g2.setColor(Color.BLUE);
            // Position
            g2.drawRect((int) player1.x, (int) player1.y, 1, 1);
            // Hitbox
            // g2.drawRect((int) (player1.x - player1.hitbox.x), (int) (player1.y - player1.hitbox.y), (int) player1.hitbox.x, (int) player1.hitbox.y);
            // Ground
            g2.drawLine(0, (int) GROUND, BOUNDX, (int) GROUND);
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_W -> player1.jump();
            case KeyEvent.VK_A -> player1.left();
            case KeyEvent.VK_D -> player1.right();
            case KeyEvent.VK_S -> player1.duck();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
