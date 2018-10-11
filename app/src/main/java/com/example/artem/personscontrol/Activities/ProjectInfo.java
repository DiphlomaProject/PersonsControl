package com.example.artem.personscontrol.Activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.artem.personscontrol.BaseActivity;
import com.example.artem.personscontrol.R;

public class ProjectInfo extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        this.setTitle("Project Details");

    }
}
