package com.example.artem.personscontrol.AspNet_Classes;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
    public ArrayList<Groups> groups;
    public Customers customer;

    public  Projects(Map<String, Object> map){
        groups = new ArrayList();

        id = (int) map.get("Id");
        customeId = (int) map.get("Customer");
        title = (String) map.get("Title");
        desc = (String) map.get("Description");
        price = (int) map.get("PriceInDollars");
        isComplite = (Boolean) map.get("isComplite");
        beginTime = (String) map.get("BeginTime");
        endTime = (String) map.get("UntilTime");

//        groupOwners;
//        groups;
//        customers;
    }
}
