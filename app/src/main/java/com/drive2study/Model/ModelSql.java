package com.drive2study.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.drive2study.MyApplication;

import java.util.LinkedList;
import java.util.List;

public class ModelSql {
    final static int VERSION = 1;
    final static String ST_TABLE = "STUDENTS";
    final static String ST_USERNAME = "USERNAME";
    final static String ST_FNAME = "FNAME";
    final static String ST_LNAME = "LNAME";
    final static String ST_STUDY = "STUDY";
    final static String ST_DAYS = "DAYS_IN_COLLEGE";
    final static String ST_IMAGE_URL = "IMAGE_URL";
    final static String ST_LOGIN_TYPE = "LOGIN_TYPE";

    MyHelper helper = new MyHelper(MyApplication.context);

    public List<Student> getAllStudents(){
        String days;
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cr = db.query(ST_TABLE,null,null,null,null,null,null);

        List<Student> data = new LinkedList<Student>();
        if (cr.moveToFirst()){
            do {
                Student st = new Student();
                st.userName = cr.getString(cr.getColumnIndex(ST_USERNAME));
                st.fName = cr.getString(cr.getColumnIndex(ST_FNAME));
                st.lName = cr.getString(cr.getColumnIndex(ST_LNAME));
                st.study = cr.getString(cr.getColumnIndex(ST_STUDY));
                st.daysInCollege = stringToBoolDaysArray(cr.getString(cr.getColumnIndex(ST_DAYS)));
                st.imageUrl = cr.getString(cr.getColumnIndex(ST_IMAGE_URL));
                st.loginType = cr.getString(cr.getColumnIndex(ST_LOGIN_TYPE));
                data.add(st);
            } while (cr.moveToNext());
        }
        return data;
    }

    public void addStundet(Student st){
        String days = boolDaysArrayToString(st.daysInCollege);

        ContentValues values = new ContentValues();

        values.put(ST_USERNAME, st.userName);
        values.put(ST_FNAME, st.fName);
        values.put(ST_LNAME, st.lName);
        values.put(ST_STUDY, st.study);
        values.put(ST_DAYS, days);
        values.put(ST_IMAGE_URL, st.imageUrl);
        values.put(ST_LOGIN_TYPE, st.loginType);

        SQLiteDatabase db = helper.getWritableDatabase();
        long rowId = db.insert(ST_TABLE, ST_USERNAME, values);
    }

    // get boolean array and convert into string ("1" for true, "0" for false)
    private String boolDaysArrayToString(boolean[] daysInCollege) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < daysInCollege.length; i++) {
            if (daysInCollege[i])
                builder.append("1");    // true
            else builder.append("0");   // false
        }
        return builder.toString();
    }

    // get days as string and convert to boolean array ('1' for true - in the college that day, '0' for false)
    private boolean[] stringToBoolDaysArray(String days) {
        boolean[] daysInCollege = new boolean[7];
        for (int i = 0; i < days.length(); i++) {
            if (days.charAt(i) == '1')  // in case of 'true'
                daysInCollege[i] = true;
            else daysInCollege[i] = false;
        }
        return daysInCollege;
    }

    class MyHelper extends SQLiteOpenHelper{
        public MyHelper(Context context) {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table "
                                    + ST_TABLE + " ("
                                    + ST_USERNAME + " text primary key, "
                                    + ST_FNAME + " text, "
                                    + ST_LNAME + " text, "
                                    + ST_STUDY + " text, "
                                    + ST_DAYS + " text, "
                                    + ST_IMAGE_URL + " text, "
                                    + ST_LOGIN_TYPE + " text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("drop table " + ST_TABLE + ";");
            onCreate(sqLiteDatabase);
        }
    }
}
