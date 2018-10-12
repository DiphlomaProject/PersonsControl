package com.example.artem.personscontrol.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.artem.personscontrol.Activities.GroupTaskInfo;
import com.example.artem.personscontrol.Adapters.Adapter_Groups;
import com.example.artem.personscontrol.Adapters.Adapter_GroupsTasks;
import com.example.artem.personscontrol.AspNet_Classes.GroupTasks;
import com.example.artem.personscontrol.AspNet_Classes.Groups;
import com.example.artem.personscontrol.DataClasses.Data_Singleton;
import com.example.artem.personscontrol.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksGroupsFragment extends Fragment {


//    public static TasksGroupsFragment sharedInstance() { return new TasksGroupsFragment(); }

    View view;
    GridView gridViewAllGroups;
    Adapter_GroupsTasks adapterGroups;
    ArrayList<GroupTasks> linesGroups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tasks_groups, container, false);
        setHasOptionsMenu(true);//Make sure you have this line of code.

        gridViewAllGroups = view.findViewById(R.id.grid_view_tasks_groups);
        linesGroups = Data_Singleton.getInstance().groupTasks;
        this.DrowGroups();


        return view;
        //return inflater.inflate(R.layout.fragment_tasks_groups, container, false);
    }

    public void DrowGroups(){
        if(linesGroups.isEmpty()) return;


        adapterGroups = new Adapter_GroupsTasks(this.getContext(), linesGroups, this);

        gridViewAllGroups.setAdapter(adapterGroups);

        adapterGroups.notifyDataSetChanged();
        //adapterGroups.invalidate();
        //((BaseAdapter) mMyListView.getAdapter()).notifyDataSetChanged();

        //Log.d("RENDER", "RENDER USERS");
        //gridViewAllUsers.setBackgroundColor(Color.parseColor("#ff0000"));

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void ShowSelectedUserInfo(GroupTasks group){
        GroupTasks templUser = new GroupTasks();
        templUser.id = group.id;
        //templUser.photoBMP = user_firebase.photoBMP;
        templUser.title = group.title;
        templUser.group = group.group;
        templUser.fromUserId = group.fromUserId;
        templUser.userFrom = group.userFrom;
        templUser.dateTimeBegin = group.dateTimeBegin;
        templUser.dateTimeEnd = group.dateTimeEnd;
        templUser.desc = group.desc;

        Intent intent = new Intent(getContext(), GroupTaskInfo.class);
        intent.putExtra("group_task_info", templUser);
        startActivity(intent);
    }
}
