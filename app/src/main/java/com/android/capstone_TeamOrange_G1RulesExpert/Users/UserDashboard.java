package com.android.capstone_TeamOrange_G1RulesExpert.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.capstone_TeamOrange_G1RulesExpert.Common.FrontScreen;
import com.android.capstone_TeamOrange_G1RulesExpert.Databases.SessionManager;
import com.android.capstone_TeamOrange_G1RulesExpert.HelperClasses.LocaleHelper;
import com.android.q1learningapp.R;
import com.android.capstone_TeamOrange_G1RulesExpert.RoomDB.DatabaseClient;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Locale;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    TextView UserDetailsText, UserNameInitials, changeLocale;
    boolean lang_selected;
    Context context;
    RelativeLayout progressbar;
    Resources resources;
    MenuView.ItemView navDarkItem;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menuIcon);
        UserDetailsText = findViewById(R.id.welcome_user);
        UserNameInitials = findViewById(R.id.userNameInitial);
        changeLocale = findViewById(R.id.change_language);
        progressbar = findViewById(R.id.dashboard_progress_bar);

        //Start Session
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUserDetailsFromSession();

        //Get all the data from Session
        String fullName = userDetails.get(SessionManager.KEY_FULL_NAME);
        String phoneNumber = userDetails.get(SessionManager.KEY_PHONE_NUMBER);

        navigationDrawer();

        UserDetailsText.setText(getString(R.string.welcome_word).toUpperCase() + "\n\n" + fullName + "\n" + phoneNumber);

    }

    //Navigation Drawer Functions
    private void navigationDrawer() {

        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

    }


    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                break;

            case R.id.nav_allQuiz:
                if(!isConnected(UserDashboard.this)){
                    showCustomDialog();
                }
                else{
                    startActivity(new Intent(getApplicationContext(), AllQuizActivity.class));
                }
                break;

            case R.id.nav_all_scores:
                startActivity(new Intent(getApplicationContext(), AllScores.class));
                break;

            case R.id.nav_add_faq:
                startActivity(new Intent(getApplicationContext(), FaqActivity.class));
                break;

            case R.id.nav_profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
            case R.id.nav_dark:
                SharedPreferences sharedPreferences
                        = getSharedPreferences(
                        "sharedPrefs", MODE_PRIVATE);
                final SharedPreferences.Editor editor
                        = sharedPreferences.edit();
                final boolean isDarkModeOn
                        = sharedPreferences
                        .getBoolean(
                                "isDarkModeOn", false);

                if (isDarkModeOn) {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_NO);
                    editor.putBoolean("isDarkModeOn",false);

                }
                else {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_YES);
                    editor.putBoolean("isDarkModeOn",true);

                }
                editor.apply();

                break;

            case R.id.nav_logout:
                //Start Session
                SessionManager sessionManager = new SessionManager(UserDashboard.this);
                sessionManager.logoutUserFromSession();
                deleteLoginInfo();
                Intent intent = new Intent(UserDashboard.this, FrontScreen.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }


    private void deleteLoginInfo(){
        class GetCreds extends AsyncTask<Void,Void,Void> {
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


    public void changeLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onStart() {
        super.onStart();

        String currentLocale = LocaleHelper.getLanguage(UserDashboard.this);
        if(currentLocale.equals("pa")){
            changeLocale.setText("ਪੰਜਾਬੀ"); }
        else{
            changeLocale.setText("English");
        }

        changeLocale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] Language = {"ENGLISH", "ਪੰਜਾਬੀ"};
                final int checkedItem;
                String currentLocale = LocaleHelper.getLanguage(UserDashboard.this);
                if(currentLocale.equals("pa")){
                    checkedItem = 1;}
                else{
                    checkedItem = 0;
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(UserDashboard.this);
                builder.setTitle(getString(R.string.select_a_language))
                        .setSingleChoiceItems(Language, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(UserDashboard.this,""+which,Toast.LENGTH_SHORT).show();
                                changeLocale.setText(Language[which]);
                                lang_selected= Language[which].equals("ENGLISH");
                                //if user select preferred language as English then
                                if(Language[which].equals("ENGLISH"))
                                {
                                    context = LocaleHelper.setLocale(UserDashboard.this, "en");
                                    changeLanguage("en");
                                }
                                //if user select preferred language as Punjabi then
                                if(Language[which].equals("ਪੰਜਾਬੀ"))
                                {
                                    context = LocaleHelper.setLocale(UserDashboard.this, "pa");
                                    changeLanguage("pa");
                                }
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                            }
                        });
                builder.create().show();
            }
        });
    }

    private boolean isConnected(UserDashboard userDashboard) {
        ConnectivityManager connectivityManager = (ConnectivityManager)userDashboard.getSystemService(Context.CONNECTIVITY_SERVICE);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(UserDashboard.this);
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
                        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}