package com.example.artem.personscontrol.SupportLibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.artem.personscontrol.DataClasses.Data_Singleton;
import com.example.artem.personscontrol.SignIn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Network_connections {

    public interface VolleyCallbackNetworkInterface {
        void callbackRestApiInfo(JSONObject response);
    }

    private VolleyCallbackNetworkInterface volleyCallback;

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
        String additionalUrl  = "api/RestApiHelper/HelpInfo";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Data_Singleton.baseURL + additionalUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        volleyCallback.callbackRestApiInfo(response);
                    }
                }, new Response.ErrorListener() {

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
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
        String additionalUrl  = "api/Users/GoogleSignIn";
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("displayName", displayName);
        mParams.put("email", email);
        mParams.put("phone", phone);
        JSONObject parameters = new JSONObject(mParams);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Data_Singleton.baseURL + additionalUrl,  parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        volleyCallback.callbackRestApiInfo(response);
                    }
                }, new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("Volley GoogleSignInRequest error : ", error.toString());
                        ((SignIn)context).hideProgressDialog();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


}
