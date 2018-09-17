package com.example.artem.personscontrol.AspNet_Classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.artem.personscontrol.DataClasses.Data_Singleton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    public String id;
    public String token;
    public String displayName;
    public String email;
    public String phone;
    public String country;
    public String city;
    public String address;
    public String isEmailVerify;
    public String isPhoneVerify;
    public String roleId;

    public User() {

    }

    public User(Map<String, Object> jsonDict, String token) {
        if (jsonDict.get("Id") != null)
            id = jsonDict.get("Id").toString();
        if (token != null)
            this.token = token;
        if (jsonDict.get("DisplayName") != null)
            displayName = jsonDict.get("DisplayName").toString();
        if (jsonDict.get("Email") != null)
            email = jsonDict.get("Email").toString();
        if (jsonDict.get("PhoneNumber") != null)
            phone = jsonDict.get("PhoneNumber").toString();
        if (jsonDict.get("Country") != null)
            country = jsonDict.get("Country").toString();
        if (jsonDict.get("City") != null)
            city = jsonDict.get("City").toString();
        if (jsonDict.get("Address") != null)
            address = jsonDict.get("Address").toString();
        if (jsonDict.get("EmailConfirmed") != null)
            isEmailVerify = jsonDict.get("EmailConfirmed").toString();
        if (jsonDict.get("PhoneNumberConfirmed") != null)
            isPhoneVerify = jsonDict.get("PhoneNumberConfirmed").toString();
        if (jsonDict.get("Roles") != null)
            roleId = ((Map<String, Object>)((ArrayList) jsonDict.get("Roles")).get(0)).get("RoleId").toString();
    }

    public Map<String, String> jsonAspNetUser(){
        Map<String, String> json = new HashMap<String, String>();
        json.put("Id", id);
        json.put("token", token);
        json.put("DisplayName", displayName);
        json.put("Email", email);
        json.put("PhoneNumber", phone);
        json.put("Country", country);
        json.put("City", city);
        json.put("Address", address);
        json.put("EmailConfirmed", isEmailVerify);
        json.put("PhoneNumberConfirmed", isPhoneVerify);
        json.put("FCMToken", Data_Singleton.deviceFCMToken);
        //json.put("Roles", roleId);
        return  json;
    }

    public Map<String, String> jsonUser(){
        Map<String, String> json = new HashMap<String, String>();
        json.put("id", id);
        json.put("token", token);
        json.put("displayName", displayName);
        json.put("email", email);
        json.put("phone", phone);
        json.put("country", country);
        json.put("city", city);
        json.put("address", address);
        json.put("emailConfirmed", isEmailVerify);
        json.put("phoneConfirmed", isPhoneVerify);
        json.put("roleId", roleId);
        json.put("FCMToken", Data_Singleton.deviceFCMToken);
        return  json;
    }

    public void saveSharedPreferences(final Activity context){
        for (String key: jsonUser().keySet()) {
            if (!key.equals("FCMToken"))
                saveToSharedPreferences(context, key, jsonAspNetUser().get(key));
        }

    }

    private void saveToSharedPreferences(final Activity activity, String key, String value){
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public boolean loadSharedPreferences(final Activity activity){
        loadFromSharedPreferences(activity);
        if(!token.isEmpty() && !email.isEmpty() && !id.isEmpty())
            return true;
        return false;
    }

    private void loadFromSharedPreferences(final Activity activity){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "";
        this.id = sharedPref.getString("id", defaultValue);
        this.token = sharedPref.getString("token", defaultValue);
        this.displayName = sharedPref.getString("displayName", defaultValue);
        this.email = sharedPref.getString("email", defaultValue);
        this.phone = sharedPref.getString("phone", defaultValue);
        this.country = sharedPref.getString("country", defaultValue);
        this.city = sharedPref.getString("city", defaultValue);
        this.address = sharedPref.getString("address", defaultValue);
        this.isEmailVerify = sharedPref.getString("emailConfirmed", defaultValue);
        this.roleId = sharedPref.getString("roleId", defaultValue);
    }

    public boolean isInforationValid(){
        if(token != null && email != null && id != null &&
                !token.isEmpty() && !email.isEmpty() && !id.isEmpty())
            return true;
        return false;
    }
}
