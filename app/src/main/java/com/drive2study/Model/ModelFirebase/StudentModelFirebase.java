package com.drive2study.Model.ModelFirebase;

import com.drive2study.Model.Model;
import com.drive2study.Model.Objects.Student;
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

public class StudentModelFirebase {

    //region Data Member
    ValueEventListener eventStudentsListener;
    DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("students");
    //endregion

    //region Interface Listener
    public interface GetAllStudentsListener{
        void onSuccess(List<Student> studentslist);
    }
    //endregion

    //region Action Methods
    public void addStudent(Student student){
        final Map<String, Object> dataMap = new HashMap<>();

        dataMap.put(student.userName.replace(".",","), student.toMap());
        stRef.updateChildren(dataMap);
    }

    public void cancelGetAllStudents() {
        stRef.removeEventListener(eventStudentsListener);
    }

    public void getAllStudents(final GetAllStudentsListener listener) {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("students");

        eventStudentsListener = stRef.addValueEventListener(new ValueEventListener() {
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

    //endregion
}
