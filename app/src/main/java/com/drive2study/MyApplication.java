package com.drive2study;

import android.app.Application;
import android.content.Context;

import com.drive2study.Model.Student;

public class MyApplication extends Application {
    public static Context context;

    public static Student currentStudent = new Student();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
