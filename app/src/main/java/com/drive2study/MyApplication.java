package com.drive2study;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.drive2study.Model.Objects.Student;

public class MyApplication extends Application {

    public static Context context;
    public static Student currentStudent = new Student();
    public static SharedPreferences sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sharedPref = getSharedPreferences("Data", Context.MODE_PRIVATE);
    }
}
