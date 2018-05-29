package com.drive2study.View;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.drive2study.Model.Student;
import com.drive2study.MyApplication;
import com.drive2study.R;

public class EditProfileFragment extends Fragment {

    public interface EditProfileFragmentDelegate {
        void onSaveClicked(Student student);
    }

    public EditProfileFragmentDelegate delegate;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    EditText firstNameEt;
    EditText lastNameEt;
    EditText studyEt;
    CheckBox[] days = new CheckBox[7];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        firstNameEt = view.findViewById(R.id.edit_profile_et_first_name);
        lastNameEt = view.findViewById(R.id.edit_profile_et_last_name);
        studyEt = view.findViewById(R.id.edit_profile_et_study);
        setDaysCheckBoxex(view);

        firstNameEt.setText(MyApplication.currentStudent.fName);
        lastNameEt.setText(MyApplication.currentStudent.lName);
        studyEt.setText(MyApplication.currentStudent.study);
        for (int i = 0; i < 7; i++)
            if (MyApplication.currentStudent.daysInCollege[i])
                days[i].setChecked(true);

        Button save = view.findViewById(R.id.btn_save_edit_profile_changes);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student = new Student();
                student.fName = firstNameEt.getText().toString();
                student.lName = lastNameEt.getText().toString();
                student.study = studyEt.getText().toString();
                for (int i = 0; i < 7; i++)
                    if (days[i].isChecked())
                        student.daysInCollege[i] = true;
                if (delegate != null){
                    delegate.onSaveClicked(student);
                }
            }
        });

        return view;
    }

    private void setDaysCheckBoxex(View view) {
        days[0] = view.findViewById(R.id.cb_sunday);
        days[1] = view.findViewById(R.id.cb_monday);
        days[2] = view.findViewById(R.id.cb_tuesday);
        days[3] = view.findViewById(R.id.cb_wednesday);
        days[4] = view.findViewById(R.id.cb_thursday);
        days[5] = view.findViewById(R.id.cb_friday);
        days[6] = view.findViewById(R.id.cb_saturday);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditProfileFragmentDelegate) {
            delegate = (EditProfileFragmentDelegate) context;
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
