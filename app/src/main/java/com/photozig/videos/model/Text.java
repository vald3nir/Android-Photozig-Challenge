package com.photozig.videos.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Text implements Serializable {

    @SerializedName("txt")
    private String txt;
    @SerializedName("time")
    private Float time;

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public Float getTime() {
        return time;
    }

    public void setTime(Float time) {
        this.time = time;
    }
}
