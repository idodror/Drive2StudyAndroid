package com.drive2study.View;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.drive2study.AppActivity;
import com.drive2study.Model.DriveRide;
import com.drive2study.Model.Model;
import com.drive2study.R;

public class DriveRideListFragment extends Fragment {

    ListView list;
    DriveRideCellAdapter driveRideCellAdapter;
    private String type;

    public interface DriveRideListFragmentDelegate {
        void onListItemSelected(String username, String type);
    }

    public DriveRideListFragmentDelegate delegate;

    public DriveRideListFragment(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driveRideCellAdapter = new DriveRideCellAdapter(this.type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drivers_list, container, false);

        if (getArguments() != null)
            this.type = getArguments().getString("type");

        list = view.findViewById(R.id.drivers_list_list);
        list.setAdapter(driveRideCellAdapter);
        list.setOnItemClickListener((adapterView, view1, i, l) -> Log.d("TAG","item selected:" + i));

        AppActivity.dataModel = ViewModelProviders.of(this).get(DataViewModel.class);
        AppActivity.dataModel.getDriveRideListData().observe(this, driveRideList -> {
            if(driveRideList != null)
                driveRideList.removeIf(rec-> !rec.getType().equals(this.type));

            driveRideCellAdapter.notifyDataSetChanged();
            if (driveRideList != null)
                Log.d("TAG","notifyDataSetChanged" + driveRideList.size());
        });

        AppActivity.dataModel.getStudentsListData().observe(this, studentsList -> {
            driveRideCellAdapter.notifyDataSetChanged();
            if (studentsList != null)
                Log.d("TAG","notifyDataSetChanged" + studentsList.size());
        });

        list.setOnItemClickListener((AdapterView<?> parent, View view12, int position, long id) -> {
            String username = (((ConstraintLayout) view12).getViewById(R.id.drive_list_item_avatar_img)).getTag().toString();
            if (delegate != null)
                delegate.onListItemSelected(username, this.type);
        });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        if (getArguments() != null)
            this.type = getArguments().getString("type");
        if (context instanceof DriveRideListFragmentDelegate) {
            delegate = (DriveRideListFragmentDelegate) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Model.instance.cancelGetAllDriveRide();
        Model.instance.cancellGetAllStudents();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.toolbar_add_button).setVisible(true);
    }

    class DriveRideCellAdapter extends BaseAdapter {
        private String type;

        public DriveRideCellAdapter(String type) {
            this.type = type;
        }

        @Override
        public int getCount() {
            Log.d("TAG","list size:" + AppActivity.dataModel.getDriveRideListData().getValue().size());

            return AppActivity.dataModel.getDriveRideListData().getValue().size();
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
                view = LayoutInflater.from(getActivity()).inflate(R.layout.drive_list_item,null);

            final DriveRide dr = AppActivity.dataModel.getDriveRideListData().getValue().get(i);
            final ImageView avatarView = view.findViewById(R.id.drive_list_item_avatar_img);

            TextView studentName = view.findViewById(R.id.drive_list_item_name_txt);
            TextView fromWhere = view.findViewById(R.id.drive_list_item_from_where_txt);

            fromWhere.setText(dr.getFromWhere());
            avatarView.setImageResource(R.drawable.student_avatar);
            avatarView.setTag(dr.getUserName());

            Model.instance.getStudent(dr.getUserName(), student -> {
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
            });

            return view;
        }
    }

}
