package com.example.artem.personscontrol.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.artem.personscontrol.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment {


    public static TasksFragment sharedInstance() { return new TasksFragment(); }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

}
