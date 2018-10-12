package com.example.artem.personscontrol.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.example.artem.personscontrol.AspNet_Classes.Projects;
import com.example.artem.personscontrol.BaseActivity;
import com.example.artem.personscontrol.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ProjectInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        this.setTitle("Project Details");

        // При старте активности получить параметры из намерения
        Intent intent = getIntent();
        Projects project_info = (Projects) intent.getExtras().getSerializable("project_info");

        ((EditText) this.findViewById(R.id.title)).setText(project_info.title);
        ((EditText) this.findViewById(R.id.company)).setText(project_info.customer.company);
        ((EditText) this.findViewById(R.id.description)).setText(project_info.desc);
        ((EditText) this.findViewById(R.id.start)).setText(project_info.beginTime);
        ((EditText) this.findViewById(R.id.end)).setText(project_info.endTime);
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar cal = Calendar.getInstance();
            Date dt = dateFormat.parse( project_info.beginTime);
            Date dt2 = dateFormat.parse( project_info.endTime);
            ((EditText) this.findViewById(R.id.start)).setText(dateFormat.format(dt));
            ((EditText) this.findViewById(R.id.end)).setText(dateFormat.format(dt2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
