package com.example.artem.personscontrol.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.artem.personscontrol.AspNet_Classes.GroupTasks;
import com.example.artem.personscontrol.AspNet_Classes.Groups;
import com.example.artem.personscontrol.R;

public class GroupTaskInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_task_info);

        this.setTitle("Group Task Details");

        // При старте активности получить параметры из намерения
        Intent intent = getIntent();
        GroupTasks group_info = (GroupTasks) intent.getExtras().getSerializable("group_task_info");
    }
}
