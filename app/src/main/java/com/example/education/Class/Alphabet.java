package com.example.education.Class;

public class Alphabet {
    private String ID;
    private String title;
    private String word;
    private String imageUrl;
    private String gameImage;
    private int difficulty;

    public Alphabet()
    {
        this.ID = "";
        this.title = "";
        this.imageUrl = "";
        this.gameImage = "";
        this.difficulty = 0;
    }

    public Alphabet(String ID, String title, String word, String imageUrl, String gameImage, int difficulty) {
        this.ID = ID;
        this.title = title;
        this.word = word;
        this.imageUrl = imageUrl;
        this.gameImage = gameImage;
        this.difficulty = difficulty;
    }

    public String getGameImage() {
        return gameImage;
    }

    public void setGameImage(String gameImage) {
        this.gameImage = gameImage;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString(){
        return "ID: " + ID + "\nWord: " + word + "\nImage Url: " + imageUrl + "\nDifficulty: " + difficulty;
    }
}
