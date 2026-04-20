package model;

import javax.swing.*;
import java.awt.*;

public class GameState {
    private static GameState instance;

    private Player player1;
    private Player player2;
    private Background bg;

    public static final int WINDOWED_WIDTH = 701;
    public static final int WINDOWED_HEIGHT = 401;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    private int windowWidth;
    private int windowHeight;
    private boolean isFullscreen;
    private boolean windowHasChanged;

    private boolean isPaused = false;
    private boolean quit = false;

    private GameState(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension d = toolkit.getScreenSize();
        SCREEN_WIDTH = d.width;
        SCREEN_HEIGHT = d.height;
    }

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

        if (flag){
            windowWidth = SCREEN_WIDTH;
            windowHeight = SCREEN_HEIGHT;
        } else {
            windowWidth = WINDOWED_WIDTH;
            windowHeight = WINDOWED_HEIGHT;
        }

        windowHasChanged = true;
        isFullscreen = flag;
    }

    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public Background getBg() { return bg; }
    public int getWindowWidth(){ return windowWidth; }
    public int getWindowHeight(){ return windowHeight; }
    public boolean windowHasChanged(){ return windowHasChanged; }
    public boolean isFullscreen(){ return isFullscreen; }
    public boolean isPaused(){ return isPaused; }
    public synchronized void waitForQuit() throws InterruptedException {
        while(!quit)
            wait();
    }

    public synchronized void setPlayer1(Player player1) { this.player1 = player1; }
    public synchronized void setPlayer2(Player player2) { this.player2 = player2; }
    public synchronized void setBg(Background bg) { this.bg = bg; }
    public synchronized void setWindowWidth(int windowWidth){ this.windowWidth = windowWidth; }
    public synchronized void setWindowHeight(int windowHeight){ this.windowHeight = windowHeight; }
    public synchronized void ok(){ contextHasChanged = false; }
    public synchronized void pauseGame(boolean pause){ this.isPaused = pause; }
    public synchronized void quit(){
        quit = true;
        notifyAll();
    }
}
