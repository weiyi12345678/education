package com.example.education.Class;

public class MusicStatus {

    private int duration;
    private int volume;
    private boolean isPlaying;

    public MusicStatus(){
        duration = 0;
        volume = 0;
        isPlaying = false;
    }

    public MusicStatus(int duration, int volume, boolean playing){
        this.duration = duration;
        this.volume = volume;
        this.isPlaying = playing;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getDuration() {
        return duration;
    }

    public int getVolume() {
        return volume;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
