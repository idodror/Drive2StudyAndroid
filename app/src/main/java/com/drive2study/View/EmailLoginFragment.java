package com.drive2study.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.drive2study.R;

public class EmailLoginFragment extends Fragment {

    private static final String ARG_EMAIL = "ARG_EMAIL";

    public interface EmailLoginFragmentDelegate {
        void onSignIn(String email, String password);
    }

    public EmailLoginFragmentDelegate delegate;

    public EmailLoginFragment() {
        // Required empty public constructor
    }

    EditText passwordEt;
    TextView userEmailText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email_login, container, false);

        userEmailText = view.findViewById(R.id.text_username);
        final String username = getArguments().getString("username");
        userEmailText.setText(username);
        passwordEt = view.findViewById(R.id.et_login_password);

        Button signIn = view.findViewById(R.id.btn_signin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEt.getText().toString();
                if (delegate != null){
                    delegate.onSignIn(username, password);
                }
            }
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
