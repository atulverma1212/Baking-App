package com.example.averma1212.bakingapp.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HP on 22-12-2017.
 */

public class Steps {
    private String id;
    private String shortDesc;
    private String desc;
    private String videoURL;
    private String imageURL;

    public Steps() {  }

    public Steps(JSONObject json){
        try {
            id = json.getString("id");
            shortDesc = json.getString("shortDescription");
            desc = json.getString("description");
            videoURL = json.getString("videoURL");
            imageURL = json.getString("thumbnailURL");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getDesc() {
        return desc;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getImageURL() { return imageURL; }
}
