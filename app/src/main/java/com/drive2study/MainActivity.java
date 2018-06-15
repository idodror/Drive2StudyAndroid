package com.drive2study;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.drive2study.Model.Objects.Student;
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
    FirebaseAuth auth;
    Context contextCompat;
    LoginScreenFragment loginScreenFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        auth = FirebaseAuth.getInstance();
        contextCompat = getBaseContext();

        getPermissionsIfNeeded();

        MyApplication.sharedPref.edit().putBoolean("logout", false).apply();
        MyApplication.sharedPref.edit().putBoolean("exit", false).apply();

        Model.instance.getStudentCred(studentCred -> {
            if (studentCred != null)
                onSignIn(studentCred.getEmail(), studentCred.getPassword());
            else showLoginScreen();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.sharedPref.getBoolean("logout", false)) {
            Model.instance.clearStudentCred();
            showLoginScreen();
        }
        if (MyApplication.sharedPref.getBoolean("exit", false)) {
            finish();
            moveTaskToBack(true);
        }
    }

    public void showLoginScreen() {
        if(loginScreenFragment == null)
            loginScreenFragment = new LoginScreenFragment();
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.add(R.id.main_container, loginScreenFragment);
        tran.commit();
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
        Model.instance.userExists(email.replace(".",","), result -> {
            if(FirebaseAuth.getInstance().getCurrentUser() != null)

                Model.instance.getStudent(email, student -> {
                    loginAndSet(student);
                });
            else if (result)
                setArgAndTranFragment(new EmailLoginFragment(), email);
            else setArgAndTranFragment(new CreateAccountFragment(), email);
        });
    }

    private void setArgAndTranFragment(Fragment nextFrag, String email){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putString("username", email);

        nextFrag.setArguments(args);
        transaction.replace(R.id.main_container, nextFrag);
        transaction.addToBackStack("firstAppScreen");
        transaction.commit();
    }

    @Override
    public void onConWithFacebook() {
        Model.instance.getStudent("test2@gmail,com", new Model.GetStudentListener() {
            @Override
            public void onDone(Student student) {
                loginAndSet(student);
            }
        });
    }

    private void loginAndSet(Student student) {
        MyApplication.currentStudent = student;
        startActivity(new Intent(MainActivity.this, AppActivity.class));
    }

    @Override
    public void onSignIn(String email, String password) {
        ProgressBar progress = null;

        if (fragmentManager.getFragments().size() > 0) {
            Fragment frag = fragmentManager.getFragments().get(0);
            if (frag instanceof EmailLoginFragment)
                progress = ((EmailLoginFragment) frag).getProgressBar() ;
        }

        Log.d("TAG", "Signing in with: " + email + ", pass: " + password);
        ProgressBar finalProgress = progress;
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("TAG", "Success");
                Model.instance.setUserCred(email, password);
                Model.instance.getStudent(email.replace(".",","), student -> {
                    MyApplication.currentStudent = student;
                    if(finalProgress != null)
                        finalProgress.setVisibility(View.GONE);
                    startActivity(new Intent(MainActivity.this, AppActivity.class));
                });
            }
            else{
                Log.d("TAG", "Failure");
                if(finalProgress != null)
                    finalProgress.setVisibility(View.GONE);
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