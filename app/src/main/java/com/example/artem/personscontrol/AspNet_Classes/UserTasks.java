package com.example.artem.personscontrol.AspNet_Classes;

import com.example.artem.personscontrol.DataClasses.Data_Singleton;

import java.util.Map;

public class UserTasks {

    public int id;
    public String title;
    public String toUserId;
    public String fromUserId;
    public String desc;
    public String dateTimeBegin;
    public String dateTimeEnd;
    public Boolean isComplite;
    public User userFrom;
    public User userTo;

    public UserTasks(Map<String, Object> map){
        id = (Integer) map.get("Id");
        title = (String) map.get("title");
        toUserId = (String) map.get("toUserId");
        fromUserId = (String) map.get("fromUserId");
        desc = (String) map.get("description");
        dateTimeBegin = (String) map.get("dateTimeBegin");
        dateTimeEnd = (String) map.get("dateTimeEnd");
        isComplite = (Boolean) map.get("isComplite");

        userFrom = new User((Map<String, Object>) map.get("userFrom"));
        userTo = Data_Singleton.getInstance().currentUser;
    }
}
