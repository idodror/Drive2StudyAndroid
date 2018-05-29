package com.drive2study;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.drive2study.Model.Model;
import com.drive2study.Model.Student;
import com.drive2study.View.CreateAccountFragment;
import com.drive2study.View.EmailLoginFragment;
import com.drive2study.View.LoginScreenFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends Activity implements
        LoginScreenFragment.LoginScreenFragmentDelegate, EmailLoginFragment.EmailLoginFragmentDelegate, CreateAccountFragment.CreateAccountFragmentDelegate {

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

        Bundle args = new Bundle();
        args.putString("username", email);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null) {
            EmailLoginFragment emailLoginFragment = new EmailLoginFragment();
            emailLoginFragment.setArguments(args);
            transaction.replace(R.id.main_container, emailLoginFragment);
        } else {
            CreateAccountFragment createAccountFragment = new CreateAccountFragment();
            createAccountFragment.setArguments(args);
            transaction.replace(R.id.main_container, createAccountFragment);
        }

        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onConWithFacebook() {
        MyApplication.currentStudent.study = "Computer Science";
        MyApplication.currentStudent.userName = "ido@bla.com";
        MyApplication.currentStudent.fName = "Ido";
        MyApplication.currentStudent.lName = "Dror";
        MyApplication.currentStudent.daysInCollege[2] = true;
        startActivity(new Intent(MainActivity.this, AppActivity.class));
    }

    @Override
    public void onSignIn(String email, String password) {
        Log.d("TAG", "Signing in with: " + email + ", pass: " + password);
    }

    @Override
    public void onJoin(Student student) {
        Log.d("TAG", "done");
        Model.instance.addStudent(student);
    }
}