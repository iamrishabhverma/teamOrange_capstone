package com.android.capstone_TeamOrange_G1RulesExpert.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.capstone_TeamOrange_G1RulesExpert.Common.FrontScreen;
import com.android.capstone_TeamOrange_G1RulesExpert.Databases.SessionManager;
import com.android.capstone_TeamOrange_G1RulesExpert.RoomDB.DatabaseClient;
import com.android.capstone_TeamOrange_G1RulesExpert.RoomDB.Entity.LoginEntity;
import com.android.capstone_TeamOrange_G1RulesExpert.Users.UserDashboard;
import com.android.q1learningapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {

    //Variables
    ImageView backBtn;
    Button SignUp;
    CountryCodePicker countryCodePicker;
    TextInputLayout loginPhoneNumber, loginPassword;
    Button loginBtnSubmit;
    RelativeLayout progressbar;
    CheckBox rememberMeCheckBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Hooks
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        countryCodePicker = findViewById(R.id.login_country_code_picker);
        loginPhoneNumber = findViewById(R.id.login_phone_number);
        loginPassword = findViewById(R.id.login_password);
        loginBtnSubmit = findViewById(R.id.login_user_btn);
        progressbar = findViewById(R.id.login_progress_bar);

        backBtn = findViewById(R.id.backButtonPress);
        SignUp = findViewById(R.id.goToSignUpActivity);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.super.onBackPressed();
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        loginBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                letTheUserLoggedIn();
            }
        });


    }

    private void letTheUserLoggedIn() {

        if(!isConnected(this)){
            showCustomDialog();
        }

        if (!validateFields()) {
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        //Get Data
        final String _password = loginPassword.getEditText().getText().toString().trim();

        String _getUserEnteredPhoneNumber = loginPhoneNumber.getEditText().getText().toString().trim();
        //Remove first zero if entered!
        if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
            _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
        }
        //Complete phone number
        final String _CompletePhoneNo = "+" + countryCodePicker.getDefaultCountryCode() + _getUserEnteredPhoneNumber;

        //Toast.makeText(LoginActivity.this,_CompletePhoneNo+"\n"+ _password, Toast.LENGTH_SHORT).show();

        //Database
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query myRef = database.getReference("Users").orderByChild("phoneNo").equalTo(_CompletePhoneNo);

        // Attach a listener to read the data at our posts reference
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    loginPhoneNumber.setError(null);
                    loginPhoneNumber.setErrorEnabled(false);

                    String systemPassword = dataSnapshot.child(_CompletePhoneNo).child("password").getValue(String.class);
                    if (systemPassword.equals(_password)) {
                        loginPassword.setError(null);
                        loginPassword.setErrorEnabled(false);
                        if (rememberMeCheckBox.isChecked()){
                            saveLoginInfo(_CompletePhoneNo,_password,true);
                        }else{
                            deleteLoginInfo();
                        }
                        String _fullName = dataSnapshot.child(_CompletePhoneNo).child("fullName").getValue(String.class);
                        String _email = dataSnapshot.child(_CompletePhoneNo).child("email").getValue(String.class);
                        String _gender = dataSnapshot.child(_CompletePhoneNo).child("gender").getValue(String.class);
                        String _dob = dataSnapshot.child(_CompletePhoneNo).child("date").getValue(String.class);

                        //Create a Session
                        SessionManager sessionManager = new SessionManager(LoginActivity.this);
                        sessionManager.createLoginSession(_fullName, _email, _CompletePhoneNo, _password, _dob, _gender);


                        //Toast.makeText(LoginActivity.this, _fullName + "\n" + _email, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                        startActivity(intent);


                    } else {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Password Does Not Match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "No Such User Found", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateFields() {
        String _phoneNumber = loginPhoneNumber.getEditText().getText().toString().trim();
        final String _password = loginPassword.getEditText().getText().toString().trim();

        if (_phoneNumber.isEmpty()) {
            loginPhoneNumber.setError(getString(R.string.phone_number_cannot_be_empty));
            loginPhoneNumber.requestFocus();
            return false;
        } else if (_password.isEmpty()) {
            loginPassword.setError(getString(R.string.password_cannot_be_empty));
            loginPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean isConnected(LoginActivity loginActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager)loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

    @Override
    protected void onStart() {
        super.onStart();
        //Start Session
//        SessionManager sessionManager = new SessionManager(this);
//        Boolean sessioncheck =  sessionManager.checkLogin();
//        if (sessioncheck) {
//            //User Already Signed In
//            startActivity(new Intent(getApplicationContext(), UserDashboard.class));
//        } else {
//            //User Signed Out
//        }



    }



    private void saveLoginInfo(final String loginID, final String password,final boolean autoSaveEnabled){


        class SaveTask extends AsyncTask<Void, Void, Void> {

            /**
             * @param voids
             * @deprecated
             */
            @Override
            protected Void doInBackground(Void... voids) {

                LoginEntity loginEntity = new LoginEntity();
                loginEntity.setLogin_id(loginID);
                loginEntity.setPassword(password);
                loginEntity.setAutoLoginEnabled(autoSaveEnabled);

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().loginDao().insert(loginEntity);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();

            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    private void deleteLoginInfo(){
        class GetCreds extends AsyncTask<Void,Void,Void>{
            /**
             * @param voids
             * @deprecated
             */
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .loginDao()
                        .deleteAll();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }

        GetCreds getCreds = new GetCreds();
        getCreds.execute();
    }

}