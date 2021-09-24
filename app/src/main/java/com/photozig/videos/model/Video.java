package com.photozig.videos.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collection;

public class Video implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("bg")
    private String videoPath;
    @SerializedName("im")
    private String imagePath;
    @SerializedName("sg")
    private String audioPath;
    @SerializedName("txts")
    private Collection<Text> texts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public Collection<Text> getTexts() {
        return texts;
    }

    public void setTexts(Collection<Text> texts) {
        this.texts = texts;
    }
}

