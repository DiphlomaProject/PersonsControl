package com.example.artem.personscontrol.AspNet_Classes;

import java.util.Map;

public class ProjectTasks {

    public int id;
    public String title;
    public String toProjectId;
    public String fromUserId;
    public String desc;
    public String dateTimeBegin;
    public String dateTimeEnd;
    public Boolean isComplite;
    public String projectName;
    public User userFrom;

    public ProjectTasks(Map<String, Object> map){
        id = (Integer) map.get("Id");
        title = (String) map.get("title");
        toProjectId = (String) map.get("toProjectId");
        fromUserId = (String) map.get("fromUserId");
        desc = (String) map.get("description");
        dateTimeBegin = (String) map.get("dateTimeBegin");
        dateTimeEnd = (String) map.get("dateTimeEnd");
        isComplite = (Boolean) map.get("isComplite");
        projectName = (String) map.get("projectName");

//        userFrom "userFrom";
    }

}
