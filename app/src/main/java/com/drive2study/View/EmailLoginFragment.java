package com.drive2study.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.drive2study.R;

public class EmailLoginFragment extends Fragment {

    public interface EmailLoginFragmentDelegate {
        void onSignIn(String email, String password);
        void onNotYourEmailAtLoginClicked();
        void onForgotPasswordClicked(String email);
    }

    public EmailLoginFragmentDelegate delegate;

    public EmailLoginFragment() {
        // Required empty public constructor
    }

    EditText passwordEt;
    TextView userEmailText;
    ProgressBar progressBar;

    public ProgressBar getProgressBar(){return progressBar;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email_login, container, false);

        userEmailText = view.findViewById(R.id.forgot_password_text_username);
        final String emailAddress = getArguments().getString("username");
        userEmailText.setText(emailAddress);
        passwordEt = view.findViewById(R.id.et_login_password);
        progressBar = view.findViewById(R.id.progress_login_bar);
        progressBar.setVisibility(View.GONE);


        Button signIn = view.findViewById(R.id.btn_signin);
        signIn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String password = passwordEt.getText().toString();
            if (delegate != null)
                delegate.onSignIn(emailAddress, password);
        });

        Button notYouEmailBtn = view.findViewById(R.id.login_btn_not_your_email);
        notYouEmailBtn.setOnClickListener(v -> {
            if (delegate != null)
                delegate.onNotYourEmailAtLoginClicked();
        });

        Button forgotPassBtn = view.findViewById(R.id.login_btn_forgot_password);
        forgotPassBtn.setOnClickListener(v -> {
            if (delegate != null)
                delegate.onForgotPasswordClicked(emailAddress);
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EmailLoginFragmentDelegate) {
            delegate = (EmailLoginFragmentDelegate) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        delegate = null;
    }
}
