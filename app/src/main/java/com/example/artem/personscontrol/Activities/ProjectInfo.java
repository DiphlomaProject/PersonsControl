package com.example.artem.personscontrol.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.artem.personscontrol.AspNet_Classes.Projects;
import com.example.artem.personscontrol.BaseActivity;
import com.example.artem.personscontrol.R;

public class ProjectInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        this.setTitle("Project Details");

        // При старте активности получить параметры из намерения
        Intent intent = getIntent();
        Projects project_info = (Projects) intent.getExtras().getSerializable("project_info");
    }
}
