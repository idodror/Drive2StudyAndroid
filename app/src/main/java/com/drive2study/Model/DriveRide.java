package com.drive2study.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class DriveRide {

    public static final String RIDER = "r";
    public static final String DRIVER = "d";

    private String userName;
    private String fromWhere;
    private LatLng coordinates;
    private String type;

    public DriveRide() {
        this.coordinates = new LatLng(0,0);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("fromWhere", fromWhere);
        result.put("lat", coordinates.latitude);
        result.put("lng", coordinates.longitude);
        result.put("type", type);

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

    public LatLng getCoordinates() { return coordinates; }

    public void setCoordinates(LatLng coordinates) { this.coordinates = coordinates; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
