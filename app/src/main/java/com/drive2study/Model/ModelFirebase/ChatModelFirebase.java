package com.drive2study.Model.ModelFirebase;

import com.drive2study.Model.Model;
import com.drive2study.Model.Objects.DriveRide;
import com.drive2study.Model.Objects.MessageDetails;
import com.drive2study.Model.Objects.Student;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChatModelFirebase {

    //region Data Members
    List<MessageDetails> chatList;
    private ValueEventListener eventMessagesListener;
    DatabaseReference msgDatabase = FirebaseDatabase.getInstance().getReference().child("messages");
    public String prevKey="";

    //endregion

    //region Interface Listener
    public interface GetAllMessagesListener{
        void onSuccess(List<MessageDetails> messagesList);
    }
    //endregion

    //region Action Methods
    public void addMessage(MessageDetails msg) {
        final Map<String, Object> dataMap = new HashMap<>();
        String  uniqueID = UUID.randomUUID().toString();
        dataMap.put(DateFormat.getDateTimeInstance().format(new Date())+uniqueID,msg.toMap());
        msgDatabase.updateChildren(dataMap);
    }

    public void addChildAddedListener(final Model.GetMessageListener listener){
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("messages");
        stRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!prevKey.equals(dataSnapshot.getKey())) {
                    prevKey = dataSnapshot.getKey();
                    String message = dataSnapshot.getValue(MessageDetails.class).getMessage();
                    String userName = dataSnapshot.getValue(MessageDetails.class).getUsername();
                    String chatWith = dataSnapshot.getValue(MessageDetails.class).getChatWith();
                    String type= dataSnapshot.getValue(MessageDetails.class).getType();
                    String date= dataSnapshot.getValue(MessageDetails.class).getDate();
                    MessageDetails msg = new MessageDetails();
                    msg.setMessage(message);
                    msg.setUsername(userName);
                    msg.setChatWith(chatWith);
                    msg.setType(type);
                    msg.setDate(date);
                    listener.onDone(msg);
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
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void getAllMessages(final GetAllMessagesListener listener) {
        eventMessagesListener = msgDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatList = new LinkedList<>();
                int index = 0;

                for (DataSnapshot msgSnapshot: dataSnapshot.getChildren()) {
                    MessageDetails msg = msgSnapshot.getValue(MessageDetails.class);
                    chatList.add(msg);

                }
                listener.onSuccess(chatList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void cancelGetAllMessages() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("messages");
        stRef.removeEventListener(eventMessagesListener);
    }
    //endregion
}
