package com.drive2study.Model;

import android.arch.persistence.room.TypeConverter;

public class Converters {
    // get boolean array and convert into string ("1" for true, "0" for false)
    @TypeConverter
    public String fromDaysInCollegeArray(boolean[] daysInCollege) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < daysInCollege.length; i++) {
            if (daysInCollege[i])
                builder.append("1");    // true
            else builder.append("0");   // false
        }
        return builder.toString();
    }

    // get days as string and convert to boolean array ('1' for true - in the college that day, '0' for false)
    @TypeConverter
    public boolean[] toDaysInCollegeArray(String days) {
        boolean[] daysInCollege = new boolean[7];
        for (int i = 0; i < days.length(); i++) {
            if (days.charAt(i) == '1')  // in case of 'true'
                daysInCollege[i] = true;
            else daysInCollege[i] = false;
        }
        return daysInCollege;
    }

}
