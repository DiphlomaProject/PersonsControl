package com.example.artem.personscontrol.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.artem.personscontrol.Activities.TaskInfo;
import com.example.artem.personscontrol.Adapters.Adapter_Tasks;
import com.example.artem.personscontrol.AspNet_Classes.UserTasks;
import com.example.artem.personscontrol.DataClasses.Data_Singleton;
import com.example.artem.personscontrol.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment {


//    public static TasksFragment sharedInstance() { return new TasksFragment(); }


    View view;
    GridView gridViewAllTasks;
    Adapter_Tasks adapterTasks;
    ArrayList<UserTasks> linesTasks;

    protected FragmentActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tasks, container, false);
        setHasOptionsMenu(true);//Make sure you have this line of code.

        gridViewAllTasks = view.findViewById(R.id.grid_view_tasks);
        linesTasks = Data_Singleton.getInstance().userTasks;
        this.DrowGroups();


        return view;
        //return inflater.inflate(R.layout.fragment_tasks_groups, container, false);
    }

    public void DrowGroups(){
        if(linesTasks.isEmpty()) return;


        adapterTasks = new Adapter_Tasks(this.getContext(), linesTasks, this);

        gridViewAllTasks.setAdapter(adapterTasks);

        adapterTasks.notifyDataSetChanged();
        //adapterTasks.invalidate();
        //((BaseAdapter) mMyListView.getAdapter()).notifyDataSetChanged();

        //Log.d("RENDER", "RENDER USERS");
        //gridViewAllUsers.setBackgroundColor(Color.parseColor("#ff0000"));

    }


    public void ShowSelectedUserInfo(UserTasks task){
        UserTasks templUser = new UserTasks();
        templUser.id = task.id;
        templUser.title = task.title;
        templUser.fromUserId = task.fromUserId;
        templUser.userFrom = task.userFrom;
        templUser.desc = task.desc;
        templUser.isComplite = task.isComplite;
        templUser.dateTimeBegin = task.dateTimeBegin;
        templUser.dateTimeEnd = task.dateTimeEnd;

        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), TaskInfo.class);
            intent.putExtra("task_info", templUser);
            startActivity(intent);
        }

    }

}
