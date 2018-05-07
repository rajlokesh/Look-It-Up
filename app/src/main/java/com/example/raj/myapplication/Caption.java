package com.example.raj.myapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Caption {

    private String caption;

    @SerializedName("bounding_box")
    @Expose
    public List<Double> boundingBox = null;
    private Double confidence;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<Double> getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(List<Double> boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

}
