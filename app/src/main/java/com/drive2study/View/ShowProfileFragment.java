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

import com.drive2study.AppActivity;
import com.drive2study.Model.Model;
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

        String fullName = MyApplication.currentStudent.getfName() + " " + MyApplication.currentStudent.getlName();
        String imgUrl = MyApplication.currentStudent.getImageUrl();
        nameTxt.setText(fullName);
        learningTxt.setText(MyApplication.currentStudent.getStudy());
        daysList.setText(AppActivity.daysAsStringBlock(MyApplication.currentStudent.getDaysInCollege()));

        if(imgUrl != null && !imgUrl.equals("")) {
            Model.instance.getImage(imgUrl, imageBitmap -> {
                if(imageBitmap != null)
                    avatarImg.setImageBitmap(imageBitmap);
            });
        }

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
