package model;

public class Giocatore {
    private String nome;
    private Player player;

    public Giocatore(String nome){
        this.nome = nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setPlayer(Player player){
        this.player = player;
    }
}
