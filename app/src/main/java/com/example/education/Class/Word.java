package com.example.education.Class;

public class Word {
    private String ID;
    private String word;
    private String parent;
    private String imageUrl;
    private String title;

    public Word (){
        this.ID = "";
        this.word = "";
        this.imageUrl = "";
        this.parent = "";
        this.title = "";
    }

    public Word(String ID, String word, String parent, String imageUrl, String title) {
        this.ID = ID;
        this.word = word;
        this.parent = parent;
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
