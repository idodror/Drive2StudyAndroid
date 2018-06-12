package com.drive2study.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface DriveRideDao {

    @Query("select * from DriveRide")
    List<DriveRide> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(DriveRide... driveRides);

    @Delete
    void delete(DriveRide driveRide);
}
