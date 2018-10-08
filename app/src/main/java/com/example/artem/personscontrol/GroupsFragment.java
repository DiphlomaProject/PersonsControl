package com.example.artem.personscontrol;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    public Context context;
    public static GroupsFragment sharedInstance() { return new GroupsFragment(); }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context = container.getContext();
        

        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

}
