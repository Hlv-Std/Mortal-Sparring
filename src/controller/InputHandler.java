package controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

// TODO: add threshold to allow combo and fix double jump
public class InputHandler {
    private JComponent component;
    private Set<Integer> keysHeld;
   private InputMap inputMap;
   private ActionMap actionMap;

    private final Deque<Integer> buffer;
    private long lastInputTime;
    private static final long COMBO_WINDOW_MS = 100;

    public InputHandler(JComponent component){
        this.component = component;
        keysHeld       = new HashSet<>();
        buffer         = new ArrayDeque<>();
        lastInputTime  = 0;
        inputMap       = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        actionMap      = component.getActionMap();
    }

    private void checkCombo(int keyCode){
        long now = System.currentTimeMillis();
        if (now - lastInputTime > COMBO_WINDOW_MS)
            buffer.clear();
        buffer.addLast(keyCode);
        lastInputTime = now;
        if (buffer.size() > 10) buffer.removeFirst();
    }

    public boolean combo(int... combo){
        if (buffer.size() < combo.length) return false;
        Integer[] tail = buffer.toArray(new Integer[0]);
        int offset = tail.length - combo.length;
        for(int i = 0; i < combo.length; i++){
            if (tail[offset + i] != combo[i]) return false;
        }
        return true;
    }

    public void start(){
        for(int keyCode : getKeys()){
            String pressed = "pressed_" + keyCode;
            String released = "released_" + keyCode;

            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), pressed);
            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), released);


            actionMap.put(pressed, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    checkCombo(keyCode);
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
