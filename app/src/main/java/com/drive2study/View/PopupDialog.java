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

import com.drive2study.Model.DriveRide;
import com.drive2study.R;

import java.util.Objects;

public class PopupDialog extends DialogFragment {

    public interface PopupDialogDelegate {
        void onClose();
        void onDriveOrRideClicked(String username);
    }

    public PopupDialogDelegate delegate;
    private String username;

    public PopupDialog() {
    }

    ImageView userImg;
    TextView fullNameTxt;
    TextView daysInCollegeTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_popup, container, false);
        getDialog().setTitle("Marker Details");

        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        userImg = view.findViewById(R.id.popup_img_avatar);
        fullNameTxt = view.findViewById(R.id.popup_txt_full_name);
        daysInCollegeTxt = view.findViewById(R.id.popup_txt_days_in_college_list);

        fullNameTxt.setText(username);

        Button closeBtn = view.findViewById(R.id.popup_btn_close);
        closeBtn.setOnClickListener(v -> {
            if (delegate != null)
                delegate.onClose();
        });

        Button driveOrRideBtn = view.findViewById(R.id.popup_btn_drive_or_ride);
        String driveOrRideButtonString = Objects.equals(getArguments().getString("type"), DriveRide.DRIVER) ? "Drive with" : "Ride with!";
        driveOrRideBtn.setText(driveOrRideButtonString);
        driveOrRideBtn.setOnClickListener(v -> {
            if (delegate != null)
                delegate.onDriveOrRideClicked(username);
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PopupDialogDelegate) {
            delegate = (PopupDialogDelegate) context;
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