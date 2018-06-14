package com.drive2study.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StudentCredDao {

    @Query("SELECT * FROM StudentCred")
    List<StudentCred> getUserCred();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StudentCred studentCred);

    @Query("delete from StudentCred")
    void clearTable();

}