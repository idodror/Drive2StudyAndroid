package com.drive2study.Model;

import java.util.List;

public class Model {
    public static Model instance = new Model();
    ModelFirebase modelFirebase;
    private Model(){
        modelFirebase = new ModelFirebase();
    }

    ModelSql modelSql = new ModelSql();

    public void cancellGetAllStudents() {
        modelFirebase.cancellGetAllStudents();
    }

    public interface GetAllStudentsListener{
        void onSuccess(List<Student> studentsList);
    }
    public void getAllStudents(final GetAllStudentsListener listener){
        modelFirebase.getAllStudents(new ModelFirebase.GetAllStudentsListener() {
            @Override
            public void onSuccess(List<Student> studentslist) {
                listener.onSuccess(studentslist);
            }
        });
        //return modelSql.getAllStudents();
    }

    public void addStudent(Student st){
        //StudentSql.addStudent(st,modelSql.getWritableDatabase());

        modelFirebase.addStudent(st);
    }

}
