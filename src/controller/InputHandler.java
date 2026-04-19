package controller;

import model.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class InputHandler {
    private final Player player1;
    private final Player player2;

    private final Set<Integer> keysHeld;
    private final InputMap inputMap;
    private final ActionMap actionMap;

    public InputHandler(JComponent component, Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        keysHeld       = new HashSet<>();
        inputMap       = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        actionMap      = component.getActionMap();
    }

    public void start(){
        for(int keyCode : getP1Keys()){
            String pressed = "pressed_" + keyCode;
            String released = "released_" + keyCode;

            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), pressed);
            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), released);


            actionMap.put(pressed, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    player1.getInputProcesser().checkCombo(keyCode);
                    keysHeld.add(keyCode);
                }
            });
            actionMap.put(released, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) { keysHeld.remove(keyCode); }
            });
        }

        for(int keyCode : getP2Keys()){
            String pressed = "pressed_" + keyCode;
            String released = "released_" + keyCode;

            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), pressed);
            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), released);


            actionMap.put(pressed, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    player2.getInputProcesser().checkCombo(keyCode);
                    keysHeld.add(keyCode);
                }
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

    private int[] getP1Keys(){
        return new int[]{
                KeyEvent.VK_W,
                KeyEvent.VK_A,
                KeyEvent.VK_S,
                KeyEvent.VK_D,
                KeyEvent.VK_X,
                KeyEvent.VK_C,
                KeyEvent.VK_V
        };
    }

    private int[] getP2Keys(){
       return new int[] {
               KeyEvent.VK_U,
               KeyEvent.VK_H,
               KeyEvent.VK_J,
               KeyEvent.VK_K,
               KeyEvent.VK_M,
               KeyEvent.VK_COMMA,
               KeyEvent.VK_PERIOD
       };
    }
}



