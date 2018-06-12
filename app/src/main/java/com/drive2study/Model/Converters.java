package com.drive2study.Model;

import android.arch.persistence.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;

import java.util.regex.Pattern;

public class Converters {

    @TypeConverter
    public String fromLatLngToString(LatLng latLng) {
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append(latLng.latitude).append("$").append(latLng.longitude);
        return builder.toString();
    }

    @TypeConverter
    public LatLng fromStringToLatLng(String str) {
        String[] strArray = str.split(Pattern.quote("$"));
        double lat = Double.parseDouble(strArray[0]);
        double lng = Double.parseDouble(strArray[1]);
        return new LatLng(lat, lng);
    }

}
