package com.example.artem.personscontrol.DataClasses;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.artem.personscontrol.AspNet_Classes.Customers;
import com.example.artem.personscontrol.AspNet_Classes.GroupTasks;
import com.example.artem.personscontrol.AspNet_Classes.Groups;
import com.example.artem.personscontrol.AspNet_Classes.ProjectTasks;
import com.example.artem.personscontrol.AspNet_Classes.Projects;
import com.example.artem.personscontrol.AspNet_Classes.User;
import com.example.artem.personscontrol.AspNet_Classes.UserTasks;
import com.example.artem.personscontrol.NavigationActivity;
import com.example.artem.personscontrol.SupportLibrary.Network_connections;

import java.util.ArrayList;
import java.util.List;

public class Data_Singleton {

    public static Network_connections network_connections;
    public static String deviceFCMToken;
    public static Activity activity;

    // AspNet Classes with application information
    public User currentUser;

    //Groups & Projects
    //public ArrayList<Customers> customers = new ArrayList<>();
    public ArrayList<Groups> groups = new ArrayList<>();
    public ArrayList<Projects> projects = new ArrayList<>();

    //Tasks by personal, groups, projects
    public ArrayList<UserTasks> userTasks = new ArrayList<>();
    public ArrayList<GroupTasks> groupTasks = new ArrayList<>();
    public ArrayList<ProjectTasks> projectTasks = new ArrayList<>();

    public NavigationActivity navigationActivity;

    private static final Data_Singleton ourInstance = new Data_Singleton();

    public static Data_Singleton getInstance() {
        return ourInstance;
    }

    private Data_Singleton() {
        network_connections = new Network_connections();
        currentUser = new User();
    }

    public void remindFCMToken(String deviceFCMToken){
        Data_Singleton.deviceFCMToken = deviceFCMToken;
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FCMToken", deviceFCMToken);
        editor.apply();
    }

    public static void getDeviceFCMToken() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "";
        deviceFCMToken = sharedPref.getString("FCMToken", defaultValue);
    }
}
