package model;

import controller.InputProcesser;

public class Player {
    private String nome;
    private Character character;
    private final InputProcesser inputProcesser;

    public Player(String nome){
        this.nome = nome;
        inputProcesser = new InputProcesser();
    }

    public String getNome() { return nome; }
    public Character getCharacter(){ return character; }
    public InputProcesser getInputProcesser(){ return inputProcesser; }

    public void setNome(String nome){
        this.nome = nome;
    }
    public void setCharacter(Character character){
        this.character = character;
    }
}
