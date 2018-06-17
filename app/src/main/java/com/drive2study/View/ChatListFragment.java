package com.drive2study.View;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.drive2study.AppActivity;
import com.drive2study.Model.Model;
import com.drive2study.Model.Objects.MessageDetails;
import com.drive2study.MyApplication;
import com.drive2study.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ChatListFragment extends Fragment {

    ListView list;
    ChatCellAdapter chatCellAdapter;

    public interface ChatListFragmentDelegate {
        void onChatListItemSelected(String username);
    }

    public ChatListFragmentDelegate delegate;

    public ChatListFragment(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatCellAdapter = new ChatCellAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        list = view.findViewById(R.id.chat_list_list);
        list.setAdapter(chatCellAdapter);

        AppActivity.dataModel = ViewModelProviders.of(this).get(DataViewModel.class);
        AppActivity.dataModel.getMessageDetailsListData().observe(this, (List<MessageDetails> chatList) -> {
            if(chatList.size()!=0) {
                chatList.removeIf(msg -> !(msg.getChatWith().replace(",",".").equals(MyApplication.currentStudent.getUserName())));
                List<MessageDetails> newList = deleteDuplicates(chatList);
                chatList.clear();
                chatList.addAll(newList);
                chatCellAdapter.notifyDataSetChanged();
            }
        });

        list.setOnItemClickListener((AdapterView<?> parent, View view12, int position, long id) -> {
            String username = (((ConstraintLayout) view12).getViewById(R.id.chat_list_item_name_txt)).getTag().toString();
            if (delegate != null)
                delegate.onChatListItemSelected(username);
        });

        return view;
    }

    private List<MessageDetails> deleteDuplicates(List<MessageDetails> chatList) {
        HashMap<String, MessageDetails> map = new HashMap<>();
        List<MessageDetails> newChatList = new LinkedList<>();
        MessageDetails msg;
        for(int i = 0; i < chatList.size(); i++){
            msg = chatList.get(i);
            if(msg != null) {
                if (!(map.containsKey(msg.getUsername()))) {
                    map.put(msg.getUsername(), msg);
                    newChatList.add(msg);
                }
            }
        }
        return newChatList;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChatListFragmentDelegate) {
            delegate = (ChatListFragmentDelegate) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Model.instance.cancelGetAllMessages();
    }


    class ChatCellAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return AppActivity.dataModel.getMessageDetailsListData().getValue().size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null)
                view = LayoutInflater.from(getActivity()).inflate(R.layout.chat_list_item,null);

            final MessageDetails msg = AppActivity.dataModel.getMessageDetailsListData().getValue().get(i);
            final ImageView avatarView = view.findViewById(R.id.chat_list_item_avatar_img);

            TextView studentName = view.findViewById(R.id.chat_list_item_name_txt);
            studentName.setText(msg.getUsername());
            studentName.setTag(msg.getUsername());

            avatarView.setImageResource(R.drawable.student_avatar);
            avatarView.setTag(msg.getUsername());

            return view;
        }
    }
}
