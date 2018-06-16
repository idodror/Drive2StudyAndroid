package com.drive2study.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.drive2study.Model.ModelFirebase.ChatModelFirebase;
import com.drive2study.Model.ModelFirebase.DriveRideModelFirebase;
import com.drive2study.Model.ModelFirebase.StorageModelFirebase;
import com.drive2study.Model.ModelFirebase.StudentModelFirebase;
import com.drive2study.Model.Objects.DriveRide;
import com.drive2study.Model.Objects.MessageDetails;
import com.drive2study.Model.Objects.Student;
import com.drive2study.Model.Objects.StudentCred;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class Model {

    //region Interfaces Listener

    public interface GetStudentListener {
        void onDone(Student student);
    }

    public interface GetStudentCredListener {
        void onDone(StudentCred studentCred);
    }

    public interface GetUserExistsListener{
        void onDone(boolean result);
    }

    public interface SaveImageListener{
        void onDone(String url);
    }

    public interface GetMessageListener{
        void onDone(MessageDetails msg);
    }


    //endregion

    //region Data members and CTOR

    public static Model instance = new Model();
    DriveRideModelFirebase driveRideModelFirebase;
    StorageModelFirebase storageModelFirebase;
    StudentModelFirebase studentModelFirebase;
    ChatModelFirebase chatModelFirebase;

    private Model(){
        driveRideModelFirebase = new DriveRideModelFirebase();
        storageModelFirebase = new StorageModelFirebase();
        studentModelFirebase = new StudentModelFirebase();
        chatModelFirebase = new ChatModelFirebase();
    }
    //endregion

    //region List Data's

    StudentListData studentListData = new StudentListData();
    DriveRideListData driveRideListData = new DriveRideListData();
    MessagesListData messagesListData = new MessagesListData();
    //endregion

    //region Student Model

    class StudentListData extends MutableLiveData<List<Student>>{
        @Override
        protected void onActive() {
            super.onActive();
            studentModelFirebase.getAllStudents(studentslist -> {
                Log.d("TAG","FB data = " + studentslist.size() );
                setValue(studentslist);
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            studentModelFirebase.cancelGetAllStudents();
            Log.d("TAG","cancelGetAllStudents");
        }

        public StudentListData() {
            super();
            //setValue(AppLocalDb.db.studentDao().getAll());
            setValue(new LinkedList<Student>());
        }
    }

    public void cancellGetAllStudents() {
        studentModelFirebase.cancelGetAllStudents();
    }

    public LiveData<List<Student>> getAllStudents(){
        return studentListData;
    }

    public void setUserCred(String email, String password) {
        StudentCredDao studentCredDao = AppLocalDb.db.studentCredDao();
        StudentCred studentCred = new StudentCred();
        studentCred.setEmail(email);
        studentCred.setPassword(password);

        studentCredDao.clearTable();
        studentCredDao.insert(studentCred);
    }

    public void getStudent(String email, final GetStudentListener listener){
        email = email.replace(".", ",");
        studentModelFirebase.getStudent(email, student -> listener.onDone(student));
    }

    public void addStudent(Student st){
        studentModelFirebase.addStudent(st);
    }

    public void clearStudentCred() {
        AppLocalDb.db.studentCredDao().clearTable();
    }

    public void getStudentCred(final GetStudentCredListener listener) {
        List<StudentCred> list = AppLocalDb.db.studentCredDao().getUserCred();
        if (list.size() == 1) {
            listener.onDone(list.get(0));
        } else listener.onDone(null);
    }

    //endregion

    //region DriveRide Model
    class DriveRideListData extends MutableLiveData<List<DriveRide>> {

        @Override
        protected void onActive() {
            super.onActive();
            // new thread tsks
            // 1. get the students list from the local DB
            DriveRideAsyncDao.getAll((List<DriveRide> data) -> {
                // 2. onComplete: update the live data with the new student list
                setValue(data);
                Log.d("TAG","got students from local DB " + data.size());

                // 3. get the student list from firebase
                driveRideModelFirebase.getAllDriveRide(driveRideList -> {
                    // 4. onSuccess: update the live data with the new student list
                    setValue(driveRideList);
                    Log.d("TAG","got driveRides from firebase " + driveRideList.size());

                    // 5. onComplete: update the local DB
                    DriveRideAsyncDao.insertAll(driveRideList, data1 -> Log.d("TAG","local data updated: " + data1.toString()));
                });
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            driveRideModelFirebase.cancelGetAllDriveRide();
            Log.d("TAG","cancelGetAllDriveRides");
        }

        DriveRideListData() {
            super();
            setValue(new LinkedList<>());
        }
    }

    public LiveData<List<DriveRide>> getAllDriveRide(){
        return driveRideListData;
    }

    public void addDriveRide(DriveRide dr_rd){ driveRideModelFirebase.addDriveRide(dr_rd); }

    public void cancelGetAllDriveRide() {
        driveRideModelFirebase.cancelGetAllDriveRide();
    }

    public void cancelGetAllMessages() {
        chatModelFirebase.cancelGetAllMessages();
    }
    //endregion

    //region Messages Model
    class MessagesListData extends MutableLiveData<List<MessageDetails>>{
        @Override
        protected void onActive() {
            super.onActive();
            chatModelFirebase.getAllMessages(messagesList -> {
                setValue(messagesList);
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            chatModelFirebase.cancelGetAllMessages();
        }

        public MessagesListData() {
            super();
            setValue(new LinkedList<MessageDetails>());
        }
    }

    public LiveData<List<MessageDetails>> getAllMessages(){
        return messagesListData;
    }

    public void addMessage (MessageDetails msg){
        chatModelFirebase.addMessage(msg);
    }

    public void addChildAddedListener (final GetMessageListener listener){
        chatModelFirebase.addChildAddedListener(new GetMessageListener() {
            @Override
            public void onDone(MessageDetails msg) {
                listener.onDone(msg);
            }
        });
    }
    //endregion

    //region Handle Image Files

    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
        storageModelFirebase.saveImage(imageBitmap,listener);
    }

    public interface GetImageListener{
        void onDone(Bitmap imageBitmap);
    }

    public void userExists(String email, final GetUserExistsListener listener){
        studentModelFirebase.getUserExists(email.replace(".",","), new GetUserExistsListener() {
            @Override
            public void onDone(boolean result) {
                listener.onDone(result);
            }
        });
    }

    public void getImage(final String url, final GetImageListener listener ){
        String localFileName = URLUtil.guessFileName(url, null, null);
        final Bitmap image = loadImageFromFile(localFileName);
        if (image == null) {                                      //if image not found - try downloading it from parse
            storageModelFirebase.getImage(url, new GetImageListener() {
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

    //endregion

    //region Local Storage

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
            //imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //addPicureToGallery(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String imageFileName){
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir, imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag", "got image from cache: " + imageFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    //endregion

}