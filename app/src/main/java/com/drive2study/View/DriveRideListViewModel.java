package com.drive2study.View;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.drive2study.Model.DriveRide;
import com.drive2study.Model.Model;
import com.drive2study.Model.Student;


import java.util.List;

public class DriveRideListViewModel extends ViewModel {
    LiveData<List<DriveRide>> data;

    public LiveData<List<DriveRide>> getData(){
        data = Model.instance.getAllDriveRide();
        return data;
    }


}