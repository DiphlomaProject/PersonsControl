package com.example.artem.personscontrol.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.artem.personscontrol.Activities.GroupInfo;
import com.example.artem.personscontrol.Adapters.Adapter_Groups;
import com.example.artem.personscontrol.AspNet_Classes.Groups;
import com.example.artem.personscontrol.DataClasses.Data_Singleton;
import com.example.artem.personscontrol.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    public Context context;
//    public static GroupsFragment sharedInstance() { return new GroupsFragment(); }

    View view;
    GridView gridViewAllGroups;
    Adapter_Groups adapterGroups;
    ArrayList<Groups> linesGroups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_groups, container, false);
        //setHasOptionsMenu(true);//Make sure you have this line of code.

        gridViewAllGroups = view.findViewById(R.id.grid_view_groups);
        linesGroups = Data_Singleton.getInstance().groups;
        this.DrowGroups();


        return view;
        //return inflater.inflate(R.layout.fragment_tasks_groups, container, false);
    }

    public void DrowGroups(){
        if(linesGroups.isEmpty()) return;


        adapterGroups = new Adapter_Groups(this.getContext(), linesGroups, this);

        gridViewAllGroups.setAdapter(adapterGroups);

        adapterGroups.notifyDataSetChanged();
        //adapterGroups.invalidate();
        //((BaseAdapter) mMyListView.getAdapter()).notifyDataSetChanged();

        //Log.d("RENDER", "RENDER USERS");
        //gridViewAllUsers.setBackgroundColor(Color.parseColor("#ff0000"));

    }

    public void ShowSelectedUserInfo(Groups group){
        Groups templUser = new Groups();
        templUser.id = group.id;
        //templUser.photoBMP = user_firebase.photoBMP;
        templUser.title = group.title;
        templUser.ownerId = group.ownerId;
        templUser.ownerInfo = group.ownerInfo;
        templUser.desc = group.desc;

        Intent intent = new Intent(getContext(), GroupInfo.class);
        intent.putExtra("group_info", templUser);
        startActivity(intent);
    }

}
