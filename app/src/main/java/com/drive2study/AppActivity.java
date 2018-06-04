package com.drive2study;

import android.app.Application;
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
import com.drive2study.View.AddPopupView;
import com.drive2study.View.DriveRideListFragment;
import com.drive2study.View.EditProfileFragment;
import com.drive2study.View.MapFragment;
import com.drive2study.View.PopupDialog;
import com.drive2study.View.ShowProfileFragment;

import java.util.List;

public class AppActivity extends AppCompatActivity implements
        MapFragment.MapFragmentDelegate, ShowProfileFragment.ShowProfileFragmentDelegate, EditProfileFragment.EditProfileFragmentDelegate, PopupDialog.PopupDialogDelegate, DriveRideListFragment.StudentsListFragmentDelegate,
        AddPopupView.AddPopupViewDelegate{

    private MapFragment mapFragment;
    private ShowProfileFragment showProfileFragment;
    private EditProfileFragment editProfileFragment;
    private FragmentManager fragmentManager;
    private DialogFragment popupDialog;
    private DialogFragment addDialog;
    private DriveRideListFragment driveListFragment;
    private DriveRideListFragment rideListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        Bundle argsDrive = new Bundle();
        Bundle argsRide = new Bundle();

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
                    argsDrive.putString("type", DriveRide.DRIVER);
                    driveListFragment.setArguments(argsDrive);
                    setFragment(driveListFragment);
                    break;
                case R.id.app_nav_item_ride:
                    argsRide.putString("type", DriveRide.RIDER);
                    rideListFragment.setArguments(argsRide);
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
            case R.id.action_add:

                FragmentTransaction ft = fragmentManager.beginTransaction();

                List<Fragment> fragmentList = fragmentManager.getFragments();
                DriveRideListFragment driveRideFragment = null;
                if(fragmentList.size()!=0) {
                    Fragment fragment = fragmentList.get(0);
                    if (fragment instanceof DriveRideListFragment)
                        driveRideFragment = (DriveRideListFragment)fragment;
                }
                String type=null;
                if(driveRideFragment!=null)
                    type = driveRideFragment.getArguments().getString("type");

                ft.addToBackStack(null);
                addDialog = new AddPopupView();
                Bundle args = new Bundle();
                if(type!=null)
                    args.putString("type", type);
                addDialog.setArguments(args);
                addDialog.show(ft, "dialog");
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
    public void onAddPopupClose() {
        addDialog.dismissAllowingStateLoss();
    }


    @Override
    public void onAddPopupOkClicked(String address, String type) {
        DriveRide newDriveRide = new DriveRide();
        newDriveRide.setUserName(MyApplication.currentStudent.userName.toString().replace(".",","));
        if(MyApplication.currentStudent.imageUrl != null)
            newDriveRide.setImageUrl(MyApplication.currentStudent.imageUrl);
        else
            newDriveRide.setImageUrl("");
        newDriveRide.setFromWhere(address);
        newDriveRide.setType(type);
        onAddPopupClose();
        Model.instance.addDriveRide(newDriveRide);
        Toast.makeText(AppActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(String studentId) {

    }
}
