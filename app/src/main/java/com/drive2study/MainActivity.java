package com.drive2study;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.drive2study.Model.Model;
import com.drive2study.Model.Student;
import com.drive2study.View.CreateAccountFragment;
import com.drive2study.View.EmailLoginFragment;
import com.drive2study.View.ForgotPasswordFragment;
import com.drive2study.View.LoginScreenFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements
        LoginScreenFragment.LoginScreenFragmentDelegate,
        EmailLoginFragment.EmailLoginFragmentDelegate,
        CreateAccountFragment.CreateAccountFragmentDelegate,
        ForgotPasswordFragment.ForgotPasswordFragmentDelegate {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            LoginScreenFragment fragment = new LoginScreenFragment();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.add(R.id.main_container, fragment);
            tran.commit();
        }
    }

    @Override
    public void onConWithEmail(String email) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment nextFrag;
        Bundle args = new Bundle();
        args.putString("username", email);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null)
            nextFrag = new EmailLoginFragment();
        else nextFrag = new CreateAccountFragment();

        nextFrag.setArguments(args);
        transaction.replace(R.id.main_container, nextFrag);
        transaction.addToBackStack("firstAppScreen");
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
    public void onNotYourEmailAtLoginClicked() {
        fragmentManager.popBackStack();
    }

    @Override
    public void onForgotPasswordClicked(String email) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
        Bundle args = new Bundle();
        args.putString("username", email);
        forgotPasswordFragment.setArguments(args);
        transaction.replace(R.id.main_container, forgotPasswordFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onJoin(Student student) {
        Model.instance.addStudent(student);
    }

    @Override
    public void onNotYourEmailAtRegisterPressed() {
        fragmentManager.popBackStack();
    }

    @Override
    public void onSendNow() {
        // TODO: send Firebase reset password email

        Toast.makeText(this, "Reset password email sent", Toast.LENGTH_SHORT).show();
        fragmentManager.popBackStack();
        fragmentManager.popBackStack();
    }

    @Override
    public void onTrySignInAgain() {
        fragmentManager.popBackStack();
        fragmentManager.popBackStack();
    }
}