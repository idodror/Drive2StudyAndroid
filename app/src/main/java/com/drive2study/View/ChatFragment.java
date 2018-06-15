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

import com.drive2study.Model.Objects.MessageDetails;
import com.drive2study.Model.Model;
import com.drive2study.MyApplication;
import com.drive2study.R;

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
        Model.instance.addChildAddedListener(new Model.GetMessageListener() {
            @Override
            public void onDone(MessageDetails msg) {
                String message = msg.getMessage();
                String userName = msg.getUsername();
                if(userName.equals(MyApplication.currentStudent.userName)){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    //addMessageBox(MessageDetails.chatWith + ":-\n" + message, 2);
                }
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
