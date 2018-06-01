package com.drive2study.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Student implements Serializable {

    @PrimaryKey
    @NonNull
    public String userName;
    public String fName;
    public String lName;
    public String study;
    @TypeConverters(Converters.class)
    public boolean[] daysInCollege;
    public String imageUrl;
    public String loginType;

    public Student() {
        daysInCollege = new boolean[7];
        userName = "";
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public boolean[] getDaysInCollege() {
        return daysInCollege;
    }

    public void setDaysInCollege(boolean[] daysInCollege) {
        this.daysInCollege = daysInCollege;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("fName", fName);
        result.put("lName", lName);
        result.put("study", study);
        result.put("days", boolArrayToIntList(daysInCollege));
        result.put("imageUrl", imageUrl);
        result.put("loginType", loginType);

        return result;
    }

    private List<Integer> boolArrayToIntList(boolean[] daysInCollege) {
        List<Integer> list = new ArrayList<>();
        for (boolean day : daysInCollege) {
            if (day)
                list.add(1);
            else list.add(0);
        }
        return list;
    }
}