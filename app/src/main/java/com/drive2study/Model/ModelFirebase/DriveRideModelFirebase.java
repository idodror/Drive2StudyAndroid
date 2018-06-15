package com.drive2study.Model.ModelFirebase;

import com.drive2study.Model.Objects.DriveRide;
import com.drive2study.Model.Model;
import com.drive2study.Model.Objects.Student;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DriveRideModelFirebase {

    //region Data Members
    List<DriveRide> drList = null;
    DatabaseReference driveRideDatabase = FirebaseDatabase.getInstance().getReference().child("driveRide");
    private ValueEventListener eventDriveRideListener;
    //endregion

    //region Interface Listener
    public interface GetAllDriveRideListener{
        void onSuccess(List<DriveRide> driveRideList);
    }
    //endregion

    //region Action Methods
    public void addDriveRide(DriveRide driveRide){
        final Map<String, Object> dataMap = new HashMap<>();

        String lastChild;
        if (driveRide.getType().equals(DriveRide.DRIVER))
            lastChild = DriveRide.DRIVER;
        else lastChild = DriveRide.RIDER;

        dataMap.put(driveRide.getUserName(), driveRide.toMap());
        driveRideDatabase.child(lastChild).updateChildren(dataMap);
    }

    public void cancelGetAllDriveRide() {
        driveRideDatabase.removeEventListener(eventDriveRideListener);
    }

    public void getAllDriveRide(final GetAllDriveRideListener listener) {
        eventDriveRideListener = driveRideDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                drList = new LinkedList<>();

                for (DataSnapshot drSnapshot: dataSnapshot.child(DriveRide.RIDER).getChildren())
                    drList.add(getDriveRideFromFirebaseSnapshot(drSnapshot));

                for (DataSnapshot drSnapshot: dataSnapshot.child(DriveRide.DRIVER).getChildren())
                    drList.add(getDriveRideFromFirebaseSnapshot(drSnapshot));

                listener.onSuccess(drList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getDriveRide(String email, final Model.GetStudentListener listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("students").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Student student = dataSnapshot.getValue(Student.class);
                    GenericTypeIndicator<ArrayList<Integer>> t = new GenericTypeIndicator<ArrayList<Integer>>() {};
                    ArrayList<Integer> daysAsIntList = dataSnapshot.child("days").getValue(t);
                    boolean[] daysAsArray = Student.intListToBoolArray(daysAsIntList);
                    if (student != null)
                        student.setDaysInCollege(daysAsArray);
                    listener.onDone(student);
                } else {
                    // User does not exist. NOW call createUserWithEmailAndPassword
                    listener.onDone(new Student());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private DriveRide getDriveRideFromFirebaseSnapshot(DataSnapshot drSnapshot) {
        DriveRide dr = drSnapshot.getValue(DriveRide.class);
        Double lat = drSnapshot.child("lat").getValue(Double.class);
        Double lng = drSnapshot.child("lng").getValue(Double.class);
        if (lat != null && lng != null)
            dr.setCoordinates(new LatLng(lat, lng));
        return dr;
    }

    //endregion

}
