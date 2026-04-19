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
        bg = BackgroundLoader.loadAnimations("Cortile", Path.of("./src/resources/backgrounds"));

        // Preapre the player then add them to the game
        player1 = new Player("Helvetica");
        player1.setCharacter(PlayerLoader.loadAnimations("Pennacchi", Path.of("./src/resources/pennacchi"), false));

        player2 = new Player("Standard");
        player2.setCharacter(PlayerLoader.loadAnimations("Fazz", Path.of("./src/resources/fazz"), true));

        player1.getCharacter().x = (double) getWidth()  / 2;
        player1.getCharacter().y = (double) getHeight() / 2;

        player2.getCharacter().x = (double) getWidth()  / 1.5;
        player2.getCharacter().y = (double) getHeight() / 1.5;

        gamePanel = new GamePanel(player1, player2, getWidth(), getHeight(), bg);
        setContentPane(gamePanel);
    }

    private void loadLayout(){}
}
