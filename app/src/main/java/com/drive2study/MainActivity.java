package com.drive2study;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.drive2study.View.EmailLoginFragment;
import com.drive2study.View.LoginScreenFragment;


public class MainActivity extends Activity implements
        LoginScreenFragment.LoginScreenFragmentDelegate, EmailLoginFragment.EmailLoginFragmentDelegate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            LoginScreenFragment fragment = new LoginScreenFragment();
            FragmentTransaction tran = getFragmentManager().beginTransaction();
            tran.add(R.id.main_container, fragment);
            tran.commit();
        }
    }

    @Override
    public void onConWithEmail(String email) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        EmailLoginFragment emailLoginFrag = new EmailLoginFragment();
        Bundle args = new Bundle();
        args.putString("username", email);
        emailLoginFrag.setArguments(args);
        transaction.replace(R.id.main_container, emailLoginFrag);
        transaction.addToBackStack(null);
        transaction.commit();
        Log.d("TAG", "Login Screen Succeeded " + email);
    }

    @Override
    public void onSignIn(String email, String password) {
        Log.d("TAG", "Signing in with: " + email + ", pass: " + password);
    }
}