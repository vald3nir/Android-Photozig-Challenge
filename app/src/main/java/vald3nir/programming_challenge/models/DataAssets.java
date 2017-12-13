package vald3nir.programming_challenge.models;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

/**
 * Created by vald3nir on 12/12/17
 */

public class DataAssets {

    @SerializedName("assetsLocation")
    private String assetsLocation;

    @SerializedName("objects")
    private Collection<Multimedia> multimedias;

    public String getAssetsLocation() {
        return assetsLocation;
    }

    public void setAssetsLocation(String assetsLocation) {
        this.assetsLocation = assetsLocation;
    }

    public Collection<Multimedia> getMultimedia() {
        return multimedias;
    }

    public void setMultimedia(Collection<Multimedia> multimedia) {
        this.multimedias = multimedia;
    }
}
