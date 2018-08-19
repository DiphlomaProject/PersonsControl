package com.example.artem.personscontrol.DataClasses;

import com.example.artem.personscontrol.SupportLibrary.Network_connections;

public class Data_Singleton {

    public static String baseURL;
    public static Network_connections network_connections;

    private static final Data_Singleton ourInstance = new Data_Singleton();

    public static Data_Singleton getInstance() {
        return ourInstance;
    }

    private Data_Singleton() {
        baseURL = "https://178.209.88.110:443/";
        network_connections = new Network_connections();
    }
}
