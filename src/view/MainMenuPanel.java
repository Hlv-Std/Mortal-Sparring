package view;

import javax.swing.*;
import java.awt.*;

/*
 * TODO: Show brief video at the start
 * TODO: Change panel upon pressing a button
 */
public class MainMenuPanel extends JPanel {
    public MainMenuPanel(){
        super(new GridBagLayout());

        JLabel gameName = new JLabel("Mortal-Sparring");
        gameName.setSize(400, 400);
        var cGameName = new GridBagConstraints();
        {
            cGameName.gridx = 1;
            cGameName.gridy = 0;
        }

        JLabel inputText = new JLabel("Premi un tasto per iniziare");
        var cInputText = new GridBagConstraints();
        {
            cInputText.gridx = 1;
            cInputText.gridy = 1;
        }
        add(gameName, cGameName);
        add(inputText, cInputText);
    }
}
