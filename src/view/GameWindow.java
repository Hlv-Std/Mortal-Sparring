package view;

import controller.PlayerLoader;
import model.Player;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.nio.file.Path;

public class GameWindow extends JFrame {
    private static final int DEF_WIDTH = 500;
    private static final int DEF_HEIGHT = 400;

    private GamePanel gamePanel;
    private Player player1;
    private Player player2;
    private PlayerLoader playerLoader;

    public GameWindow(){
        super("Mortal Sparring");
        setSize(DEF_WIDTH, DEF_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadLayout();

        setVisible(true);
    }

    private void initComponents(){
        // Preapre the player then add them to the game
        player1 = new Player("Fazz");
        player2 = new Player("Fazz");

        playerLoader = new PlayerLoader(player1, Path.of("./src/resources/fazz"));
        playerLoader.loadAnimations();
        playerLoader.setPlayer(player2);
        playerLoader.loadAnimations();

        player1.x = (double) getWidth()  / 2;
        player1.y = (double) getHeight() / 2;

        player2.x = (double) getWidth()  / 4;
        player2.y = (double) getHeight() / 4;


        gamePanel = new GamePanel(player1, player2, getWidth(), getHeight());
        setContentPane(gamePanel);
    }

    private void loadLayout(){}
}
