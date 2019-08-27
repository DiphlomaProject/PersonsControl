package com.example.artem.personscontrol;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.artem.personscontrol.AspNet_Classes.User;
import com.example.artem.personscontrol.DataClasses.Data_Singleton;
import com.example.artem.personscontrol.FirebasePushNotifications.MyFirebaseMessagingService;
import com.example.artem.personscontrol.SupportLibrary.Network_connections;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Map;

public class SignIn extends BaseActivity implements View.OnClickListener, Network_connections.VolleyCallbackNetworkInterface {

    private static final String TAG_Email = "EmailPassword";
    private static final String TAG_Google = "GoogleLogIn";
    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private static final int RC_GOOGLE_SIGN_LOGOUT = 8001;

    Network_connections network_connections;
    int networkAction = Network_connections.VolleyRequestNone; // 0 - SignOut | 1 - SignIn | 2 - Google SignIn

    // [START declare_auth]
    private FirebaseAuth mAuth;
    //google sign In authenticator
    private GoogleSignInClient mGoogleSignInClient;

    Activity activity_login;
    Snackbar snackbar;
    //Preferences_Singleton preferences_singleton = Preferences_Singleton.getInstance();
    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Data_Singleton.activity = this;
        Data_Singleton.getDeviceFCMToken();

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        network_connections = new Network_connections();
        network_connections.RegisterCallBack(this);

        this.setTitle("Welcome");
        activity_login = this;


        // register response FCM device Id
        //new MyFirebaseInstanceIDService().onTokenRefresh();
        //new MyFirebaseMessagingService();

        // Views
        mEmailField = findViewById(R.id.field_email);
        mPasswordField = findViewById(R.id.field_password);

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.verify_email_button).setOnClickListener(this);
        //findViewById(R.id.reset_password_button).setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();

        //snackbar

    }

    public void GoogleSignInSnackBar(String message){
        snackbar = Snackbar.make(findViewById(R.id.scrollViewLogin), message, Snackbar.LENGTH_LONG);
        //snackbar.setText("Welcome " + user.getDisplayName());
        /*snackbar.setAction(R.string.sign_out, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });*/
        snackbar.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            //GoogleSignInSnackBar(currentUser);
            showProgressDialog();
            GoogleSignInApi(currentUser);
        }else if (Data_Singleton.getInstance().currentUser.loadSharedPreferences(this)) {
            // Создать намерение, которое показывает, какую активность вызвать
            // и содержит необходимые параметры
            Intent intent = new Intent(this, NavigationActivity.class);
            // Старт активности без возврата результата
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        /*if (i == R.id.email_create_account_button) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else*/
        if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }else if (i == R.id.sign_in_button) { //google button
            signIn();
        }else if (i == R.id.email_create_account_button) {
            // Создать намерение, которое показывает, какую активность вызвать
            // и содержит необходимые параметры
            Intent intent = new Intent(this, SignUp.class);
            // Старт активности без возврата результата
            startActivity(intent);
        }/*else if (i == R.id.reset_password_button){
            resetPassword();
        }*/
    }

    private void signIn(String email, String password){
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fill email and password correct", Toast.LENGTH_LONG).show();
            return;
        }

        showProgressDialog();
        networkAction = Network_connections.VolleyRequestSignIn;
        network_connections.SignInRequest(this, email, password);
    }


    private void signIn() {
        showProgressDialog();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG_Google, "Google sign in failed", e);
                // ...
                hideProgressDialog();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_Google, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //GoogleSignInSnackBar(user);
                            GoogleSignInApi(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_Google, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                            hideProgressDialog();
                        }

                        // ...
                    }
                });
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(activity_login, R.string.signed_out, Toast.LENGTH_SHORT).show();
                    }
                });

        //updateUI(null);

        networkAction = Network_connections.VolleyRequestSignOut;
        network_connections.SignOutRequest(this);
        hideProgressDialog();
    }

    private void GoogleSignInApi(FirebaseUser account){
        networkAction = Network_connections.VolleyRequestGoogleSignIn;
        network_connections.GoogleSignInRequest(this, account.getDisplayName(), account.getEmail(), account.getPhoneNumber());
    }


    @Override
    public void callbackRestApiInfo(JSONObject response) {
        try {
            Map<String, Object> map = Network_connections.toMap(response);
            switch (networkAction){
                case  Network_connections.VolleyRequestNone:
                    break;
                case Network_connections.VolleyRequestSignIn:
                case Network_connections.VolleyRequestGoogleSignIn:
                    if ((int)map.get("code") == 202 || (int)map.get("code") == 200){

                        Data_Singleton.getInstance().currentUser = new User((Map<String, Object>) map.get("data"), map.get("token").toString());
                        if (!Data_Singleton.getInstance().currentUser.isInforationValid()) {
                            signOut();
                            break;
                        }else {
                            Data_Singleton.getInstance().currentUser.saveToSharedPreferences(this);
                        }
                        // Создать намерение, которое показывает, какую активность вызвать
                        // и содержит необходимые параметры
                        Intent intent = new Intent(this, NavigationActivity.class);
                        // Старт активности без возврата результата
                        startActivity(intent);
                        finish();
                    } else {
                        ((TextView)this.findViewById(R.id.error_message)).setText((String)map.get("message"));
                        ((TextView)this.findViewById(R.id.error_message)).setVisibility(View.VISIBLE);
                        this.signOut();
                    }
                    break;
                case  Network_connections.VolleyRequestSignOut:
                    break;
                default:
                    this.signOut();
                    break;
            }//switch
        } catch (JSONException e) {
            e.printStackTrace();
            this.signOut();
            this.GoogleSignInSnackBar("Json (GoogleApi) parser response error.");
        }
        hideProgressDialog();
    }

    @Override
    public void callbackGetImage(Bitmap bitmap) {

    }
}

