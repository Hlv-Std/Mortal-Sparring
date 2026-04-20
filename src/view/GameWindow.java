package view;

import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * TODO: Switch up panes
 * TODO: Handle buttons out of player movement (Esc, Input for name choosing)
 */
public class GameWindow extends JFrame implements KeyListener {
    private MainMenuPanel mainMenuPanel;
    private CharacterSelectPanel characterSelectPanel;
    private GamePanel gamePanel;
    private GameState gameState;

    public GameWindow(){
        super("Mortal Sparring");

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension d = toolkit.getScreenSize();

        setSize(GameState.WINDOWED_WIDTH, GameState.WINDOWED_HEIGHT);
        setResizable(false);
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gameState = GameState.getInstance();
        gameState.setWindowWidth(GameState.WINDOWED_WIDTH);
        gameState.setWindowHeight(GameState.WINDOWED_HEIGHT);
        gameState.setWindowFullscreen(this, true);

        loadMainMenuPanel();

        addKeyListener(this);
        setVisible(true);
        requestFocus();
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

        if (e.getKeyCode() == KeyEvent.VK_F2){
            gameState.setWindowFullscreen(this, !gameState.isFullscreen());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    static void main() throws InterruptedException {
        SwingUtilities.invokeLater(GameWindow::new);
        GameState.getInstance().waitForQuit();
        System.exit(0);
    }
}
