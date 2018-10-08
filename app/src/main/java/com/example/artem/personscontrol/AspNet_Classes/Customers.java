package com.example.artem.personscontrol.AspNet_Classes;

import java.util.Map;

public class Customers {

    public int id;
    public String company;
    public String contactPerson;
    public String position;
    public String phone;
    public String desc;

    public  Customers(Map<String, Object> map){
        id = (int) map.get("Id");
        company = (String) map.get("Company");
        contactPerson = (String) map.get("ContactPerson");
        position = (String) map.get("Position");
        phone = (String) map.get("Phone");
        desc = (String) map.get("Description");
    }
}
