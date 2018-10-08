package com.example.artem.personscontrol;


import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public static ProfileFragment sharedInstance() { return new ProfileFragment(); }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{12}$";
            CharSequence inputString = phoneNumber;
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputString);

            if(!matcher.matches()) {
                Toast.makeText(getContext(), "Write correctly number (+xxx xxx-xx-xx)", Toast.LENGTH_LONG).show();
                return false;
            }
            else
                return true;
        }
        return false;
    }

}
