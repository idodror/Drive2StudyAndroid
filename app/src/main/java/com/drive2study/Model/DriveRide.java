package com.drive2study.Model;

import java.util.HashMap;
import java.util.Map;

public class DriveRide {

    public static final String RIDER = "r";
    public static final String DRIVER = "d";

    private String userName;
    private String fromWhere;
    private String type;
    private String imageUrl;

    public DriveRide() {

    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("fromWhere", fromWhere);
        result.put("type", type);
        result.put("imageUrl", imageUrl);

        return result;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
