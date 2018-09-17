package com.example.artem.personscontrol.SupportLibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.artem.personscontrol.BaseActivity;
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
        void callbackGetImage(Bitmap bitmap);
    }

    private VolleyCallbackNetworkInterface volleyCallback;

    public static final int VolleyRequestNone = -1;
    public static final int VolleyRequestSignOut = 0;
    public static final int VolleyRequestSignIn = 1;
    public static final int VolleyRequestGoogleSignIn = 2;

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
        mParams.put("FCMToken", Data_Singleton.deviceFCMToken);
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

    public void SignInRequest(final Context context, String email, String password){

        HttpsTrustManager.allowAllSSL();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String additionalUrl  = "api/Users/SignIn";
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("email", email);
        mParams.put("password", password);
        mParams.put("FCMToken", Data_Singleton.deviceFCMToken);
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
        String additionalUrl  = "api/Users/SignOut";
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("token", Data_Singleton.getInstance().currentUser.token);
        mParams.put("FCMToken", Data_Singleton.deviceFCMToken);
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
                        ((BaseActivity)context).hideProgressDialog();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void GetImagePhoto(final Context context){
        HttpsTrustManager.allowAllSSL();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
//        String additionalUrl  = "api/Users/getImage";
//        Map<String, String> mParams = new HashMap<String, String>();
//        mParams.put("token", Data_Singleton.getInstance().currentUser.email);
//        JSONObject parameters = new JSONObject(mParams);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, Data_Singleton.baseURL + additionalUrl,  parameters, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        volleyCallback.callbackRestApiInfo(response);
//                    }
//                }, new Response.ErrorListener() {
//                    @SuppressLint("LongLogTag")
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO: Handle error
//                        ((BaseActivity)context).hideProgressDialog();
//                    }
//                });
//
//        // Add the request to the RequestQueue.
//        queue.add(jsonObjectRequest);

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
    }

}

