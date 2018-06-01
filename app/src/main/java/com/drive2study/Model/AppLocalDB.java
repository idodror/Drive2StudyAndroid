package com.drive2study.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.drive2study.MyApplication;

@Database(entities = {Student.class}, version = 1)
@TypeConverters(Converters.class)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract StudentDao studentDao();
}

public class AppLocalDB {
    static public AppLocalDbRepository db = Room.databaseBuilder(MyApplication.context,
            AppLocalDbRepository.class,
            "Student.db").fallbackToDestructiveMigration().build();
}