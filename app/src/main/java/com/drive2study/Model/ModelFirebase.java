package com.drive2study.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    List<DriveRide> drList = null;
    DatabaseReference driveRideDatabase = FirebaseDatabase.getInstance().getReference().child("driveRide");

    ////////////////////////////////////////////////////////
    ///////////          Student                   /////////
    ////////////////////////////////////////////////////////
    public void addStudent(Student student){
        final Map<String, Object> dataMap = new HashMap<>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("students");
        dataMap.put(student.userName, student.toMap());
        mDatabase.updateChildren(dataMap);
    }

    public void cancelGetAllStudents() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("students");
        stRef.removeEventListener(eventListener);
    }

    interface GetAllStudentsListener{
        public void onSuccess(List<Student> studentslist);
    }

    ValueEventListener eventListener;

    public void getAllStudents(final GetAllStudentsListener listener) {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("students");

        eventListener = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Student> stList = new LinkedList<>();

                for (DataSnapshot stSnapshot: dataSnapshot.getChildren()) {
                    Student st = stSnapshot.getValue(Student.class);
                    stList.add(st);
                }
                listener.onSuccess(stList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUserExists(String email, final Model.GetUserExistsListener listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("students").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {
                    // use "username" already exists
                    // Let the user know he needs to pick another username.
                    listener.onDone(true);
                } else {
                    // User does not exist. NOW call createUserWithEmailAndPassword
                    listener.onDone(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getStudent(String email, final Model.GetStudentListener listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("students").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {
                    listener.onDone((Student) dataSnapshot.getValue());
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

    ////////////////////////////////////////////////////////
    ///////////          DriveRide                   ///////
    ////////////////////////////////////////////////////////
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

    interface GetAllDriveRideListener{
        public void onSuccess(List<DriveRide> driveRideList);
    }

    private ValueEventListener eventDriveRideListener;

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

    private DriveRide getDriveRideFromFirebaseSnapshot(DataSnapshot drSnapshot) {
        DriveRide dr = drSnapshot.getValue(DriveRide.class);
        Double lat = drSnapshot.child("lat").getValue(Double.class);
        Double lng = drSnapshot.child("lng").getValue(Double.class);
        if (lat != null && lng != null)
            dr.setCoordinates(new LatLng(lat, lng));
        return dr;
    }

    //Managing Files
    public void saveImage(Bitmap imageBitmap, final Model.SaveImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        Date d = new Date();
        String name = ""+ d.getTime();
        StorageReference imagesRef = storage.getReference().child("pics").child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onDone(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.onDone(downloadUrl.toString());
            }
        });

    }

    public void getImage(String url, final Model.GetImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                Log.d("TAG","get image from firebase success");
                listener.onDone(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.d("TAG",exception.getMessage());
                Log.d("TAG","get image from firebase Failed");
                listener.onDone(null);
            }
        });
    }

}
