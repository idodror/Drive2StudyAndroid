package com.drive2study.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

@Entity
public class DriveRide {

    public static final String RIDER = "r";
    public static final String DRIVER = "d";

    @PrimaryKey
    @NonNull
    private String userName;
    private String fromWhere;
    @TypeConverters(Converters.class)
    private LatLng coordinates;
    private String type;

    public DriveRide() {
        this.userName = "";
        this.coordinates = new LatLng(0,0);
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
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


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("fromWhere", fromWhere);
        result.put("lat", coordinates.latitude);
        result.put("lng", coordinates.longitude);
        result.put("type", type);

        return result;
    }

}
