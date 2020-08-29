package com.android.capstone_TeamOrange_G1RulesExpert.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.capstone_TeamOrange_G1RulesExpert.Common.FrontScreen;
import com.android.q1learningapp.R;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    //Variables
    ImageView backBtn;
    RadioGroup radioGroup;
    RadioButton radioButton;
    DatePicker datePicker;
    TextInputLayout fullName, emailID, passWord, phone_number;
    Button registerBtn;
    CountryCodePicker countryCodePicker;

    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        //Initializing awesomeValidation object
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //Hooks
        radioGroup = findViewById(R.id.radio_group);
        datePicker = findViewById(R.id.age_picker);
        fullName = findViewById(R.id.signup_fullname);
        emailID = findViewById(R.id.signup_emailId);
        passWord = findViewById(R.id.signup_password);
        countryCodePicker = findViewById(R.id.country_code_picker);
        phone_number = findViewById(R.id.signup_phone_number);
        registerBtn = findViewById(R.id.signup_register_button);


        //Adding validation to editTexts
        awesomeValidation.addValidation(this, R.id.signup_fullname, "[a-zA-Z\\s]+", R.string.firstNameError);
        awesomeValidation.addValidation(this, R.id.signup_emailId, Patterns.EMAIL_ADDRESS, R.string.EmailError);
        awesomeValidation.addValidation(this, R.id.signup_phone_number, "^[2-9]{2}[0-9]{8}$", R.string.PhoneNumberError);
        awesomeValidation.addValidation(this, R.id.signup_password, "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}", R.string.PasswordError);


        backBtn = findViewById(R.id.signup_back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpActivity.super.onBackPressed();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == registerBtn) {
                    submitForm();
                }
            }
        });
    }

    private void submitForm() {
        //first validate the form then move ahead
        //if this becomes true that means validation is successful
        if(!isConnected(this)){
            showCustomDialog();
        }

        if (awesomeValidation.validate()) {
            if(validateGender() && validateAge()){

                /*** Get Text Field Values **/
                final String _fullName = fullName.getEditText().getText().toString();
                final String _email = emailID.getEditText().getText().toString();
                final String _password = passWord.getEditText().getText().toString();
                /*** Get Text Field Values **/

                /**********************  Get complete phone number ****************************/
                final String _phoneNo = "+" + getFullPhoneNumber();
                /**********************  Get complete phone number ****************************/

                /********************** Get Gender ****************************/
                final String _gender = getUserGender();
                /********************** Get Gender ****************************/

                /********************** Get Selected date ****************************/
                final String _date = getDateOfBirth();
                /********************** Get Selected Date ****************************/

                // Get a reference to our posts
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                Query myRef = database.getReference("Users").orderByChild("phoneNo").equalTo(_phoneNo);

                // Attach a listener to read the data at our posts reference
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(SignUpActivity.this,"Phone Number Already Exists", Toast.LENGTH_SHORT).show();
                            phone_number.setError("Already Exists");
                            phone_number.requestFocus();
                        }
                        else{
                            /*Toast.makeText(SignUpActivity.this, "No Such User Found", Toast.LENGTH_SHORT).show();*/
                            phone_number.setError(null);
                            phone_number.setErrorEnabled(false);
                            /*Toast.makeText(getApplicationContext(), "Full Name -  " + _fullName + " \n"
                                            + "E-Mail -  " + _email + " \n"
                                            + "Password -  " + _password + " \n"
                                            + "Gender -  " + _gender + " \n"
                                            + "DOB -  " + _date + " \n"
                                            + "Phone No. -  " + _phoneNo
                                    , Toast.LENGTH_SHORT).show();*/

                            Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                            //Pass all fields to the next activity
                            intent.putExtra("fullName", _fullName);
                            intent.putExtra("email", _email);
                            intent.putExtra("password", _password);
                            intent.putExtra("gender", _gender);
                            intent.putExtra("dob", _date);
                            intent.putExtra("phoneNo", _phoneNo);
                            intent.putExtra("whatToDO", "createNewUser");
                            startActivity(intent);

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SignUpActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    private String getFullPhoneNumber() {
        String _getUserEnteredPhoneNumber = phone_number.getEditText().getText().toString().trim();
        //Remove first zero if entered!
        if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
            _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
        }
        //Complete phone number
        return countryCodePicker.getDefaultCountryCode() + _getUserEnteredPhoneNumber;
    }

    private String getUserGender() {
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(selectedId);
        return (String) radioButton.getText();
    }


    private String getDateOfBirth() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        return day + "/" + month + "/" + year;
    }


    /*** Validations ***/
    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if (isAgeValid < 16) {
            Toast.makeText(this, "You are not eligible to apply. You must be at least 16 years old", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }
    /*** Validations ***/

    private boolean isConnected(SignUpActivity signUpActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager)signUpActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())){
            return true;
        }
        else{
            return false;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage("Please check you Internet Connection to continue further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(), FrontScreen.class));
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}