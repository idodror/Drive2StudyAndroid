package com.drive2study.View;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.drive2study.R;
import com.drive2study.Model.Student;

public class CreateAccountFragment extends Fragment {
    public interface CreateAccountFragmentDelegate {
        void onJoin(Student student);
    }

    public CreateAccountFragmentDelegate delegate;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    EditText fNameEt;
    EditText lNameEt;
    EditText studyEt;
    EditText passwordEt;
    TextView userEmailText;
    TextView emptyFieldsText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        final String username = getArguments().getString("username");
        userEmailText = view.findViewById(R.id.text_username_register);
        userEmailText.setText(username);
        fNameEt = view.findViewById(R.id.et_first_name);
        lNameEt = view.findViewById(R.id.et_last_name);
        studyEt = view.findViewById(R.id.et_study);
        passwordEt = view.findViewById(R.id.et_register_password);
        emptyFieldsText = view.findViewById(R.id.txt_empty_fields);

        Button join = view.findViewById(R.id.btn_join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student = new Student();
                String password = passwordEt.getText().toString();
                student.userName = username;
                student.fName = fNameEt.getText().toString();
                student.lName = lNameEt.getText().toString();
                student.study = studyEt.getText().toString();
                if (student.study.equals("") || student.fName.equals("") || student.study.equals("") || password.equals("")) {
                    emptyFieldsText.setVisibility(View.VISIBLE);
                } else {
                    student.imageUrl = "";
                    student.loginType = "RegularLogin";
                    if (delegate != null) {
                        delegate.onJoin(student);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateAccountFragmentDelegate) {
            delegate = (CreateAccountFragmentDelegate) context;
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
