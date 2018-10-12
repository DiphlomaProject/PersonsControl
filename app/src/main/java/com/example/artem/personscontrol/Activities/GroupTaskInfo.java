package com.example.artem.personscontrol.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.artem.personscontrol.AspNet_Classes.GroupTasks;
import com.example.artem.personscontrol.AspNet_Classes.Groups;
import com.example.artem.personscontrol.BaseActivity;
import com.example.artem.personscontrol.DataClasses.Data_Singleton;
import com.example.artem.personscontrol.R;
import com.example.artem.personscontrol.SupportLibrary.Network_connections;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.example.artem.personscontrol.SupportLibrary.Network_connections.CompleteTaskGroup_Action;
import static com.example.artem.personscontrol.SupportLibrary.Network_connections.CompleteTaskPersonal_Action;
import static com.example.artem.personscontrol.SupportLibrary.Network_connections.CompleteTaskProject_Action;

public class GroupTaskInfo extends BaseActivity {

    GroupTasks group_info;
    Context context = this;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_task_info);

        this.setTitle("Group Task Details");

        // При старте активности получить параметры из намерения
        Intent intent = getIntent();
        group_info = (GroupTasks) intent.getExtras().getSerializable("group_task_info");

        ((EditText) this.findViewById(R.id.title)).setText(group_info.title);
        ((EditText) this.findViewById(R.id.description)).setText(group_info.desc);
        ((EditText) this.findViewById(R.id.fromUser)).setText(group_info.userFrom.displayName);
        ((EditText) this.findViewById(R.id.end)).setText(group_info.dateTimeEnd);
        ((EditText) this.findViewById(R.id.start)).setText(group_info.dateTimeBegin);
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar cal = Calendar.getInstance();
            Date dt = dateFormat.parse( group_info.dateTimeBegin);
            Date dt2 = dateFormat.parse( group_info.dateTimeEnd);
            ((EditText) this.findViewById(R.id.start)).setText(dateFormat.format(dt));
            ((EditText) this.findViewById(R.id.end)).setText(dateFormat.format(dt2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ((Button) this.findViewById(R.id.compliteButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Network_connections network_connections = new Network_connections();
                network_connections.CompleteTask(context, Data_Singleton.getInstance().currentUser.token, Integer.toString(group_info.id), CompleteTaskGroup_Action);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.menu_accept_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_accept_task:
                Network_connections network_connections = new Network_connections();
                network_connections.CompleteTask(context, Data_Singleton.getInstance().currentUser.token, Integer.toString(group_info.id), CompleteTaskGroup_Action);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
