package com.drive2study.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student implements Serializable {

    public String userName;
    public String fName;
    public String lName;
    public String study;
    public boolean[] daysInCollege = new boolean[7];
    public String imageUrl;
    public String loginType;

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
