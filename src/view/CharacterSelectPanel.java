package view;

import controller.BackgroundLoader;
import controller.PlayerLoader;
import model.GameState;

import javax.swing.*;
import java.nio.file.Path;

/*
 * TODO: UI for selecting the characters
 * TODO: Load the resources to the Players (Refactor code in GameWindow)
 * TODO: Show loading screen before loading assets (pre choice and post)
 */
public class CharacterSelectPanel extends JPanel {
    public CharacterSelectPanel(){
        super();
        setFocusable(true);
        requestFocusInWindow();

        GameState gameState = GameState.getInstance();
        gameState.setBg(BackgroundLoader.loadAnimations("CortileAI", Path.of("./src/resources/backgrounds")));

        // Preapre the player then add them to the game
        gameState.setPlayer1(
                PlayerLoader.loadAnimations(
                        "Helvetica",
                        "Pennacchi",
                        Path.of("./src/resources/pennacchi"),
                        false));

        gameState.setPlayer2(
                PlayerLoader.loadAnimations(
                        "Standard",
                        "Fazz",
                        Path.of("./src/resources/fazz"),
                        true));

        gameState.getPlayer1().getCharacter().x = (double) getWidth()  / 2;
        gameState.getPlayer1().getCharacter().y = (double) getHeight() / 2;

        gameState.getPlayer2().getCharacter().x = (double) getWidth()  / 1.5;
        gameState.getPlayer2().getCharacter().y = (double) getHeight() / 1.5;

    }
}
