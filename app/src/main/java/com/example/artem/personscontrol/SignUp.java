package com.example.artem.personscontrol;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artem.personscontrol.AspNet_Classes.User;
import com.example.artem.personscontrol.DataClasses.Data_Singleton;
import com.example.artem.personscontrol.SupportLibrary.Network_connections;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends BaseActivity implements  Network_connections.VolleyCallbackNetworkInterface {

    int networkAction = Network_connections.VolleyRequestNone;
    Network_connections network_connections;

    Activity activity = this;

    EditText email;
    EditText name;
    EditText password;
    EditText repeatPassword;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        network_connections = new Network_connections();
        network_connections.RegisterCallBack(this);

        email = this.findViewById(R.id.editTextEmail);
        name = this.findViewById(R.id.editTextName);
        password = this.findViewById(R.id.editTextPassword);
        repeatPassword = this.findViewById(R.id.editTextRepPassword);

        signUp = this.findViewById(R.id.buttonSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkInfo())
                    return;
                networkAction = Network_connections.VolleyRequestRegisterUser;
                network_connections.SignUpRequest(activity, email.getText().toString(), password.getText().toString(), name.getText().toString());
            }
        });
    }

    public boolean checkInfo(){
        boolean res = true;
        TextView textView;

        textView = this.findViewById(R.id.textView_name_error);
        textView.setVisibility(View.INVISIBLE);
        textView = this.findViewById(R.id.textView_email_error);
        textView.setVisibility(View.INVISIBLE);
        textView = this.findViewById(R.id.textView_pass_error);
        textView.setVisibility(View.INVISIBLE);
        textView = this.findViewById(R.id.textView_pass_rep_error);
        textView.setVisibility(View.INVISIBLE);

        if (name.getText().toString().isEmpty()){
            //Toast.makeText(this, "Write correctly your name.", Toast.LENGTH_LONG).show();
            //return false;
            textView = this.findViewById(R.id.textView_name_error);
            textView.setVisibility(View.VISIBLE);
            res = false;
        }
        if (!isEmailValid(email.getText().toString())){
            //Toast.makeText(this, "Write correctly email.", Toast.LENGTH_LONG).show();
            //return false;
            textView = this.findViewById(R.id.textView_email_error);
            textView.setVisibility(View.VISIBLE);
            res = false;
        }
        if ((password.getText().length() < 6 || repeatPassword.getText().length() < 6) ||
                !password.getText().toString().contentEquals(repeatPassword.getText())) {
            //Toast.makeText(this, "Write correctly password and repeat password.", Toast.LENGTH_LONG).show();
            //return false;
            textView = this.findViewById(R.id.textView_pass_error);
            textView.setVisibility(View.VISIBLE);
            textView = this.findViewById(R.id.textView_pass_rep_error);
            textView.setVisibility(View.VISIBLE);
            res = false;
        }

        return res;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
                    break;
                case  Network_connections.VolleyRequestSignOut:
                    break;
                case Network_connections.VolleyRequestRegisterUser:
                    if ((int)map.get("code") == 202 || (int)map.get("code") == 200){
                        // Создать намерение, которое показывает, какую активность вызвать
                        // и содержит необходимые параметры
                        Intent intent = new Intent(this, SignIn.class);
                        Toast.makeText(this, "You can login.", Toast.LENGTH_SHORT).show();
                        // Старт активности без возврата результата
                        startActivity(intent);
                        finish();
                    } else {
                        //Toast.makeText(this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                        TextView textView = this.findViewById(R.id.textView_message_error);
                        textView.setText(map.get("message").toString());
                        textView.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }//switch
            networkAction = Network_connections.VolleyRequestNone;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hideProgressDialog();
    }

    @Override
    public void callbackGetImage(Bitmap bitmap) {}
}
