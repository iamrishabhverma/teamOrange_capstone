package com.android.capstone_TeamOrange_G1RulesExpert.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.capstone_TeamOrange_G1RulesExpert.Common.LoginSignup.SignUpActivity;
import com.android.capstone_TeamOrange_G1RulesExpert.Common.LoginSignup.LoginActivity;
import com.android.capstone_TeamOrange_G1RulesExpert.HelperClasses.LocaleHelper;
import com.android.q1learningapp.R;

import java.util.Locale;

public class FrontScreen extends AppCompatActivity {

    Button login_button;
    Button register_button;
    TextView changeLocale;
    boolean lang_selected;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_front_screen);


        login_button = findViewById(R.id.login_button);
        register_button = findViewById(R.id.register_button);
        changeLocale = findViewById(R.id.change_language);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginView();
            }
        } );

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterView();
            }
        });

    }

    public void LoginView(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void RegisterView(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String currentLocale = LocaleHelper.getLanguage(FrontScreen.this);
        if(currentLocale.equals("pa")){
            changeLocale.setText("ਪੰਜਾਬੀ");
        } else{
            changeLocale.setText("English");
        }

        changeLocale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] Language = {"ENGLISH", "ਪੰਜਾਬੀ"};
                final int checkedItem;
                String currentLocale = LocaleHelper.getLanguage(FrontScreen.this);
                if(currentLocale.equals("pa")){
                    checkedItem = 1;}
                else{
                    checkedItem = 0;
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(FrontScreen.this);
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
                                    context = LocaleHelper.setLocale(FrontScreen.this, "en");
                                    changeLanguage("en");
                                }
                                //if user select preferred language as Punjabi then
                                if(Language[which].equals("ਪੰਜਾਬੀ"))
                                {
                                    context = LocaleHelper.setLocale(FrontScreen.this, "pa");
                                    changeLanguage("pa");
                                }
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), FrontScreen.class));
                            }
                        });
                builder.create().show();
            }
        });
    }

    public void changeLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

}