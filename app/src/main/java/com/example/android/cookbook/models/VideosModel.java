package com.example.android.cookbook.models;

import android.arch.persistence.room.TypeConverter;
import android.provider.Settings;
import android.text.TextUtils;

import com.example.android.cookbook.globaldata.Global_Constants;

/**
 * Created by raghvendra on 14/7/18.
 */

public class VideosModel  {

    private String id;
     private String shortDescription;
    private String description;
    private String thumbnailURL;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getVideoURL() {
        if (TextUtils.isEmpty(videoURL))
            return Global_Constants.NO_DATA;
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    private String videoURL;

    public String getThumbnailURL() {
        if (TextUtils.isEmpty(thumbnailURL))
              return  Global_Constants.NO_DATA;
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }



}
