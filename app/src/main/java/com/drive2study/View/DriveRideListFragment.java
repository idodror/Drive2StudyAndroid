package com.drive2study.View;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.drive2study.Model.DriveRide;
import com.drive2study.Model.Model;
import com.drive2study.R;
import java.util.List;

public class DriveRideListFragment extends Fragment {

    ListView list;
    MyAdapter myAdapter;
    DriveRideListViewModel dataModel;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public interface StudentsListFragmentDelegate{
        void onItemSelected(String studentId);
    }

    public StudentsListFragmentDelegate delegate;

    public DriveRideListFragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAdapter = new MyAdapter(this.type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drivers_list, container, false);

        list = view.findViewById(R.id.drivers_list_list);
        list.setAdapter(myAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG","item selected:" + i);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataModel = ViewModelProviders.of(this).get(DriveRideListViewModel.class);
        dataModel.getData().observe(this, driveRideList -> {
            if(driveRideList.size()!=0)
                for (DriveRide dr : driveRideList)
                    if (!dr.getType().equals(this.type))
                        driveRideList.remove(dr);

            myAdapter.notifyDataSetChanged();
            Log.d("TAG","notifyDataSetChanged" + driveRideList.size());
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Model.instance.cancellGetAllStudents();
    }

    class MyAdapter extends BaseAdapter {
        private String type;
        public MyAdapter(String type) {
            this.type=type;
        DriveRide dr = new DriveRide();
        dr.setUserName("Moris Amon");
            dr.setFromWhere("agnon");
            dr.setType(DriveRide.DRIVER);
            dr.setImageUrl("http");
            Model.instance.addDriveRide(dr);
            dr.setUserName("idoooo");
            dr.setFromWhere("dsdsdssdds");
            dr.setType(DriveRide.RIDER);
            dr.setImageUrl("sssss");
            Model.instance.addDriveRide(dr);

        }

        @Override
        public int getCount() {
            Log.d("TAG","list size:" + dataModel.getData().getValue().size());

            return dataModel.getData().getValue().size();
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

            if (view == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.drive_list_item,null);

            }

            final DriveRide dr_rd = dataModel.getData().getValue().get(i);

            TextView userName = view.findViewById(R.id.driveListItem_name);
            TextView fromWhere = view.findViewById(R.id.driveListItem_fromWhere);
            final ImageView avatarView = view.findViewById(R.id.driveListItem_avatar);

            userName.setText(dr_rd.getUserName());
            fromWhere.setText(dr_rd.getFromWhere());
            avatarView.setImageResource(R.drawable.student_avatar);
            avatarView.setTag(dr_rd.getUserName());
            /*if (dr_rd.getImageUrl() != null){
                Model.instance.getImage(dr_rd.getImageUrl(), new Model.GetImageListener() {
                    @Override
                    public void onDone(Bitmap imageBitmap) {
                        if (dr_rd.getUserName().equals(avatarView.getTag()) && imageBitmap != null) {
                            avatarView.setImageBitmap(imageBitmap);
                        }
                    }
                });
            }*/
            return view;
        }
    }

}
