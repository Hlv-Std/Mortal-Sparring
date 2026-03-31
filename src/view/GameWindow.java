package view;

import controller.PlayerLoader;
import model.Player;

import javax.swing.*;
import java.nio.file.Path;

public class GameWindow extends JFrame {
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

        gamePanel = new GamePanel(player1, getWidth(), getHeight());
        setContentPane(gamePanel);
        addKeyListener(gamePanel);
    }

    private void loadLayout(){}
}
