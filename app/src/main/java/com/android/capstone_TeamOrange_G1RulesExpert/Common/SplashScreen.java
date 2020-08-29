package com.android.capstone_TeamOrange_G1RulesExpert.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.capstone_TeamOrange_G1RulesExpert.RoomDB.Entity.LoginEntity;
import com.android.capstone_TeamOrange_G1RulesExpert.Users.UserDashboard;
import com.android.capstone_TeamOrange_G1RulesExpert.Databases.ScoresHelper;
import com.android.capstone_TeamOrange_G1RulesExpert.Databases.SessionManager;
import com.android.capstone_TeamOrange_G1RulesExpert.HelperClasses.LocaleHelper;
import com.android.q1learningapp.R;
import com.android.capstone_TeamOrange_G1RulesExpert.RoomDB.DatabaseClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    //Variable to hold animation time for seconds
    private static int SPLASH_TIMER = 5000;

    ImageView splash_bgImage;
    TextView poweredByLine;

    Animation sideAnim, bottomAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // your language
        changeLanguage(LocaleHelper.getLanguage(SplashScreen.this));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        //Toast.makeText(SplashScreen.this,LocaleHelper.getLanguage(SplashScreen.this), Toast.LENGTH_SHORT).show();

        //Hooks
        splash_bgImage = findViewById(R.id.splash_bgImage);
        poweredByLine = findViewById(R.id.poweredByLine);

        //Animations
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //Set Animations on Elements
        splash_bgImage.setAnimation(sideAnim);
        poweredByLine.setAnimation(bottomAnim);

        //dark mode code end


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Start Session
//                SessionManager sessionManager = new SessionManager(SplashScreen.this);
//                Boolean sessioncheck =  sessionManager.checkLogin();
//                if (sessioncheck) {
//                    //User Already Signed In
//                    //Get all the data from Session
//                    //Start Session
//                    HashMap<String, String> userDetails = sessionManager.getUserDetailsFromSession();
//                    String _phoneNo = userDetails.get(SessionManager.KEY_PHONE_NUMBER);
//                    String _password = userDetails.get(SessionManager.KEY_PASSWORD);
//
//                    getUserDetails(_phoneNo, _password);
//
//                } else {
//                    //User Signed Out
//                    Intent intent = new Intent(SplashScreen.this, FrontScreen.class);
//                    startActivity(intent);
//                    finish();
//                }
                // your language
                changeLanguage(LocaleHelper.getLanguage(SplashScreen.this));

            }
        }, SPLASH_TIMER);

        insertRootNodesScores();
        retrieveLoginInfo();


    }

    private void insertRootNodesScores() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Scores").child("+4672229999");

        ScoresHelper addNewScore = new ScoresHelper("0", "0", "quiz1", "+4672229999", "");
        myRef.child("s1").setValue(addNewScore);
    }


    public void changeLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void getUserDetails(final String phoneNumber, final String passWord) {
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query myRef = database.getReference("Users").orderByChild("phoneNo").equalTo(phoneNumber);

        // Attach a listener to read the data at our posts reference
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Toast.makeText(ProfileActivity.this, "User Exists", Toast.LENGTH_SHORT).show();
                    String systemPassword = dataSnapshot.child(phoneNumber).child("password").getValue(String.class);
                    if (systemPassword.equals(passWord)) {

                        Intent intent = new Intent(SplashScreen.this, UserDashboard.class);
                        startActivity(intent);
                        //Toast.makeText(SplashScreen.this, "User Exists", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //Toast.makeText(SplashScreen.this, "User Does Not Exists", Toast.LENGTH_SHORT).show();
                        //Start Session
                        SessionManager sessionManager = new SessionManager(SplashScreen.this);
                        sessionManager.logoutUserFromSession();
                        Intent intent = new Intent(SplashScreen.this, FrontScreen.class);
                        startActivity(intent);
                    }
                }
                else{
                    //Toast.makeText(ProfileActivity.this, "User Does Not Exists", Toast.LENGTH_SHORT).show();
                    //Start Session
                    SessionManager sessionManager = new SessionManager(SplashScreen.this);
                    sessionManager.logoutUserFromSession();
                    Intent intent = new Intent(SplashScreen.this, FrontScreen.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SplashScreen.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveLoginInfo(){
        class GetCreds extends AsyncTask<Void,Void, List<LoginEntity>> {
            /**
             * @param voids
             * @deprecated
             */
            @Override
            protected List<LoginEntity> doInBackground(Void... voids) {

                List<LoginEntity> credList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .loginDao()
                        .getAll();

                return credList;
            }

            @Override
            protected void onPostExecute(List<LoginEntity> loginEntities) {
                super.onPostExecute(loginEntities);
                //check here for valid user among all saved users
                if (loginEntities.size()>0){
                    //user already saved
                    LoginEntity loginEntity = loginEntities.get(0);
                    getUserDetails(loginEntity.getLogin_id(),loginEntity.getPassword());

                } else {
                    //User Signed Out
                    Intent intent = new Intent(SplashScreen.this, FrontScreen.class);
                    startActivity(intent);
                    finish();
                }
                //.....................



                //dark mode code
                //if dark mode enabled in shared preferences then set it to night mode
                SharedPreferences sharedPreferences
                        = getSharedPreferences(
                        "sharedPrefs", MODE_PRIVATE);
                final SharedPreferences.Editor editor
                        = sharedPreferences.edit();
                final boolean isDarkModeOn
                        = sharedPreferences
                        .getBoolean(
                                "isDarkModeOn", false);

                SessionManager.isDarkModeOn = isDarkModeOn;
                if (isDarkModeOn) {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_YES);
                }
                else {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_NO);
                }

            }
        }

        GetCreds getCreds = new GetCreds();
        getCreds.execute();
    }

}