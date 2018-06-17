package com.drive2study.Model;

import android.os.AsyncTask;
import com.drive2study.Model.Objects.DriveRide;
import java.util.List;

public class DriveRideAsyncDao {

    interface DriveRideAsyncDaoListener<T>{
        void onComplete(T data);
    }

    static public void getAll(final DriveRideAsyncDaoListener<List<DriveRide>> listener) {

        class DriveRideAsyncTask extends AsyncTask<String,String,List<DriveRide>> {
            @Override
            protected List<DriveRide> doInBackground(String... strings) {
                return AppLocalDb.db.driveRideDao().getAll();
            }

            @Override
            protected void onPostExecute(List<DriveRide> driveRides) {
                super.onPostExecute(driveRides);
                listener.onComplete(driveRides);
            }
        }
        DriveRideAsyncTask task = new DriveRideAsyncTask();
        task.execute();
    }


    static void insertAll(final List<DriveRide> driveRides, final DriveRideAsyncDaoListener<Boolean> listener){

        class DriveRideAsyncTask extends AsyncTask<List<DriveRide>,String,Boolean> {

            @Override
            protected Boolean doInBackground(List<DriveRide>... driveRides) {
                for (DriveRide dr:driveRides[0]) {
                    AppLocalDb.db.driveRideDao().insertAll(dr);
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }
        DriveRideAsyncTask task = new DriveRideAsyncTask();
        task.execute(driveRides);
    }

}
