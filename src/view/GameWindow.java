package view;

import controller.BackgroundLoader;
import controller.PlayerLoader;
import model.Background;
import model.GameState;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Path;

/*
 * TODO: Switch up panes
 * TODO: Handle buttons out of player movement (Esc, Input for name choosing)
 */
public class GameWindow extends JFrame implements KeyListener {
    private static final int DEF_WIDTH = 701;
    private static final int DEF_HEIGHT = 401;

    private MainMenuPanel mainMenuPanel;
    private CharacterSelectPanel characterSelectPanel;
    private GamePanel gamePanel;
    private GameState gameState;

    public GameWindow(){
        super("Mortal Sparring");
        setSize(DEF_WIDTH, DEF_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gameState = GameState.getInstance();
        gameState.setWindowWidth(getWidth());
        gameState.setWindowWidth(getHeight());

        GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();
        device.setFullScreenWindow(this);
        
        loadMainMenuPanel();

        addKeyListener(this);
        setVisible(true);
    }

    private void loadMainMenuPanel(){
        mainMenuPanel = new MainMenuPanel();
        setContentPane(mainMenuPanel);
        revalidate();
        repaint();
    }

    private void loadCharacterSelectPanel(){
        characterSelectPanel = new CharacterSelectPanel();
        setContentPane(characterSelectPanel);
        revalidate();
        repaint();
    }

    private void loadGamePanel(){
        gamePanel = new GamePanel();
        setContentPane(gamePanel);
        revalidate();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        Container currentPane = getContentPane();
        if (currentPane.equals(mainMenuPanel)){
            System.out.println("Passing to character select");
            loadCharacterSelectPanel();
        }else if (currentPane.equals(characterSelectPanel)){
            System.out.println("Passing to game");
            loadGamePanel();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
