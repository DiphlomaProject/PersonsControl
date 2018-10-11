package com.example.artem.personscontrol.SupportLibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.artem.personscontrol.AspNet_Classes.Customers;
import com.example.artem.personscontrol.AspNet_Classes.GroupTasks;
import com.example.artem.personscontrol.AspNet_Classes.Groups;
import com.example.artem.personscontrol.AspNet_Classes.ProjectTasks;
import com.example.artem.personscontrol.AspNet_Classes.Projects;
import com.example.artem.personscontrol.AspNet_Classes.User;
import com.example.artem.personscontrol.AspNet_Classes.UserTasks;
import com.example.artem.personscontrol.BaseActivity;
import com.example.artem.personscontrol.DataClasses.Data_Singleton;
import com.example.artem.personscontrol.GroupsFragment;
import com.example.artem.personscontrol.ProjectsFragment;
import com.example.artem.personscontrol.R;
import com.example.artem.personscontrol.SignIn;
import com.example.artem.personscontrol.TasksFragment;
import com.example.artem.personscontrol.TasksGroupsFragment;
import com.example.artem.personscontrol.TasksProjectsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.acl.Group;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Network_connections {

    public interface VolleyCallbackNetworkInterface {
        void callbackRestApiInfo(JSONObject response);
        void callbackGetImage(Bitmap bitmap);
    }

    private VolleyCallbackNetworkInterface volleyCallback;

    public static final int VolleyRequestNone = -1;
    public static final int VolleyRequestSignOut = 0;
    public static final int VolleyRequestSignIn = 1;
    public static final int VolleyRequestGoogleSignIn = 2;
    public static final int VolleyRequestGetUserPhoto = 3;
    public static final int VolleyRequestRegisterUser = 4;

    public static final int VolleyRequestGetGroups = 5;
    public static final int VolleyRequestGetProjects = 6;

    public static final int VolleyRequestGetTasks = 7;
    public static final int VolleyRequestGetTasksGroups = 8;
    public static final int VolleyRequestGetTasksProjects = 9;

    public static final int RedirectToTasksTasksAfterRequest = 0;
    public static final int RedirectToProjectsTasksAfterRequest = 1;
    public static final int RedirectToGroupsTasksAfterRequest = 2;

    public static final int CompleteTaskPersonal_Action = 0;
    public static final int CompleteTaskGroup_Action = 1;
    public static final int CompleteTaskProject_Action = 2;

    private static String base_URL  = "https://178.209.88.110:443/";
    private static String SignIn_URL =  "api/Users/SignIn";
    private static String GoogleSignIn_URL = "api/Users/GoogleSignIn";
    private static String HelpInfo_URL = "api/RestApiHelper/helpinfo";
    private static String SignUp_URL = "api/Users/SignUp";
    private static String SignOut_URL ="api/Users/SignOut";
    private static String GetUserImg_URL = "api/Users/getUserImg?email=";
    private static String GetUserGroups_URL = "api/Groups/GetGroups";
    private static String GetUserProject_URL = "api/Projects/GetProjects";
    private static String GetUserTasks_URL = "api/Tasks/GetTasks";
    private static String CompleteTaskPersonal_URL = "api/Tasks/UpdateTasksPersonal";
    private static String CompleteTaskGroup_URL = "api/Tasks/UpdateTasksGroup";
    private static String CompleteTaskProject_URL = "api/Tasks/UpdateTasksProject";

    public Network_connections(){ }

    public void RegisterCallBack(VolleyCallbackNetworkInterface volleyCallback){
        this.volleyCallback = volleyCallback;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public void RestApiInfo(final Context context){
        if(context == null) return;

        HttpsTrustManager.allowAllSSL();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        //String additionalUrl  = "api/RestApiHelper/HelpInfo";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, base_URL + HelpInfo_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        volleyCallback.callbackRestApiInfo(response);
                    }
                }, new Response.ErrorListener() {

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley onErrorResponse : ", error.toString());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

        // Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void GoogleSignInRequest(final Context context, String displayName, String email, String phone){

        HttpsTrustManager.allowAllSSL();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        //String additionalUrl  = "api/Users/GoogleSignIn";
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("displayName", displayName);
        mParams.put("email", email);
        mParams.put("phone", phone);
        mParams.put("FCMToken", Data_Singleton.deviceFCMToken);
        JSONObject parameters = new JSONObject(mParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, base_URL + GoogleSignIn_URL,  parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        volleyCallback.callbackRestApiInfo(response);
                    }
                }, new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley GoogleSignInRequest error : ", error.toString());
                        ((SignIn)context).hideProgressDialog();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void SignInRequest(final Context context, String email, String password){

        HttpsTrustManager.allowAllSSL();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        //String additionalUrl  = "api/Users/SignIn";
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("email", email);
        mParams.put("password", password);
        mParams.put("FCMToken", Data_Singleton.deviceFCMToken);
        JSONObject parameters = new JSONObject(mParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, base_URL + SignIn_URL,  parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        volleyCallback.callbackRestApiInfo(response);
                    }
                }, new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley SignInRequest error : ", error.toString());
                        ((BaseActivity)context).hideProgressDialog();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void SignOutRequest(final Context context){

        HttpsTrustManager.allowAllSSL();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        //String additionalUrl  = "api/Users/SignOut";
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("token", Data_Singleton.getInstance().currentUser.token);
        mParams.put("FCMToken", Data_Singleton.deviceFCMToken);
        JSONObject parameters = new JSONObject(mParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, base_URL + SignOut_URL,  parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        volleyCallback.callbackRestApiInfo(response);
                    }
                }, new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((BaseActivity)context).hideProgressDialog();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void SignUpRequest(final Context context, String email, String passwod, String displayName){

        HttpsTrustManager.allowAllSSL();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        //String additionalUrl  = "api/Users/SignUp";
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("email", email);
        mParams.put("password", passwod);
        mParams.put("displayName", displayName);
        JSONObject parameters = new JSONObject(mParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, base_URL + SignUp_URL,  parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        volleyCallback.callbackRestApiInfo(response);
                    }
                }, new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((BaseActivity)context).hideProgressDialog();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void GetImagePhoto(final Context context, String email){
        HttpsTrustManager.allowAllSSL();
        if(email == null || email.isEmpty()) return;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        //String additionalUrl  = "api/Users/getUserImg?email=" + email;

        ImageLoader mImageLoader = new ImageLoader(queue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                        volleyCallback.callbackGetImage(bitmap);
                    }
                });
        mImageLoader.get(base_URL + GetUserImg_URL + email, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                volleyCallback.callbackGetImage(response.getBitmap());
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley get image error : ", error.toString());
            }
        });

    }

    // Methods without callbacks, they change navigation fragments

    // Groups and Projects
    public void GetUsersGroups(final Context context, String token, String id){
        if(Data_Singleton.getInstance().navigationActivity == null) return;

        HttpsTrustManager.allowAllSSL();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        //String additionalUrl  = "api/Groups/GetGroups";
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("token", token);
        mParams.put("id", id);
        JSONObject parameters = new JSONObject(mParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, base_URL + GetUserGroups_URL,  parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        GroupConverter(response);
                        ((BaseActivity)context).hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((BaseActivity)context).hideProgressDialog();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void GetUsersProjects(final Context context, String token, String id){
        if(Data_Singleton.getInstance().navigationActivity == null) return;

        HttpsTrustManager.allowAllSSL();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        //String additionalUrl  = "api/Groups/GetGroups";
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("token", token);
        mParams.put("userId", id);
        JSONObject parameters = new JSONObject(mParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, base_URL + GetUserProject_URL,  parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //volleyCallback.callbackRestApiInfo(response);
                        ProjectsConverter(response);
                        ((BaseActivity)context).hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((BaseActivity)context).hideProgressDialog();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    // get tasks, this url request will get all tasks for current user (Personal, Groups, Projects)
    public void GetUsersTasks(final Context context, String token, String id, final int redirectTo){
        if(Data_Singleton.getInstance().navigationActivity == null) return;

        HttpsTrustManager.allowAllSSL();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        //String additionalUrl  = "api/Groups/GetGroups";
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("token", token);
        mParams.put("userId", id);
        JSONObject parameters = new JSONObject(mParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, base_URL + GetUserTasks_URL,  parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //volleyCallback.callbackRestApiInfo(response);
                        TasksConverter(response, redirectTo);
                        ((BaseActivity)context).hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((BaseActivity)context).hideProgressDialog();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    // converters and move to fragments
    private void GroupConverter(JSONObject response){
        if (response == null) return;
        try {
            Map<String, Object> groupMap = toMap(response);
            Map<String,Object> groups_model = (Map<String, Object>) groupMap.get("groups_model");

            if (groups_model == null  || groups_model.size() <= 0){
                Data_Singleton.getInstance().navigationActivity.setTitle("My Groups");
                Data_Singleton.getInstance().navigationActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.navigation_container, ProjectsFragment.sharedInstance()).commit();
                return;
            }

            List<Map<String, Object>> groupsOnly = (List<Map<String, Object>>) groups_model.get("groups");
            List<Map<String, Object>> owners = (List<Map<String, Object>>) groups_model.get("owners");

            Data_Singleton.getInstance().groups = new ArrayList<>();
            for(Map<String,Object> oneGroup : groupsOnly)
                if (!Data_Singleton.getInstance().groups.contains(new Groups(oneGroup))) {
                    Groups gr = new Groups(oneGroup);
                    for (Map<String, Object> owner : owners)
                        if (gr.ownerId.equals((new User(owner)).id)) {
                            gr.ownerInfo = new User(owner);
                            break;
                        }
                    Data_Singleton.getInstance().groups.add(gr);
                }
            Data_Singleton.getInstance().navigationActivity.setTitle("My Groups");
            Data_Singleton.getInstance().navigationActivity.getFragmentManager().beginTransaction()
                    .replace(R.id.navigation_container, ProjectsFragment.sharedInstance()).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // converters and move to fragments
    private void ProjectsConverter(JSONObject response){
        if (response == null) return;
        try {
            Map<String, Object> projectsMap = toMap(response);
            Map<String,Object> projects_model = (Map<String, Object>) projectsMap.get("data");
            List<Map<String, Object>> projectsOnly = (List<Map<String, Object>>) projects_model.get("projects");

            if (projectsOnly == null  || projectsOnly.size() <= 0){
                Data_Singleton.getInstance().navigationActivity.setTitle("My Projects");
                Data_Singleton.getInstance().navigationActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.navigation_container, ProjectsFragment.sharedInstance()).commit();
                return;
            }

            List<Map<String, Object>> customers = (List<Map<String, Object>>) projects_model.get("customers");
            List<Map<String, Object>> groups = (List<Map<String, Object>>) projects_model.get("groups");

            Data_Singleton.getInstance().projects = new ArrayList<>();
            for(Map<String,Object> oneProject : projectsOnly)
                if(!Data_Singleton.getInstance().projects.contains(new Projects(oneProject))) {
                    Projects pj = new Projects(oneProject);

                    ArrayList<Groups> groupsProj = new ArrayList<>();
                    for(Map<String, Object> listGroups : groups) {
                        ArrayList<Map<String, Object>> groupsJson = (ArrayList<Map<String, Object>>)listGroups.get(Integer.toString(pj.id));
                        if (groupsJson != null)
                            for(Map<String, Object> group : groupsJson) {
                                if (!groupsProj.contains(new Groups(group)))
                                    groupsProj.add(new Groups(group));
                            }
                    }

                    for(Map<String, Object> ownersList : customers) {
                        Map<String, Object> cust = (Map<String, Object>) ownersList.get(Integer.toString(pj.id));
                        pj.customer = new Customers(cust);
                    }

                    pj.groups = groupsProj;
                    Data_Singleton.getInstance().projects.add(pj);
                }

            Data_Singleton.getInstance().navigationActivity.setTitle("My Projects");
            Data_Singleton.getInstance().navigationActivity.getFragmentManager().beginTransaction()
                    .replace(R.id.navigation_container, ProjectsFragment.sharedInstance()).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void TasksConverter(JSONObject response, int redirectTo){
        if (response == null) return;
        try {
            Map<String, Object> projectsMap = toMap(response);
            Map<String, Object> data = (Map<String, Object> ) projectsMap.get("data");

            if (data != null  || data.size() > 0){

                ArrayList<Map<String, Object>> tasksPerson = (ArrayList<Map<String, Object>>) data.get("tasksPerson");
                if(tasksPerson != null && tasksPerson.size() > 0){
                    Data_Singleton.getInstance().userTasks = new ArrayList<>();
                    for(Map<String, Object> task : tasksPerson)
                        if(!Data_Singleton.getInstance().userTasks.contains(new UserTasks(task)))
                            Data_Singleton.getInstance().userTasks.add(new UserTasks(task));
                }

                ArrayList<Map<String, Object>> tasksGroups = (ArrayList<Map<String, Object>>) data.get("tasksGroups");
                ArrayList<Map<String, Object>> groups = (ArrayList<Map<String, Object>>) data.get("groups");
                if(tasksGroups != null && tasksGroups.size() > 0){
                    Data_Singleton.getInstance().groupTasks = new ArrayList<>();
                    for(Map<String, Object> task : tasksGroups) {
                        GroupTasks gTask = new GroupTasks(task);
                        for (Map<String, Object> group : groups) {
                            if (new Groups(group).id == gTask.toGroupId)
                                gTask.group = new Groups(group);
                        }
                        if (!Data_Singleton.getInstance().groupTasks.contains(gTask)) {
                            Data_Singleton.getInstance().groupTasks.add(gTask);
                        }
                    }
                }


                ArrayList<Map<String, Object>> tasksProjects = (ArrayList<Map<String, Object>>) data.get("tasksProjects");
                ArrayList<Map<String, Object>> projects = (ArrayList<Map<String, Object>> ) data.get("projects");
                if(tasksProjects != null && tasksProjects.size() > 0){
                    Data_Singleton.getInstance().projectTasks = new ArrayList<>();
                    for(Map<String, Object> task : tasksProjects) {
                        ProjectTasks gTask = new ProjectTasks(task);
                        for (Map<String, Object> project : projects) {
                            if (new Projects(project).id == gTask.toProjectId)
                                gTask.project = new Projects(project);
                        }
                        if (!Data_Singleton.getInstance().projectTasks.contains(gTask)) {
                            Data_Singleton.getInstance().projectTasks.add(gTask);
                        }
                    }
                }
            }

            switch (redirectTo){
                case RedirectToTasksTasksAfterRequest:
                    Data_Singleton.getInstance().navigationActivity.setTitle("My Tasks");
                    Data_Singleton.getInstance().navigationActivity.getFragmentManager().beginTransaction()
                            .replace(R.id.navigation_container, TasksFragment.sharedInstance()).commit();
                    break;
                case RedirectToGroupsTasksAfterRequest:
                    Data_Singleton.getInstance().navigationActivity.setTitle("My Groups Tasks");
                    Data_Singleton.getInstance().navigationActivity.getFragmentManager().beginTransaction()
                            .replace(R.id.navigation_container, TasksGroupsFragment.sharedInstance()).commit();
                    break;
                case RedirectToProjectsTasksAfterRequest:
                    Data_Singleton.getInstance().navigationActivity.setTitle("My Projects Tasks");
                    Data_Singleton.getInstance().navigationActivity.getFragmentManager().beginTransaction()
                            .replace(R.id.navigation_container, TasksProjectsFragment.sharedInstance()).commit();
                    break;
                    default:
                        break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Complete Task
    public void CompleteTask(final Context context, String token, String taskId, Integer completeAction){
        if(Data_Singleton.getInstance().navigationActivity == null) return;

        HttpsTrustManager.allowAllSSL();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        //String additionalUrl  = "api/Groups/GetGroups";
        Map<String, String> mParams = new HashMap<String, String>();
        String url = base_URL;
        mParams.put("token", token);
        mParams.put("taskId", taskId);

        switch (completeAction){
            case CompleteTaskPersonal_Action:
                url += CompleteTaskPersonal_URL;
                break;
            case CompleteTaskGroup_Action:
                url += CompleteTaskGroup_URL;
                break;
            case CompleteTaskProject_Action:
                url += CompleteTaskProject_URL;
                break;
                default:
                    return;
        }


        JSONObject parameters = new JSONObject(mParams);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url,  parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ((BaseActivity)context).hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((BaseActivity)context).hideProgressDialog();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


}

