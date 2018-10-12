package com.example.artem.personscontrol.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.artem.personscontrol.AspNet_Classes.Groups;
import com.example.artem.personscontrol.R;

public class GroupInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        this.setTitle("Group Details");

        // При старте активности получить параметры из намерения
        Intent intent = getIntent();
        Groups group_info = (Groups) intent.getExtras().getSerializable("group_info");
    }
}
