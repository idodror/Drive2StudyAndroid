package com.drive2study.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.drive2study.Model.GPSTracker;
import com.drive2study.R;
import com.google.android.gms.maps.model.LatLng;

public class AddDriverRiderPopupDialog extends DialogFragment {

    public interface AddDriverRiderPopupDialogDelegate {
        void onClose(DialogFragment dialogFragment);
        void onAddPopupOkClicked(String address, String type, LatLng gpsCoordinates);
    }

    public AddDriverRiderPopupDialogDelegate delegate;

    @SuppressLint("ValidFragment")
    public AddDriverRiderPopupDialog() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_popup_add_driver_rider, container, false);
        getDialog().setTitle("Add Your Self!");

        TextView addressTextView = view.findViewById(R.id.textAddressView);

        Button closeBtn = view.findViewById(R.id.popup_btn_close);
        closeBtn.setOnClickListener(v -> {
            if (delegate != null)
                delegate.onClose(this);
        });

        Button okBtn = view.findViewById(R.id.add_popup_button);
        okBtn.setOnClickListener(v -> {
            if (delegate != null) {
                if (delegate != null)
                    // check if GPS or address
                    if (!addressTextView.getText().toString().equals(""))
                        delegate.onAddPopupOkClicked(addressTextView.getText().toString(), getArguments().getString("type"), null);
                    else {
                        GPSTracker gps = new GPSTracker(getContext());
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        delegate.onAddPopupOkClicked(null, getArguments().getString("type"), new LatLng(latitude, longitude));
                    }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddDriverRiderPopupDialogDelegate) {
            delegate = (AddDriverRiderPopupDialogDelegate) context;
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
