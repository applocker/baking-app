package com.dappslocker.bakingapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tiwuya on 10,October,2018
 */
class Step {

/*    "id":0,
            "shortDescription":"Recipe Introduction",
            "description":"Recipe Introduction",
            "videoURL":"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
            "thumbnailURL":""*/

    @SerializedName("id")
    Integer id;
    @SerializedName("shortDescription")
    String shortDescription;
    @SerializedName("description")
    String description;
    @SerializedName("videoURL")
    String videoURL;
    @SerializedName("thumbnailURL")
    String thumbnailURL;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescriptiosn) {
        this.shortDescription = shortDescriptiosn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
