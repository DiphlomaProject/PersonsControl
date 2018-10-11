package com.example.artem.personscontrol.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.artem.personscontrol.Activities.ProjectInfo;
import com.example.artem.personscontrol.Adapters.Adapter_Projects;
import com.example.artem.personscontrol.AspNet_Classes.Projects;
import com.example.artem.personscontrol.DataClasses.Data_Singleton;
import com.example.artem.personscontrol.NavigationActivity;
import com.example.artem.personscontrol.R;
import com.example.artem.personscontrol.SignIn;

import java.util.ArrayList;

public class ProjectsFragment extends Fragment {


    //public static ProjectsFragment sharedInstance() { return new ProjectsFragment(); }

    View view;
    GridView gridViewAllGroups;
    Adapter_Projects adapterGroups;
    ArrayList<Projects> linesGroups;

    protected FragmentActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_projects, container, false);
        setHasOptionsMenu(true);//Make sure you have this line of code.

        gridViewAllGroups = view.findViewById(R.id.grid_view_projects);
        linesGroups = Data_Singleton.getInstance().projects;
        this.DrowGroups();


        return view;
        //return inflater.inflate(R.layout.fragment_tasks_groups, container, false);
    }

    public void DrowGroups(){
        if(linesGroups.isEmpty()) return;


        adapterGroups = new Adapter_Projects(this.getContext(), linesGroups, this);

        gridViewAllGroups.setAdapter(adapterGroups);

        adapterGroups.notifyDataSetChanged();
        //adapterGroups.invalidate();
        //((BaseAdapter) mMyListView.getAdapter()).notifyDataSetChanged();

        //Log.d("RENDER", "RENDER USERS");
        //gridViewAllUsers.setBackgroundColor(Color.parseColor("#ff0000"));

    }


    public void ShowSelectedUserInfo(Projects group){
        Projects templUser = new Projects();
        templUser.id = group.id;
        templUser.title = group.title;
        templUser.customeId = group.customeId;
        templUser.customer = group.customer;
        templUser.desc = group.desc;
        templUser.isComplite = group.isComplite;
        templUser.beginTime = group.beginTime;
        templUser.endTime = group.endTime;

        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), ProjectInfo.class);
            intent.putExtra("project_info", templUser);
            startActivity(intent);
        }

    }

}
