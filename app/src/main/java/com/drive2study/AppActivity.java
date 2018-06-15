package com.drive2study;

import android.location.Address;
import android.location.Geocoder;
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

import com.drive2study.Model.Objects.DriveRide;
import com.drive2study.Model.Model;
import com.drive2study.Model.Objects.MessageDetails;
import com.drive2study.Model.Objects.Student;
import com.drive2study.View.AddDriverRiderPopupDialog;
import com.drive2study.View.ChatFragment;
import com.drive2study.View.ChatListFragment;
import com.drive2study.View.DriveRideListFragment;
import com.drive2study.View.DataViewModel;
import com.drive2study.View.EditProfileFragment;
import com.drive2study.View.ExitPopupDialog;
import com.drive2study.View.MapFragment;
import com.drive2study.View.MarkerClickPopupDialog;
import com.drive2study.View.ShowProfileFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class AppActivity extends AppCompatActivity implements
        MapFragment.MapFragmentDelegate,
        ShowProfileFragment.ShowProfileFragmentDelegate,
        EditProfileFragment.EditProfileFragmentDelegate,
        MarkerClickPopupDialog.MarkerClickPopupDialogDelegate,
        DriveRideListFragment.DriveRideListFragmentDelegate,
        AddDriverRiderPopupDialog.AddDriverRiderPopupDialogDelegate,
        ChatFragment.ChatFragmentDelegate,
        ExitPopupDialog.ExitPopupDialogDelegate,
        ChatListFragment.ChatListFragmentDelegate{

    private BottomNavigationView bottomNavigationView;
    private MapFragment mapFragment;
    private ShowProfileFragment showProfileFragment;
    private EditProfileFragment editProfileFragment;
    private FragmentManager fragmentManager;
    private DialogFragment markerClickPopupDialog;
    private DialogFragment addDriverRiderPopupDialog;
    private DialogFragment exitPopupDialog;
    private DriveRideListFragment driveListFragment;
    private DriveRideListFragment rideListFragment;
    private ChatFragment chatFragment;
    private ChatListFragment chatListFragment;
    public static DataViewModel dataModel;

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
        chatFragment = new ChatFragment();
        chatListFragment = new ChatListFragment();

        bottomNavigationView = findViewById(R.id.app_nav_bar);
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
                    setFragment(chatListFragment);
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
        //tran.addToBackStack(null);
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
            case R.id.toolbar_add_button:
                openAddDriverRiderPopup();
                break;
            case R.id.toolbar_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                MyApplication.sharedPref.edit().putBoolean("logout", true).apply();
                break;
        }
        return true;
    }

    private void openAddDriverRiderPopup() {
        FragmentTransaction ft = fragmentManager.beginTransaction();

        List<Fragment> fragmentList = fragmentManager.getFragments();
        DriveRideListFragment driveRideFragment = null;
        if (fragmentList.size() != 0) {
            Fragment fragment = fragmentList.get(0);
            if (fragment instanceof DriveRideListFragment)
                driveRideFragment = (DriveRideListFragment) fragment;
        }

        String type = null;
        if (driveRideFragment != null && driveRideFragment.getArguments() != null)
            type = driveRideFragment.getArguments().getString("type");

        ft.addToBackStack(null);
        addDriverRiderPopupDialog = new AddDriverRiderPopupDialog();

        Bundle args = new Bundle();
        if (type != null)
            args.putString("type", type);

        addDriverRiderPopupDialog.setArguments(args);
        addDriverRiderPopupDialog.show(ft, "dialog");
    }

    @Override
    public void onEditProfileClicked() {
        setFragment(editProfileFragment);
    }

    @Override
    public void onSaveClicked(Student student) {
        Model.instance.addStudent(student);
        MyApplication.currentStudent = student;
        setFragment(showProfileFragment);
        Toast.makeText(AppActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerTap(DriveRide dr) {
        openPopupToRideOrDrive(dr.getUserName(), dr.getType());
    }

    private void openPopupToRideOrDrive(String username, String type) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.addToBackStack(null);
        markerClickPopupDialog = new MarkerClickPopupDialog();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("type", type);
        markerClickPopupDialog.setArguments(args);
        markerClickPopupDialog.show(ft, "dialog");
    }

    @Override
    public void onClose(DialogFragment dialogFragment) {
        dialogFragment.dismissAllowingStateLoss();
    }



    @Override
    public void onDriveOrRideClicked(String username,String type) {
        MessageDetails msg = new MessageDetails();
        msg.setUsername(MyApplication.currentStudent.getUserName());
        msg.setChatWith(username);
        msg.setType(type);
        if(type.equals(DriveRide.DRIVER)) {
            msg.setMessage("Hello, would you like a ride to college?");
        }
        else{
            msg.setMessage("Hello, can I have a ride to college?");
        }
        Model.instance.addMessage(msg);
    }

    @Override
    public void onYesExit() {
        finish();
        moveTaskToBack(false);
        MyApplication.sharedPref.edit().putBoolean("exit", true).apply();
    }

    @Override
    public void onAddPopupOkClicked(String address, String type, LatLng gpsCoordinates) {
        DriveRide dr = new DriveRide();
        dr.setUserName(MyApplication.currentStudent.userName.replace(".",","));
        dr.setType(type);

        // address sent
        if (address != null) {
            dr.setFromWhere(address);
            addNewDriveRideAsyncWithAddress(dr, address);
        }
        else {  // GPS coordinates sent
            dr.setCoordinates(gpsCoordinates);
            addNewDriveRideAsyncWithLatLng(dr, gpsCoordinates);
        }
        onClose(addDriverRiderPopupDialog);
        Toast.makeText(AppActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
    }

    private void addNewDriveRideAsyncWithAddress(DriveRide dr, String address) {
        CompletableFuture.supplyAsync( () -> generateLatLngFromAddress(address))
                .thenApply((LatLng latLng) -> {
                    if (latLng != null)
                        dr.setCoordinates(latLng);
                    return 1;
                } )
                .thenAccept( answer -> {
                    Model.instance.addDriveRide(dr);
                });
    }

    private void addNewDriveRideAsyncWithLatLng(DriveRide dr, LatLng latLng) {
        CompletableFuture.supplyAsync( () -> generateAddressFromLatLng(latLng))
            .thenApply((String str) -> {
                if (str != null)
                    dr.setFromWhere(str);
                return 1;
            } )
            .thenAccept( answer -> {
                Model.instance.addDriveRide(dr);
            });
    }

    private String generateAddressFromLatLng(LatLng latLng) {
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        List<Address> address = new ArrayList<>();
        try {
            address = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String yourAddress = null;
        if (address.size() > 0) {
            yourAddress = address.get(0).getAddressLine(0);
        }
        return yourAddress;
    }

    private LatLng generateLatLngFromAddress(String address) {
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        LatLng latLng;
        try {
            List<Address> addresses = geoCoder.getFromLocationName(address , 1);
            if (addresses.size() > 0) {
                latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                return latLng;
            }
        }
        catch(Exception e) { return null; }
        return null;
    }

    @Override
    public void onBackPressed() {
        if (bottomNavigationView.getSelectedItemId () != R.id.app_nav_item_map) {
            bottomNavigationView.setSelectedItemId(R.id.app_nav_item_map);
        } else if (bottomNavigationView.getSelectedItemId () == R.id.app_nav_item_map && !mapFragment.isVisible()) {
            bottomNavigationView.setSelectedItemId(R.id.app_nav_item_map);
        } else {
            // the user see the map
            openExitPopup();
            //super.onBackPressed();
        }
    }

    private void openExitPopup() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.addToBackStack(null);
        exitPopupDialog = new ExitPopupDialog();
        exitPopupDialog.show(ft, "dialog");
    }

    public static String daysAsStringBlock(boolean[] days) {
        String daysBlock = "";
        if (days[0]) daysBlock += "Sunday\n";
        if (days[1]) daysBlock += "Monday\n";
        if (days[2]) daysBlock += "Tuesday\n";
        if (days[3]) daysBlock += "Wednesday\n";
        if (days[4]) daysBlock += "Thursday\n";
        if (days[5]) daysBlock += "Friday\n";
        if (days[6]) daysBlock += "Saturday";

        return daysBlock;
    }

    @Override
    public void onListItemSelected(String username, String type) {
        openPopupToRideOrDrive(username, type);
    }

    @Override
    public void onChatListItemSelected(String username) {
        Bundle args = new Bundle();
        args.putString("username",username);
        chatFragment.setArguments(args);
        setFragment(chatFragment);
    }
}
