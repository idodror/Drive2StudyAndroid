package com.drive2study.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.drive2study.AppActivity;
import com.drive2study.Model.Objects.DriveRide;
import com.drive2study.Model.Model;
import com.drive2study.MyApplication;
import com.drive2study.R;

import java.util.Objects;

public class MarkerClickPopupDialog extends DialogFragment {

    public interface MarkerClickPopupDialogDelegate {
        void onClose(DialogFragment dialogFragment);
        void onDriveOrRideClicked(String username, String type);
    }

    public MarkerClickPopupDialogDelegate delegate;
    private String username;

    public MarkerClickPopupDialog() {
    }

    ImageView userImg;
    TextView fullNameTxt;
    TextView daysInCollegeTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_popup_marker_click, container, false);
        getDialog().setTitle("Marker Details");

        if (getArguments() != null)
            username = getArguments().getString("username");

        userImg = view.findViewById(R.id.popup_img_avatar);
        fullNameTxt = view.findViewById(R.id.popup_txt_full_name);
        daysInCollegeTxt = view.findViewById(R.id.popup_txt_days_in_college_list);

        GetStudentData(username);

        Button closeBtn = view.findViewById(R.id.popup_btn_close);
        closeBtn.setOnClickListener(v -> {
            if (delegate != null)
                delegate.onClose(this);
        });

        Button driveOrRideBtn = view.findViewById(R.id.popup_btn_drive_or_ride);
        if (!MyApplication.currentStudent.getUserName().equals(username.replace(",","."))) {
            String driveOrRideButtonString = Objects.equals(getArguments().getString("type"), DriveRide.DRIVER) ? "Ride with!" : "Drive with!";
            driveOrRideBtn.setText(driveOrRideButtonString);
            driveOrRideBtn.setOnClickListener(v -> {
                if (delegate != null)
                    delegate.onDriveOrRideClicked(username, getArguments().getString("type"));
            });
        } else driveOrRideBtn.setVisibility(View.INVISIBLE);

        return view;
    }

    private void GetStudentData(String username) {
        Model.instance.getStudent(username, student -> {
            String studentName = student.getfName() + " " + student.getlName();
            fullNameTxt.setText(studentName);
            daysInCollegeTxt.setText(AppActivity.daysAsStringBlock(student.getDaysInCollege()));
            if (student.getImageUrl() != null) {
                String url = student.getImageUrl();
                if (!url.equals("")) {
                    Model.instance.getImage(url, imageBitmap -> {
                        if (imageBitmap != null) {
                            userImg.setImageBitmap(imageBitmap);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MarkerClickPopupDialogDelegate) {
            delegate = (MarkerClickPopupDialogDelegate) context;
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
