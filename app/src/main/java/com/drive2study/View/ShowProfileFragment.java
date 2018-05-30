package com.drive2study.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.drive2study.MyApplication;
import com.drive2study.R;

public class ShowProfileFragment extends Fragment {

    public interface ShowProfileFragmentDelegate {
        void onEditProfileClicked();
    }

    public ShowProfileFragmentDelegate delegate;

    public ShowProfileFragment() {
        // Required empty public constructor
    }

    ImageView avatarImg;
    TextView nameTxt;
    TextView learningTxt;
    TextView daysList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_profile, container, false);

        avatarImg = view.findViewById(R.id.img_profile_avatar);
        nameTxt = view.findViewById(R.id.txt_profile_full_name);
        learningTxt = view.findViewById(R.id.txt_profile_learning);
        daysList = view.findViewById(R.id.txt_profile_days_list);

        String fullName = MyApplication.currentStudent.fName + " " + MyApplication.currentStudent.lName;
        nameTxt.setText(fullName);
        daysList.setText(daysAsStringBlock());

        Button editProfile = view.findViewById(R.id.btn_edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null){
                    delegate.onEditProfileClicked();
                }
            }
        });

        return view;
    }

    private String daysAsStringBlock() {
        boolean[] days = MyApplication.currentStudent.daysInCollege;
        String daysBlock = "";
        if (days[0]) daysBlock += "Sunday\n";
        if (days[1]) daysBlock += "Monday\n";
        if (days[2]) daysBlock += "Tuesday\n";
        if (days[3]) daysBlock += "Wednesday\n";
        if (days[4]) daysBlock += "Thursday\n";
        if (days[5]) daysBlock += "Friday\n";
        if (days[6]) daysBlock += "Saturday";

        return daysBlock;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowProfileFragmentDelegate) {
            delegate = (ShowProfileFragmentDelegate) context;
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
