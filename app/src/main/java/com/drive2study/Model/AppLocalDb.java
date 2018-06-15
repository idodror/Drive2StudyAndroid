package com.drive2study.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.drive2study.Model.Objects.DriveRide;
import com.drive2study.Model.Objects.StudentCred;
import com.drive2study.MyApplication;

@Database(entities = {DriveRide.class, StudentCred.class}, version = 2, exportSchema = false)
@TypeConverters(Converters.class)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract DriveRideDao driveRideDao();
    public abstract StudentCredDao studentCredDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository db = Room.databaseBuilder(MyApplication.context,
            AppLocalDbRepository.class,
            "dbFileName.db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
}
