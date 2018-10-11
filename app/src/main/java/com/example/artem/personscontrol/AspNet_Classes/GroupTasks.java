package com.example.artem.personscontrol.AspNet_Classes;

import java.io.Serializable;
import java.util.Map;

public class GroupTasks implements Serializable {

    public int id;
    public String title;
    public int toGroupId;
    public String fromUserId;
    public String desc;
    public String dateTimeBegin;
    public String dateTimeEnd;
    public Boolean isComplite;
    //public String groupName;
    public User userFrom;
    public Groups group;

    public GroupTasks(Map<String, Object> map){
        id = (Integer) map.get("Id");
        title = (String) map.get("title");
        toGroupId = (Integer) map.get("toGroupId");
        fromUserId = (String) map.get("fromUserId");
        desc = (String) map.get("description");
        dateTimeBegin = (String) map.get("dateTimeBegin");
        dateTimeEnd = (String) map.get("dateTimeEnd");
        isComplite = (Boolean) map.get("isComplite");
        //groupName = (String) map.get("groupName");

        userFrom = new User((Map<String, Object>) map.get("userFrom"));
    }

}
