package com.example.artem.personscontrol.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.artem.personscontrol.AspNet_Classes.ProjectTasks;
import com.example.artem.personscontrol.AspNet_Classes.UserTasks;
import com.example.artem.personscontrol.R;

public class ProjectTaskInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_task_info);

        this.setTitle("Project Task Details");

        // При старте активности получить параметры из намерения
        Intent intent = getIntent();
        ProjectTasks project_info = (ProjectTasks) intent.getExtras().getSerializable("project_task_info");
    }
}
