package com.example.artem.personscontrol;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignIn extends BaseActivity implements View.OnClickListener {

    private static final String TAG_Email = "EmailPassword";
    private static final String TAG_Google = "GoogleLogIn";
    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private static final int RC_GOOGLE_SIGN_LOGOUT = 8001;

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


        this.setTitle("Welcome");
        activity_login = this;

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
        snackbar = Snackbar.make(findViewById(R.id.scrollViewLogin), "snackbar", Snackbar.LENGTH_LONG);
    }

    public void GoogleSignInSnackBar(FirebaseUser user){
        snackbar.setText("Welcome " + user.getDisplayName());
        snackbar.setAction(R.string.sign_out, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        snackbar.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            GoogleSignInSnackBar(currentUser);
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
        } else if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }else*/ if (i == R.id.sign_in_button) { //google button
            signIn();
        }/*else if (i == R.id.verify_email_button) {
            sendEmailVerification();
        }else if (i == R.id.reset_password_button){
            resetPassword();
        }*/
    }


    private void signIn() {
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
                // Создать намерение, которое показывает, какую активность вызвать
                // и содержит необходимые параметры
                Intent intent = new Intent(this, NavigationActivity.class);
                // Старт активности без возврата результата
                startActivity(intent);
                finish();

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG_Google, "Google sign in failed", e);
                // ...
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
                            GoogleSignInSnackBar(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_Google, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void AddUserToDatabase(){

    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(activity_login, R.string.signed_out, Toast.LENGTH_SHORT).show();
                    }
                });

        //updateUI(null);
    }
}
