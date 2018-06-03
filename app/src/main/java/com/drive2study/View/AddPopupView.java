package com.drive2study.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.drive2study.R;

public class AddPopupView extends DialogFragment {

    public interface AddPopupViewDelegate {
        void onClose();

        void onDriveOrRideClicked(String username);
    }

    public AddPopupViewDelegate delegate;
    private String messageData;

    public AddPopupView() {
    }

    TextView messageTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_dialog_popup, container, false);
        getDialog().setTitle("Add Your Self!");

        if (getArguments() != null) {
            messageData = getArguments().getString("message");

        }

        messageTextView = view.findViewById(R.id.add_popup_textView);

        messageTextView.setText(" ");

        Button closeBtn = view.findViewById(R.id.no_button);
        closeBtn.setOnClickListener(v -> {
            if (delegate != null)
                delegate.onClose();
        });

        Button okBtn = view.findViewById(R.id.yes_button);
        okBtn.setOnClickListener(v -> {
            if (delegate != null) {
                if (delegate != null)
                    delegate.onDriveOrRideClicked(messageData);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddPopupViewDelegate) {
            delegate = (AddPopupViewDelegate) context;
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
