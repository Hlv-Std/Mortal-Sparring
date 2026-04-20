package view;

import controller.BackgroundLoader;
import controller.PlayerLoader;
import model.GameState;

import javax.swing.*;
import java.nio.file.Path;

/*
 * TODO: UI for selecting the characters
 * TODO: Load the resources to the Players (Refactor code in GameWindow)
 * TODO: Show loading screen before loading assets (pre choice and post, if needed)
 */
public class CharacterSelectPanel extends JPanel {
    public CharacterSelectPanel(){
        super();

        GameState gameState = GameState.getInstance();
        gameState.setBg(BackgroundLoader.loadAnimations("CortileAI"));

        // Preapre the player then add them to the game
        gameState.setPlayer1(
                PlayerLoader.loadAnimations(
                        "Helvetica",
                        "Pennacchi",
                        false));

        gameState.setPlayer2(
                PlayerLoader.loadAnimations(
                        "Standard",
                        "Fazz",
                        true));

        gameState.getPlayer1().getCharacter().x = (double) gameState.getWindowWidth() / 3;
        gameState.getPlayer1().getCharacter().y = (double) gameState.getWindowHeight() / 1.5;

        gameState.getPlayer2().getCharacter().x = (double) gameState.getWindowWidth()  / 1.5;
        gameState.getPlayer2().getCharacter().y = (double) gameState.getWindowHeight() / 1.5;

    }
}
