package com.drive2study.View;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.drive2study.R;

public class LoginScreenFragment extends Fragment {

    public interface LoginScreenFragmentDelegate {
        void onConWithEmail(String email);
    }

    public LoginScreenFragmentDelegate delegate;

    public LoginScreenFragment() {
        // Required empty public constructor
    }

    EditText emailEt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_screen, container, false);

        emailEt = view.findViewById(R.id.et_user_email);

        Button continueWithEmail = view.findViewById(R.id.btn_email_login);
        continueWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString();
                if (delegate != null){
                    delegate.onConWithEmail(email);
                }
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
