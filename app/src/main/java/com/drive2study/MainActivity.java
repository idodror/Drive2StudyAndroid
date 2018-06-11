package com.drive2study;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.drive2study.Model.Model;
import com.drive2study.Model.Student;
import com.drive2study.View.CreateAccountFragment;
import com.drive2study.View.EmailLoginFragment;
import com.drive2study.View.ForgotPasswordFragment;
import com.drive2study.View.LoginScreenFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements
        LoginScreenFragment.LoginScreenFragmentDelegate,
        EmailLoginFragment.EmailLoginFragmentDelegate,
        CreateAccountFragment.CreateAccountFragmentDelegate,
        ForgotPasswordFragment.ForgotPasswordFragmentDelegate {

    FragmentManager fragmentManager;
    FirebaseAuth auth;
    Context contextCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        auth = FirebaseAuth.getInstance();
        contextCompat = getBaseContext();

        getPermissionsIfNeeded();

        if (savedInstanceState == null) {
            LoginScreenFragment fragment = new LoginScreenFragment();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.add(R.id.main_container, fragment);
            tran.commit();
        }
    }

    private void getPermissionsIfNeeded() {
        int check;
        String[] permissions = { android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                 android.Manifest.permission.ACCESS_FINE_LOCATION,
                                 android.Manifest.permission.ACCESS_COARSE_LOCATION };

        for (String permission : permissions) {
            check = ActivityCompat.checkSelfPermission(this, permission);
            if (check != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{ permission },1024);
        }
    }

    @Override
    public void onConWithEmail(String email) {


        Model.instance.userExists(email.replace(".",","), new Model.GetUserExistsListener() {
            @Override
            public void onDone(boolean result) {

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("username", email);
                Fragment nextFrag;

                if (result)
                    nextFrag = new EmailLoginFragment();
                else nextFrag = new CreateAccountFragment();

                nextFrag.setArguments(args);
                transaction.replace(R.id.main_container, nextFrag);
                transaction.addToBackStack("firstAppScreen");
                transaction.commit();
            }
        });

    }

    @Override
    public void onConWithFacebook() {
        Model.instance.getStudent("test2@gmail,com", new Model.GetStudentListener() {
            @Override
            public void onDone(Student student) {
                MyApplication.currentStudent = student;
                startActivity(new Intent(MainActivity.this, AppActivity.class));
            }
        });
    }

    @Override
    public void onSignIn(String email, String password) {
        ProgressBar progress = ((EmailLoginFragment)getSupportFragmentManager().getFragments().get(0)).getProgressBar();
        Log.d("TAG", "Signing in with: " + email + ", pass: " + password);
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("TAG", "Success");
                   Model.instance.getStudent(email.replace(".",","), new Model.GetStudentListener() {
                        @Override
                        public void onDone(Student student) {
                            MyApplication.currentStudent = student;
                            progress.setVisibility(View.GONE);
                            startActivity(new Intent(MainActivity.this, AppActivity.class));
                        }
                    });
                }
                else{
                    Log.d("TAG", "Failure");
                    progress.setVisibility(View.GONE);

                }
            }
        });
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
    public void onJoin(Student student,String password) {

        auth.createUserWithEmailAndPassword(student.userName,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Model.instance.addStudent(student);
                onSignIn(student.userName, password);
            }
        });

    }

    @Override
    public void onNotYourEmailAtRegisterPressed() {
        fragmentManager.popBackStack();
    }

    @Override
    public void onSendNow(String email) {

        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                Log.d("Reset Password", "Email sent.");
        });

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