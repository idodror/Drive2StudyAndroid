package com.drive2study.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.drive2study.Model.Model;
import com.drive2study.Model.Objects.Student;
import com.drive2study.MyApplication;
import com.drive2study.R;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;

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
    Button changeAvatar;
    ImageView avatar;
    CheckBox[] days = new CheckBox[7];
    Bitmap imageBitmap;
    boolean imageChanged;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        firstNameEt = view.findViewById(R.id.edit_profile_et_first_name);
        lastNameEt = view.findViewById(R.id.edit_profile_et_last_name);
        studyEt = view.findViewById(R.id.edit_profile_et_study);
        changeAvatar = view.findViewById(R.id.btn_edit_profile_change_avatar);
        avatar = view.findViewById(R.id.edit_profile_avatar);

        setDaysCheckBoxex(view);

        firstNameEt.setText(MyApplication.currentStudent.fName);
        lastNameEt.setText(MyApplication.currentStudent.lName);
        studyEt.setText(MyApplication.currentStudent.study);

        imageChanged = false;

        String url = MyApplication.currentStudent.getImageUrl();
        if(url != null && !url.equals("")) {
            Model.instance.getImage(url, imageBitmapFromDB -> {
                if(imageBitmapFromDB != null)
                    avatar.setImageBitmap(imageBitmapFromDB);
            });
        }

        for (int i = 0; i < 7; i++)
            if (MyApplication.currentStudent.daysInCollege[i])
                days[i].setChecked(true);

        Button save = view.findViewById(R.id.btn_save_edit_profile_changes);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Student student = new Student();
                student.userName = MyApplication.currentStudent.userName;
                student.loginType = MyApplication.currentStudent.loginType;
                student.fName = firstNameEt.getText().toString();
                student.lName = lastNameEt.getText().toString();
                student.study = studyEt.getText().toString();
                student.imageUrl = MyApplication.currentStudent.getImageUrl();

                for (int i = 0; i < 7; i++)
                    if (days[i].isChecked())
                        student.daysInCollege[i] = true;
                //save image
                if (delegate != null) {
                    if (imageBitmap != null && imageChanged) {
                        Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
                            @Override
                            public void onDone(String url) {
                                //save student obj
                                student.setImageUrl(url);
                                delegate.onSaveClicked(student);
                            }
                        });
                    } else delegate.onSaveClicked(student);
                }
            }
        });

        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera
                Intent takePictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            avatar.setImageBitmap(imageBitmap);
            imageChanged = true;
        }
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
