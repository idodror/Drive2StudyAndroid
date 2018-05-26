package com.drive2study.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class ModelFirebase {

    public void addStudent(Student student){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("students").child(student.userName).push();
        mDatabase.child("fName").setValue(student.fName);
        //mDatabase.child("students").child(student.userName).setValue(student);
    }

    public void cancellGetAllStudents() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("students");
        stRef.removeEventListener(eventListener);
    }

    interface GetAllStudentsListener{
        public void onSuccess(List<Student> studentslist);
    }

    private ValueEventListener eventListener;

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
}
