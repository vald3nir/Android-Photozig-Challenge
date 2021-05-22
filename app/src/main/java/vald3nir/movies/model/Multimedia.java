package vald3nir.movies.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by vald3nir on 12/12/17
 */

public class Multimedia implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("bg")
    private String video;
    @SerializedName("im")
    private String image;
    @SerializedName("sg")
    private String audio;
    @SerializedName("txts")
    private Collection<Text> texts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public Collection<Text> getTexts() {
        return texts;
    }

    public void setTexts(Collection<Text> texts) {
        this.texts = texts;
    }
}
