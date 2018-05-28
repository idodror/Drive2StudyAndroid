package com.drive2study;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.drive2study.View.MapFragment;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        BottomNavigationView bottomNavigationView = findViewById(R.id.app_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.app_nav_item_map:
                        Toast.makeText(AppActivity.this, "Map Clicked", Toast.LENGTH_SHORT).show();
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
            MapFragment fragment = new MapFragment();
            FragmentTransaction tran = getFragmentManager().beginTransaction();
            tran.add(R.id.main_app_content, fragment);
            tran.commit();
        }
    }
}
