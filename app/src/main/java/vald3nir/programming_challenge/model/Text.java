package vald3nir.programming_challenge.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vald3nir on 12/12/17
 */

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
