package com.drive2study.View;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.drive2study.R;

public class ForgotPasswordFragment extends Fragment {

    public interface ForgotPasswordFragmentDelegate {
        void onSendNow();
        void onTrySignInAgain();
    }

    public ForgotPasswordFragmentDelegate delegate;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    TextView userEmailText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        userEmailText = view.findViewById(R.id.forgot_password_text_username);
        if (getArguments() != null)
            userEmailText.setText(getArguments().getString("username"));

        Button sendNow = view.findViewById(R.id.btn_send_reset_pass_code);
        sendNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null)
                    delegate.onSendNow();
            }
        });

        Button trySignInAgain = view.findViewById(R.id.forgot_pass_btn_try_signin_again);
        trySignInAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null)
                    delegate.onTrySignInAgain();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ForgotPasswordFragmentDelegate) {
            delegate = (ForgotPasswordFragmentDelegate) context;
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
