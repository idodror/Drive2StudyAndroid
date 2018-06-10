package com.drive2study.View;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.drive2study.Model.DriveRide;
import com.drive2study.Model.Model;
import com.drive2study.Model.Student;

import java.util.List;

public class DataViewModel extends ViewModel {

    private LiveData<List<DriveRide>> driveRideListData;
    private LiveData<List<Student>> studentsListData;

    public LiveData<List<DriveRide>> getDriveRideListData(){
        driveRideListData = Model.instance.getAllDriveRide();
        return driveRideListData;
    }

    public LiveData<List<Student>> getStudentsListData(){
        studentsListData = Model.instance.getAllStudents();
        return studentsListData;
    }
}