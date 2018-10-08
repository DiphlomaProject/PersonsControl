package com.example.artem.personscontrol.AspNet_Classes;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Projects {

    public int id;
    public int customeId;
    public String title;
    public String desc;
    public int price;
    public boolean isComplite;
    public String beginTime;
    public String endTime;
    public HashMap<Integer, User> groupOwners;
    public HashMap<Integer, Groups> groups;
    public List<Customers> customers;

    public  Projects(Map<String, Object> map){
        id = (int) map.get("userId");
        customeId = (int) map.get("customer");
        title = (String) map.get("title");
        desc = (String) map.get("desc");
        price = (int) map.get("priceInDollars");
        isComplite = (Boolean) map.get("isComplite");
        beginTime = (String) map.get("beginTime");
        endTime = (String) map.get("untilTime");
//        groupOwners;
//        groups;
//        customers;
    }
}
