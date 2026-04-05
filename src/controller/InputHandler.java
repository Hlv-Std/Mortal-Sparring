package controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

// TODO: add threshold to allow combo and fix double jump
public class InputHandler {
    private final Set<Integer> keysHeld = new HashSet<>();

    public InputHandler(JComponent component){
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        for(int keyCode : getKeys()){
            String pressed = "pressed_" + keyCode;
            String released = "released_" + keyCode;

            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), pressed);
            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), released);

            actionMap.put(pressed, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { keysHeld.add(keyCode); }
            });
            actionMap.put(released, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { keysHeld.remove(keyCode); }
            });
        }

    }

    public boolean isHeld(int keyCode){
        return keysHeld.contains(keyCode);
    }

    private int[] getKeys(){
        return new int[]{
        //        Player 1       Player 2
                KeyEvent.VK_W, KeyEvent.VK_U,
                KeyEvent.VK_A, KeyEvent.VK_H,
                KeyEvent.VK_S, KeyEvent.VK_J,
                KeyEvent.VK_D, KeyEvent.VK_K,
                KeyEvent.VK_X, KeyEvent.VK_M,
                KeyEvent.VK_C, KeyEvent.VK_COMMA,
                KeyEvent.VK_V, KeyEvent.VK_PERIOD
        };
    }
}
