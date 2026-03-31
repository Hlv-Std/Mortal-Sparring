package view;

import controller.PlayerLoader;
import model.Player;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.nio.file.Path;

public class GameWindow extends JFrame implements KeyListener {
    private static final int DEF_WIDTH = 500;
    private static final int DEF_HEIGHT = 400;

    private GamePanel gamePanel;
    private Player player1;
    private PlayerLoader player1Loader;

    public GameWindow(){
        super("Mortal Sparring");
        setSize(DEF_WIDTH, DEF_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(this);

        initComponents();
        loadLayout();

        setVisible(true);
    }

    private void initComponents(){
        // Preapre the player then add it to the game
        player1 = new Player("Fazz");
        player1Loader = new PlayerLoader(player1, Path.of("./src/resources/fazz"));
        player1Loader.loadAnimations();
        player1.x = (double) getWidth() /2;
        player1.y = (double) getHeight() /2;

        gamePanel = new GamePanel(player1);
        setContentPane(gamePanel);
    }

    private void loadLayout(){

    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_Q -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
