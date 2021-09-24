package com.photozig.videos.model;


import com.google.gson.annotations.SerializedName;

import java.util.Collection;

public class Feed {

    @SerializedName("assetsLocation")
    private String assetsLocation;

    @SerializedName("objects")
    private Collection<Video> videos;

    public String getAssetsLocation() {
        return assetsLocation;
    }

    public void setAssetsLocation(String assetsLocation) {
        this.assetsLocation = assetsLocation;
    }

    public Collection<Video> getVideos() {
        return videos;
    }

    public void setVideos(Collection<Video> videos) {
        this.videos = videos;
    }
}

