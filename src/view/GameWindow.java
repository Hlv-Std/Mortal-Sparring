package view;

import controller.BackgroundLoader;
import controller.PlayerLoader;
import model.Background;
import model.Player;

import javax.swing.*;
import java.nio.file.Path;

public class GameWindow extends JFrame {
    private static final int DEF_WIDTH = 701;
    private static final int DEF_HEIGHT = 401;

    private GamePanel gamePanel;
    private Background bg;
    private BackgroundLoader bgLoader;
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
        bg = new Background("Bagno", "Super");
        bgLoader = new BackgroundLoader(bg, Path.of("./src/resources/backgrounds"));
        bgLoader.loadAnimations();

        // Preapre the player then add them to the game
        player1 = new Player("Fazz");
        player2 = new Player("Fazz");

        playerLoader = new PlayerLoader(player1, Path.of("./src/resources/fazz"));
        playerLoader.loadAnimations();
        playerLoader.setPlayer(player2, Path.of("./src/resources/fazz2"));
        playerLoader.loadAnimations();

        player1.x = (double) getWidth()  / 2;
        player1.y = (double) getHeight() / 2;

        player2.x = (double) getWidth()  / 4;
        player2.y = (double) getHeight() / 4;


        gamePanel = new GamePanel(player1, player2, getWidth(), getHeight(), bg);
        setContentPane(gamePanel);
    }

    private void loadLayout(){}
}
