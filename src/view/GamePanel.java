package view;

import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements KeyListener {
    private final double MAX_SPEED = 80;
    private final int WIDTH;
    private final int HEIGHT;
    private final double GROUND;

    private Graphics2D g2;
    private Player player1;
    private Player player2;

    public GamePanel(Player player1, int width, int height){
        this.player1 = player1;
        this.WIDTH = width;
        this.HEIGHT = height;
        GROUND = (double) HEIGHT - 20;

        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        // TODO: We simplify the logic by using basic movement. We do not need full physics
        double dt = (double) 1 / 60; // 60 FPS
        Timer t = new Timer(1000 / 60, (e) -> {
            player1.velY += 40 * dt;
            player1.x += player1.velX * dt;
            player1.y += player1.velY * dt;
            if (player1.y >= GROUND){
                player1.y = GROUND;
                player1.velY = 0;
                player1.setInAir(false);
            }
            updatePlayers();
        });
        t.start();
        player1.loadKeyCombo();
    }

    private void updatePlayers(){
        // Chech position
        if (player1.y == GROUND){
            player1.setInAir(false);
            player1.setFalling(false);
            player1.resetJumps();
        } else if (player1.isInAir() && player1.velY > 0){
            player1.setFalling(true);
        } else {
            player1.setInAir(true);
            player1.setFalling(false);
        }

        // TODO: Player 2
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
            g2.drawRect(
                    (int) (player1.x - (player1.hitbox.x / 2)),
                    (int) (player1.y - player1.hitbox.y),
                    (int) player1.hitbox.x,
                    (int) player1.hitbox.y);
            // Ground
            g2.drawLine(0, (int) GROUND, WIDTH, (int) GROUND);
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        player1.addKeyCombo(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
