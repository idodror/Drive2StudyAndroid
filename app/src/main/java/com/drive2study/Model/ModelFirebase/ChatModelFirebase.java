package com.drive2study.Model.ModelFirebase;

import com.drive2study.Model.Objects.MessageDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChatModelFirebase {

    //region Data Members
    private ValueEventListener eventMessagesListener;
    DatabaseReference msgDatabase = FirebaseDatabase.getInstance().getReference().child("messages");
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
        dataMap.put(uniqueID,msg.toMap());
        msgDatabase.updateChildren(dataMap);
    }


    public void getAllMessages(final GetAllMessagesListener listener) {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("messages");

        eventMessagesListener = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MessageDetails> msgList = new LinkedList<>();

                for (DataSnapshot stSnapshot: dataSnapshot.getChildren()) {
                    MessageDetails msg = stSnapshot.getValue(MessageDetails.class);
                    msgList.add(msg);
                }
                listener.onSuccess(msgList);
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
