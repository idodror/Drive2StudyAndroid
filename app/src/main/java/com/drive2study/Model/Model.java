package com.drive2study.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by eliav.menachi on 09/05/2018.
 */

public class Model {
    public static Model instance = new Model();
    ModelFirebase modelFirebase;
    private Model(){
        modelFirebase = new ModelFirebase();
    }

    ////////////////////////////////////////////////////////
    ///////////          Student                   /////////
    ////////////////////////////////////////////////////////

    public void cancellGetAllStudents() {
        modelFirebase.cancellGetAllStudents();
    }

    class StudentListData extends  MutableLiveData<List<Student>>{
        @Override
        protected void onActive() {
            super.onActive();
            modelFirebase.getAllStudents(new ModelFirebase.GetAllStudentsListener() {
                @Override
                public void onSuccess(List<Student> studentslist) {
                    Log.d("TAG","FB data = " + studentslist.size() );
                    setValue(studentslist);
                    for (Student st : studentslist){
                        //AppLocalDb.db.studentDao().insertAll(st);
                    }
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            modelFirebase.cancellGetAllStudents();
            Log.d("TAG","cancellGetAllStudents");
        }

        public StudentListData() {
            super();
            //setValue(AppLocalDb.db.studentDao().getAll());
            setValue(new LinkedList<Student>());
        }
    }

    StudentListData studentListData = new StudentListData();

    public LiveData<List<Student>> getAllStudents(){
        return studentListData;
    }

    public void addStudent(Student st){
        modelFirebase.addStudent(st);
    }

    ////////////////////////////////////////////////////////
    ///////////          DriveRide                   ///////
    ////////////////////////////////////////////////////////

    public void cancellGetAllDriveRide() {
        modelFirebase.cancellGetAllDriveRide();
    }

    class DriveRideListData extends  MutableLiveData<List<DriveRide>>{
        @Override
        protected void onActive() {
            super.onActive();
            modelFirebase.getAllDriveRide(new ModelFirebase.GetAllDriveRideListener() {
                @Override
                public void onSuccess(List<DriveRide> driveRideList) {
                    Log.d("TAG","FB data = " + driveRideList.size() );
                    setValue(driveRideList);
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            modelFirebase.cancellGetAllDriveRide();
            Log.d("TAG","cancellGetAllStudents");
        }

        public DriveRideListData() {
            super();
            setValue(new LinkedList<>());
        }
    }

    private DriveRideListData driveRideListData = new DriveRideListData();

    public LiveData<List<DriveRide>> getAllDriveRide(){
        return driveRideListData;
    }

    public void addDriveRide(DriveRide dr_rd){ modelFirebase.addDriveRide(dr_rd); }



    ////////////////////////////////////////////////////////
    //  HAndle Image Files
    ////////////////////////////////////////////////////////



    public interface SaveImageListener{
        void onDone(String url);
    }

    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap,listener);
    }



    public interface GetImageListener{
        void onDone(Bitmap imageBitmap);
    }
    public void getImage(final String url, final GetImageListener listener ){
        String localFileName = URLUtil.guessFileName(url, null, null);
        final Bitmap image = loadImageFromFile(localFileName);
        if (image == null) {                                      //if image not found - try downloading it from parse
            modelFirebase.getImage(url, new GetImageListener() {
                @Override
                public void onDone(Bitmap imageBitmap) {
                    if (imageBitmap == null) {
                        listener.onDone(null);
                    }else {
                        //2.  save the image localy
                        String localFileName = URLUtil.guessFileName(url, null, null);
                        Log.d("TAG", "save image to cache: " + localFileName);
                        saveImageToFile(imageBitmap, localFileName);
                        //3. return the image using the listener
                        listener.onDone(imageBitmap);
                    }
                }
            });
        }else {
            Log.d("TAG","OK reading cache image: " + localFileName);
            listener.onDone(image);
        }
    }

    // Store / Get from local mem
    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        if (imageBitmap == null) return;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //addPicureToGallery(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String imageFileName){
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}