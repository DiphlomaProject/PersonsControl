package com.example.artem.personscontrol.DataClasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.artem.personscontrol.AspNet_Classes.User;
import com.example.artem.personscontrol.SupportLibrary.Network_connections;

public class Data_Singleton {

    public static String baseURL;
    public static Network_connections network_connections;
    public static String deviceFCMToken;
    public static Activity activity;

    // AspNet Classes with application information
    public User currentUser;

    private static final Data_Singleton ourInstance = new Data_Singleton();

    public static Data_Singleton getInstance() {
        return ourInstance;
    }

    private Data_Singleton() {
        baseURL = "https://178.209.88.110:443/";
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

    public static String getDeviceFCMToken() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "";
        deviceFCMToken = sharedPref.getString("FCMToken", defaultValue);
        return deviceFCMToken;
    }
}
