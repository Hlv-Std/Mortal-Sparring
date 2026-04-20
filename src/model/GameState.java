package model;

public class GameState {
    private static GameState instance;

    private Player player1;
    private Player player2;
    private Background bg;

    private int windowWidth;
    private int windowHeight;

    private GameState(){}

    public static GameState getInstance(){
        if (instance == null)
            return new GameState();
        return instance;
    }

    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public Background getBg() { return bg; }
    public int getWindowWidth(){ return windowWidth; }
    public int getWindowHeight(){ return windowHeight; }

    public void setPlayer1(Player player1) { this.player1 = player1; }
    public void setPlayer2(Player player2) { this.player2 = player2; }
    public void setBg(Background bg) { this.bg = bg; }
    public void setWindowWidth(int windowWidth){ this.windowWidth = windowWidth; }
    public void setWindowHeight(int windowHeight){ this.windowHeight = windowHeight; }
}
