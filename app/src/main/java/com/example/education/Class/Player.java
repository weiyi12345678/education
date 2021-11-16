package com.example.education.Class;

public class Player {

    private String name;
    private int score;
    private int gameType;

    public Player(String name, int score, int gameType) {
        this.name = name;
        this.score = score;
        this.gameType = gameType;
    }

    public Player ()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }
}
