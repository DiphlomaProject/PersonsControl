package com.example.artem.personscontrol.DataClasses;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.artem.personscontrol.AspNet_Classes.Customers;
import com.example.artem.personscontrol.AspNet_Classes.Groups;
import com.example.artem.personscontrol.AspNet_Classes.Projects;
import com.example.artem.personscontrol.AspNet_Classes.User;
import com.example.artem.personscontrol.NavigationActivity;
import com.example.artem.personscontrol.SupportLibrary.Network_connections;

import java.util.List;

public class Data_Singleton {

    public static Network_connections network_connections;
    public static String deviceFCMToken;
    public static Activity activity;

    // AspNet Classes with application information
    public User currentUser;
    public List<Customers> customers;
    public List<Groups> groups;
    public List<Projects> projects;

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
