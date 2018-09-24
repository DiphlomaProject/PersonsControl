package com.example.artem.personscontrol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.artem.personscontrol.DataClasses.Data_Singleton;
import com.example.artem.personscontrol.FirebasePushNotifications.MyFirebaseInstanceIDService;
import com.example.artem.personscontrol.SupportLibrary.Network_connections;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NavigationActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, Network_connections.VolleyCallbackNetworkInterface {

    int networkAction = -1; // 0 - Google SignIn | 1 - SignIn | 2 - SignOut



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // При старте активности получить параметры из намерения
        //Intent intent = getIntent();
        //int actionInt = intent.getIntExtra("type_of_action_with_user",-1);

        /*if(actionInt == preferences_singleton.WRITE_USER_INFO)
            NewUser();
        else if(actionInt == preferences_singleton.UPDATE_USER_INFO)
            UpdateUser();
        //Toast.makeText(this, "UPDATE USER INFO", Toast.LENGTH_SHORT).show();


        //show information about user in navigation controller menu
        hView =  navigationView.getHeaderView(0);
        //hView.setBackgroundResource(R.drawable.agency);
        hView.setBackgroundResource(R.drawable.background_five_colors);

        userEmail = hView.findViewById(R.id.userEmail);
        userName = hView.findViewById(R.id.userName);*/
        Data_Singleton.network_connections.RegisterCallBack(this);
        //Data_Singleton.network_connections.RestApiInfo(this);

    }

    public void getStartInformation(){
        Data_Singleton.network_connections.GetImagePhoto(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_sign_out){
            showProgressDialog();
            // configure shared preferences
            Data_Singleton.getInstance().currentUser.clearSharedPreferences();

            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            // [START initialize_auth]
            FirebaseAuth mAuth; mAuth = FirebaseAuth.getInstance();

            // Firebase sign out
            mAuth.signOut();

            Toast.makeText(getBaseContext(), R.string.signed_out, Toast.LENGTH_SHORT).show();

            // Google revoke access
            mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //preferences_singleton.user = null;
                        }
                    });

            // Создать намерение, которое показывает, какую активность вызвать
            // и содержит необходимые параметры
            Intent intent = new Intent(this, SignIn.class);

            // Добавление параметров в намерение
            //intent.putExtra("test", edit1.getText().toString());

            // Старт активности без возврата результата
            startActivity(intent);

            // Старт активности с возвратом результата
            //startActivityForResult(intent, RC_GOOGLE_SIGN_LOGOUT);
            hideProgressDialog();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void callbackRestApiInfo(JSONObject response) {
        try {
            Map<String, Object> map = Network_connections.toMap(response);
            Log.e("JSONObject to Map", "accept");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callbackGetImage(Bitmap bitmap) {

    }
}
