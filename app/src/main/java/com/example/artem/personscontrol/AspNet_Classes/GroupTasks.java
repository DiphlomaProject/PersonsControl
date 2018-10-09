package com.example.artem.personscontrol.AspNet_Classes;

import java.util.Map;

public class GroupTasks {

    public int id;
    public String title;
    public String toGroupId;
    public String fromUserId;
    public String desc;
    public String dateTimeBegin;
    public String dateTimeEnd;
    public Boolean isComplite;
    public String groupName;
    public User userFrom;

    public GroupTasks(Map<String, Object> map){
        id = (Integer) map.get("Id");
        title = (String) map.get("title");
        toGroupId = (String) map.get("toGroupId");
        fromUserId = (String) map.get("fromUserId");
        desc = (String) map.get("description");
        dateTimeBegin = (String) map.get("dateTimeBegin");
        dateTimeEnd = (String) map.get("dateTimeEnd");
        isComplite = (Boolean) map.get("isComplite");
        groupName = (String) map.get("groupName");

//        userFrom "userFrom";
    }

}
