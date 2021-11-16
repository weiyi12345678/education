package com.example.education.Class;

public class Ranking {
    private String name;
    private long score;
    private int gameType;

    public Ranking()
    {

    }

    public Ranking(String name, long score, int gameType) {
        this.name = name;
        this.score = score;
        this.gameType = gameType;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
