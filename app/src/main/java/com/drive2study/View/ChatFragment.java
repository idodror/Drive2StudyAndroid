package com.drive2study.View;


import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.drive2study.Model.MessageDetails;
import com.drive2study.Model.Model;
import com.drive2study.MyApplication;
import com.drive2study.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;


public class ChatFragment extends Fragment {

    public interface ChatFragmentDelegate{
    }

    public ChatFragmentDelegate delegate;

    public ChatFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activit_chat, container, false);
        return view;
    }

    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference;


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout = view.findViewById(R.id.layout1);
        layout_2 = view.findViewById(R.id.layout2);
        sendButton = view.findViewById(R.id.sendButton);
        messageArea = view.findViewById(R.id.messageArea);
        scrollView = view.findViewById(R.id.scrollView);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    MessageDetails newMsg = new MessageDetails();
                    newMsg.setMessage(messageText);
                    newMsg.setUsername(MyApplication.currentStudent.userName);
                    newMsg.setChatWith("amonmoris@gmail.com");
                    Model.instance.addMessage(newMsg);
                    messageArea.setText("");
                }
            }
        });

        Firebase.setAndroidContext(getContext());
        //TODO: fix! add event lisetener!
        reference = new Firebase("https://drivetostudyandorid.firebaseio.com/messages");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String message = dataSnapshot.getValue(MessageDetails.class).getMessage();
                String userName =dataSnapshot.getValue(MessageDetails.class).getUsername();

                if(userName.equals(MyApplication.currentStudent.userName)){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    //addMessageBox(MessageDetails.chatWith + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(getContext());
        textView.setText(message);
        textView.setTextSize(16f);
        textView.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

}
