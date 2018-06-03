package com.drive2study;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.drive2study.Model.DriveRide;
import com.drive2study.Model.Model;
import com.drive2study.Model.Student;
import com.drive2study.View.DriveRideListFragment;
import com.drive2study.View.EditProfileFragment;
import com.drive2study.View.MapFragment;
import com.drive2study.View.PopupDialog;
import com.drive2study.View.ShowProfileFragment;

public class AppActivity extends AppCompatActivity implements
        MapFragment.MapFragmentDelegate, ShowProfileFragment.ShowProfileFragmentDelegate, EditProfileFragment.EditProfileFragmentDelegate, PopupDialog.PopupDialogDelegate, DriveRideListFragment.StudentsListFragmentDelegate{

    private MapFragment mapFragment;
    private ShowProfileFragment showProfileFragment;
    private EditProfileFragment editProfileFragment;
    private FragmentManager fragmentManager;
    private DialogFragment popupDialog;
    private DriveRideListFragment driveListFragment;
    private DriveRideListFragment rideListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        mapFragment = new MapFragment();
        showProfileFragment = new ShowProfileFragment();
        editProfileFragment = new EditProfileFragment();
        fragmentManager = getSupportFragmentManager();
        driveListFragment = new DriveRideListFragment();
        rideListFragment = new DriveRideListFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.app_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.app_nav_item_map:
                    setFragment(mapFragment);
                    break;
                case R.id.app_nav_item_drive:
                    driveListFragment.setType("d");
                    setFragment(driveListFragment);
                    break;
                case R.id.app_nav_item_ride:
                    rideListFragment.setType("r");
                    setFragment(rideListFragment);
                    break;
                case R.id.app_nav_item_chat:
                    Toast.makeText(AppActivity.this, "Chat Clicked", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });

        if (savedInstanceState == null) {
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.add(R.id.main_app_content, mapFragment);
            tran.commit();
        }

        setTitle(MyApplication.currentStudent.userName);
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.main_app_content, fragment);
        tran.addToBackStack(null);
        tran.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_my_profile:
                setFragment(showProfileFragment);
                break;
        }
        return true;
    }

    @Override
    public void onEditProfileClicked() {
        setFragment(editProfileFragment);
    }

    @Override
    public void onSaveClicked(Student student) {

        // garbage values - until authentication will work
        student.loginType = "";
        student.userName ="10";
        student.imageUrl = "";

        Model.instance.addStudent(student);
        fragmentManager.popBackStack();
        Toast.makeText(AppActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerTap(DriveRide dr) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.addToBackStack(null);
        popupDialog = new PopupDialog();
        Bundle args = new Bundle();
        args.putString("username", dr.getUserName());
        args.putString("type", dr.getType());
        popupDialog.setArguments(args);
        popupDialog.show(ft, "dialog");
    }

    @Override
    public void onClose() {
        popupDialog.dismissAllowingStateLoss();
    }

    @Override
    public void onDriveOrRideClicked(String username) {
    }

    @Override
    public void onItemSelected(String studentId) {

    }
}
