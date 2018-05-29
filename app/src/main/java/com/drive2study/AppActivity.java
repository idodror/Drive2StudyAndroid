package com.drive2study;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.drive2study.View.EditProfileFragment;
import com.drive2study.View.MapFragment;
import com.drive2study.View.ShowProfileFragment;

public class AppActivity extends AppCompatActivity implements
        MapFragment.MapFragmentDelegate, ShowProfileFragment.ShowProfileFragmentDelegate, EditProfileFragment.EditProfileFragmentDelegate {

    private MapFragment mapFragment;
    private ShowProfileFragment showProfileFragment;
    private EditProfileFragment editProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        mapFragment = new MapFragment();
        showProfileFragment = new ShowProfileFragment();
        editProfileFragment = new EditProfileFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.app_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.app_nav_item_map:
                        setFragment(mapFragment);
                        break;
                    case R.id.app_nav_item_drive:
                        Toast.makeText(AppActivity.this, "Drive Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.app_nav_item_ride:
                        Toast.makeText(AppActivity.this, "Ride Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.app_nav_item_chat:
                        Toast.makeText(AppActivity.this, "Chat Clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        if (savedInstanceState == null) {
            FragmentTransaction tran = getFragmentManager().beginTransaction();
            tran.add(R.id.main_app_content, mapFragment);
            tran.commit();
        }

        setTitle(MyApplication.currentStudent.userName);
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction tran = getFragmentManager().beginTransaction();
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
                //Toast.makeText(AppActivity.this, "My Profile Clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onEditProfileClicked() {
        setFragment(editProfileFragment);
    }
}
