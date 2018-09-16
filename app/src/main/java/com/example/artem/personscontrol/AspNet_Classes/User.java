package com.example.artem.personscontrol.AspNet_Classes;

import android.content.Context;
import android.content.SharedPreferences;

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
        if (jsonDict.get("token") != null)
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
        return  json;
    }

    public void save(Context context){
        saveToSharedPreferences(context, "key", id);
    }

    private void saveToSharedPreferences(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void loadFromSharedPreferences(){

    }
}
