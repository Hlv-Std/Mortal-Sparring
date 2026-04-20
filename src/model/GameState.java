package model;

import javax.swing.*;
import java.awt.*;

public class GameState {
    private static GameState instance;

    private Player player1;
    private Player player2;
    private Background bg;

    private int windowWidth;
    private int windowHeight;
    private boolean isFullscreen;
    private boolean contextHasChanged;

    private GameState(){}

    public static synchronized GameState getInstance(){
        if (instance == null)
            instance = new GameState();
        return instance;
    }

    public synchronized void setWindowFullscreen(JFrame window, boolean flag){
        window.dispose();
        window.setUndecorated(flag);
        window.setVisible(true);
        GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .setFullScreenWindow(flag ? window : null);
        contextHasChanged = true;
        isFullscreen = flag;
    }

    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public Background getBg() { return bg; }
    public int getWindowWidth(){ return windowWidth; }
    public int getWindowHeight(){ return windowHeight; }
    public boolean checkForChanges(){ return contextHasChanged; }
    public boolean isFullscreen(){ return isFullscreen; }

    public synchronized void setPlayer1(Player player1) { this.player1 = player1; }
    public synchronized void setPlayer2(Player player2) { this.player2 = player2; }
    public synchronized void setBg(Background bg) { this.bg = bg; }
    public synchronized void setWindowWidth(int windowWidth){ this.windowWidth = windowWidth; }
    public synchronized void setWindowHeight(int windowHeight){ this.windowHeight = windowHeight; }
    public synchronized void ok(){ contextHasChanged = false; }
}
