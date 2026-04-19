package view;

import model.Player;

import javax.swing.*;

/*
 * TODO: UI for selecting the characters
 * TODO: Load the resources to the Players (Refactor code in GameWindow)
 * TODO: Show loading screen before loading assets (pre choice and post)
 */
public class CharacterSelectPanel extends JPanel {
    private Player player1;
    private Player player2;

    public CharacterSelectPanel(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
    }
}
