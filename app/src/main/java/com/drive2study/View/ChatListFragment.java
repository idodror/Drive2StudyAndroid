package com.drive2study.View;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.drive2study.AppActivity;
import com.drive2study.Model.Objects.MessageDetails;
import com.drive2study.R;

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
        AppActivity.dataModel.getMessageDetailsListData().observe(this, chatList -> {
            chatCellAdapter.notifyDataSetChanged();
        });

        return view;
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
        //Model.instance.cancelGetAllDriveRide();
        //Model.instance.cancellGetAllStudents();
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
            avatarView.setImageResource(R.drawable.student_avatar);
            avatarView.setTag(msg.getUsername());

            /*Model.instance.getStudent(msg.getUsername(), student -> {
                if (student.getImageUrl() != null) {
                    String name = student.getfName() + " " + student.getlName();
                    studentName.setText(name);
                    String url = student.getImageUrl();
                    if (!url.equals("")) {
                        Model.instance.getImage(url, imageBitmap -> {
                            if (dr.getUserName().equals(avatarView.getTag()) && imageBitmap != null) {
                                avatarView.setImageBitmap(imageBitmap);
                            }
                        });
                    }
                }
            });*/

            return view;
        }
    }
}
