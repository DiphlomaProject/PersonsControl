package com.example.artem.personscontrol.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.artem.personscontrol.AspNet_Classes.UserTasks;
import com.example.artem.personscontrol.R;

public class TaskInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        this.setTitle("Task Details");

        // При старте активности получить параметры из намерения
        Intent intent = getIntent();
        UserTasks project_info = (UserTasks) intent.getExtras().getSerializable("task_info");
    }
}
