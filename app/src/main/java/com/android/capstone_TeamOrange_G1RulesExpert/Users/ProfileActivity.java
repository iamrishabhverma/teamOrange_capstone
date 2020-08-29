package com.android.capstone_TeamOrange_G1RulesExpert.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.android.capstone_TeamOrange_G1RulesExpert.Databases.SessionManager;
import com.android.q1learningapp.R;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    ImageView backBtn;
    DatePicker datePicker;
    RadioGroup radioGroup;
    RadioButton radioButton;
    CountryCodePicker countryCodePicker;
    private TextInputLayout fullName, email_ID , phone_number, passWord;
    Button registerBtn;


    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        //Initializing awesomeValidation object
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //Hooks
        scrollView = findViewById(R.id.profile_scrollView);
        radioGroup = findViewById(R.id.profile_radio_group);
        datePicker = findViewById(R.id.profile_age_picker);
        fullName = findViewById(R.id.profile_fullname);
        email_ID = findViewById(R.id.profile_emailId);
        passWord = findViewById(R.id.profile_password);
        countryCodePicker = findViewById(R.id.country_code_picker);
        phone_number = findViewById(R.id.profile_phone_number);
        registerBtn = findViewById(R.id.profile_update_button);


        if (SessionManager.isDarkModeOn){
            scrollView.setBackgroundResource(R.color.colorPrimaryDark);
        }else{
            scrollView.setBackgroundResource(R.drawable.light_green_bg);

        }
        backBtn = findViewById(R.id.backButtonPress);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UserDashboard.class);
                startActivity(intent);
            }
        });

        setValues();

    }

    private void setValues() {
        //Start Session
        SessionManager sessionManager = new SessionManager(ProfileActivity.this);
        HashMap<String, String> userDetails = sessionManager.getUserDetailsFromSession();

        //Get all the data from Session
        String _phoneNo = userDetails.get(SessionManager.KEY_PHONE_NUMBER);
        String _password = userDetails.get(SessionManager.KEY_PASSWORD);
        String _fullName = userDetails.get(SessionManager.KEY_FULL_NAME);
        String _gender = userDetails.get(SessionManager.KEY_GENDER);
        String _dob = userDetails.get(SessionManager.KEY_DOB);
        String _email = userDetails.get(SessionManager.KEY_EMAIL);


        fullName.getEditText().setText(_fullName);
        phone_number.getEditText().setText(_phoneNo);
        email_ID.getEditText().setText(_email);
        passWord.getEditText().setText(_password);
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id

    }


}