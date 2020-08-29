package com.android.capstone_TeamOrange_G1RulesExpert.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.capstone_TeamOrange_G1RulesExpert.Common.FrontScreen;
import com.android.capstone_TeamOrange_G1RulesExpert.Databases.UserHelperClass;
import com.android.q1learningapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerifyOTP extends AppCompatActivity {

    String fullName, email, gender, dob,  phoneNo, password, whatToDO;
    TextView otpDescriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);


        otpDescriptionText = findViewById(R.id.otp_description_text);
        //Get all the data from Intent
        fullName = getIntent().getStringExtra("fullName");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        gender = getIntent().getStringExtra("gender");
        dob = getIntent().getStringExtra("dob");
        phoneNo = getIntent().getStringExtra("phoneNo");
        whatToDO = getIntent().getStringExtra("whatToDO");

        otpDescriptionText.setText("Registered Using a Phone Number: "+phoneNo);

        if (whatToDO.equals("createNewUser")) {
            storeNewUsersData();
            //Toast.makeText(VerifyOTP.this, "Verification Completed", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeNewUsersData() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");

        UserHelperClass addNewUser = new UserHelperClass(fullName, email, phoneNo, password, dob, gender);
        reference.child(phoneNo).setValue(addNewUser);

    }

    public void goToHomeFromOTP(View view) {
        Intent intent = new Intent(getApplicationContext(), FrontScreen.class);
        startActivity(intent);
    }

    public void callNextScreenFromOTP(View view) {
        Intent intent = new Intent(getApplicationContext(), FrontScreen.class);
        startActivity(intent);
    }
}