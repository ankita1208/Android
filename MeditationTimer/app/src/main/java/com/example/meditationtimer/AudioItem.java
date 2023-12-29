package com.example.meditationtimer;

public class AudioItem {

    private String title;
    private String duration;
    private int thumbnailResId;

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    private String audioUrl;

    public AudioItem(String title, String duration, int thumbnailResId, String audioUrl) {
        this.title = title;
        this.duration = duration;
        this.thumbnailResId = thumbnailResId;
        this.audioUrl = audioUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public int getThumbnailResId() {
        return thumbnailResId;
    }
}
