package view;

import controller.PlayerLoader;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private Player player1;
    private Graphics2D g2;

    public GamePanel(Player player1){
        this.player1 = player1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (BufferedImage frame : player1.getFrames("Idle")){
            Image scaledFrame = frame.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            g2.drawImage(scaledFrame, (int) player1.x, (int) player1.y, null);
        }
        repaint();
    }
}
