package com.drive2study.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.drive2study.R;

import java.util.regex.Matcher;

public class LoginScreenFragment extends Fragment {

    public interface LoginScreenFragmentDelegate {
        void onConWithEmail(String email);
        void onConWithFacebook();
    }

    public LoginScreenFragmentDelegate delegate;

    public LoginScreenFragment() {
        // Required empty public constructor
    }

    EditText emailEt;
    TextView validateEmailTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_screen, container, false);

        emailEt = view.findViewById(R.id.et_user_email);
        validateEmailTxt = view.findViewById(R.id.label_valid_email_required);

        Button continueWithEmail = view.findViewById(R.id.btn_email_login);
        continueWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString();

              /*  Matcher m = Patterns.EMAIL_ADDRESS.matcher(email);
                if (!m.matches())    // check validation of email
                    validateEmailTxt.setVisibility(View.VISIBLE);
                else if (delegate != null) {
                    delegate.onConWithEmail(email);
                }*/
                delegate.onConWithEmail(email);
            }
        });

        Button continueWithFacebook = view.findViewById(R.id.btn_facebook);
        continueWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null)
                    delegate.onConWithFacebook();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginScreenFragmentDelegate) {
            delegate = (LoginScreenFragmentDelegate) context;
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

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("username", emailEt.getText().toString());
    }
}
