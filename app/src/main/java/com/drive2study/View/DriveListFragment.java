package com.drive2study.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.drive2study.Model.Student;
import com.drive2study.R;

import java.util.LinkedList;
import java.util.List;

public class DriveListFragment extends Fragment {

    ListView list;
    MyAdapter myAdapter;
    List<Student> data = new LinkedList<Student>();


    public interface StudentsListFragmentDelegate{
        void onItemSelected(String studentId);
    }

    public StudentsListFragmentDelegate delegate;

    public DriveListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAdapter = new MyAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drivers_list, container, false);
        list = view.findViewById(R.id.drivers_list_list);
        list.setAdapter(myAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(delegate != null){
                    Student s = data.get(i);
                    delegate.onItemSelected(s.fName+" "+s.lName);
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StudentsListFragmentDelegate) {
            delegate = (StudentsListFragmentDelegate) context;
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

    class MyAdapter extends BaseAdapter {
        public MyAdapter() {
            for (int i = 0; i < 100; i++) {
                Student s = new Student();
                s.fName = "fname " + i;
                s.userName = "lname" + i;
                data.add(s);
            }
        }

        @Override
        public int getCount() {
            return data.size();
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

            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.drive_list_item, null);
            }

            final Student s = data.get(i);

            TextView userName = view.findViewById(R.id.driveListItem_name);
            TextView fromWhere = view.findViewById(R.id.driveListItem_fromWhere);


            userName.setText(s.fName + " " + s.lName);
            fromWhere.setText("TBD");
            return view;
        }
    }

}
