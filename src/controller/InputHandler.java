package controller;

import model.GameState;
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

    private final int[] p1Keys;
    private final int[] p2Keys;
    private final int pauseKey;
    private final int restartKey;
    private final int quitKey;

    public InputHandler(JComponent component, Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        keysHeld       = new HashSet<>();
        inputMap       = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        actionMap      = component.getActionMap();

        p1Keys = new int[]{
                KeyEvent.VK_W,
                KeyEvent.VK_A,
                KeyEvent.VK_S,
                KeyEvent.VK_D,
                KeyEvent.VK_X,
                KeyEvent.VK_C,
                KeyEvent.VK_V
        };
        p2Keys = new int[]{
                KeyEvent.VK_U,
                KeyEvent.VK_H,
                KeyEvent.VK_J,
                KeyEvent.VK_K,
                KeyEvent.VK_M,
                KeyEvent.VK_COMMA,
                KeyEvent.VK_PERIOD
        };
        pauseKey = KeyEvent.VK_ESCAPE;
        restartKey = KeyEvent.VK_R;
        quitKey = KeyEvent.VK_Q;
    }

    public void start(){
        for(int keyCode : p1Keys){
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

        for(int keyCode : p2Keys){
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

        String pressed = "pressed_ " + pauseKey;
        inputMap.put(KeyStroke.getKeyStroke(pauseKey, 0, false), pressed);
        actionMap.put(pressed, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameState.getInstance().pauseGame(!GameState.getInstance().isPaused());
            }
        });

        pressed = "pressed_ " + restartKey;
        inputMap.put(KeyStroke.getKeyStroke(restartKey, 0, false), pressed);
        actionMap.put(pressed, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
              GameState state = GameState.getInstance();
              if (state.isPaused()){
                state.restartGame();
                state.setGameover(false);
                state.pauseGame(false);
              }
            }
        });

        pressed = "pressed_ " + quitKey;
        inputMap.put(KeyStroke.getKeyStroke(quitKey, 0, false), pressed);
        actionMap.put(pressed, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
              GameState state = GameState.getInstance();
                if (state.isPaused()){
                    state.quit();
                }
            }
        });
    }

    public boolean isHeld(int keyCode){
        return keysHeld.contains(keyCode);
    }
}



