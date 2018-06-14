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

import java.util.EventListener;

public class ExitPopupDialog extends DialogFragment {

    public interface ExitPopupDialogDelegate {
        void onClose(DialogFragment dialogFragment);
        void onYesExit();
    }

    public ExitPopupDialogDelegate delegate;

    @SuppressLint("ValidFragment")
    public ExitPopupDialog() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_popup_exit, container, false);
        getDialog().setTitle("Exit?");

        View.OnClickListener listener = v -> {
            if (delegate != null)
                delegate.onClose(this);
        };

        Button closeBtn = view.findViewById(R.id.exit_popup_btn_close);
        Button noBtn = view.findViewById(R.id.no_button);

        closeBtn.setOnClickListener(listener);
        noBtn.setOnClickListener(listener);

        Button yesButton = view.findViewById(R.id.yes_button);
        yesButton.setOnClickListener(v -> {
            if (delegate != null)
                delegate.onYesExit();
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExitPopupDialogDelegate) {
            delegate = (ExitPopupDialogDelegate) context;
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
