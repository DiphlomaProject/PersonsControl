package com.example.artem.personscontrol.AspNet_Classes;

import java.util.Map;

public class Groups {

    public int id;
    public String title;
    public int ownerId;
    public String desc;
    public User ownerInfo;

    public Groups(Map<String, Object> map){
        id  = (int) map.get("Id");
        title = (String) map.get("Title");
        ownerId = (int) map.get("Owner");
        desc = (String) map.get("Description");
    }
}
