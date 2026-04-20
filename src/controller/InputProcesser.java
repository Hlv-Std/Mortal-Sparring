package controller;

import java.util.ArrayDeque;
import java.util.Deque;

public class InputProcesser {
    private final Deque<Integer> buffer;
    private long lastInputTime;
    private static final long COMBO_WINDOW_MS = 100;

    public InputProcesser(){
        buffer         = new ArrayDeque<>();
        lastInputTime  = 0;
    }

    void checkCombo(int keyCode){
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
        buffer.clear();
        return true;
    }

}
