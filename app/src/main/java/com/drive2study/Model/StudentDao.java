package com.drive2study.Model;

import android.arch.persistence.room.Dao;

@Dao
public interface StudentDao {
    /*@Query("select * from Student")
    List<Student> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Student... students);

    @Delete
    void delete(Student student);*/
}