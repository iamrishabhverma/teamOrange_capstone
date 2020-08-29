package com.android.capstone_TeamOrange_G1RulesExpert.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.capstone_TeamOrange_G1RulesExpert.Databases.ScoresHelper;
import com.android.capstone_TeamOrange_G1RulesExpert.Databases.SessionManager;
import com.android.q1learningapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ScoresActivity extends AppCompatActivity{

    TextView totalScoresText;
    ImageView backBtn;
    Button allQuiz, allScores;
    private String finalScores, totalQues, userId, passW, quizNumber, getCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scores);

        totalScoresText = findViewById(R.id.total_scores_text);
        backBtn = findViewById(R.id.backButtonPress);
        allQuiz = findViewById(R.id.goToAllQuiz);
        allScores = findViewById(R.id.goToAllScores);

        //Get all the data from Intent
        totalQues = getIntent().getStringExtra("totalQues");
        finalScores = getIntent().getStringExtra("totalScores");
        quizNumber = getIntent().getStringExtra("QuizNo");

        totalScoresText.setText(finalScores + "/" + totalQues);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUserDetailsFromSession();

        //Get all the data from Session
        String fullName = userDetails.get(SessionManager.KEY_FULL_NAME);
        userId = userDetails.get(SessionManager.KEY_PHONE_NUMBER);
        passW = userDetails.get(SessionManager.KEY_PASSWORD);

        insertIntoDatabase(userId, passW);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoresActivity.this, AllQuizActivity.class);
                startActivity(intent);
            }
        });

        allQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoresActivity.this, AllQuizActivity.class);
                startActivity(intent);
            }
        });

        allScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoresActivity.this, AllScores.class);
                startActivity(intent);
            }
        });
    }


    private void insertIntoDatabase(final String phoneNumber, String password) {

        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query myRef = database.getReference("Users").orderByChild("phoneNo").equalTo(phoneNumber);

        // Attach a listener to read the data at our posts reference
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if(quizNumber.equals("Challenge")){
                        //Don't Store Values in Database
                    }else{
                        storeScoresData();
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ScoresActivity.this, AllQuizActivity.class);
        startActivity(intent);
    }

    private void storeScoresData() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Scores").child(userId);

        Date date = new Date();
        String strDateFormat = "dd/MM/YYYY hh:mm:ss a";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        getCurrentDate = sdf.format(date);

        String id = reference.push().getKey();

        ScoresHelper addNewScore = new ScoresHelper(finalScores, totalQues, quizNumber, userId, getCurrentDate);
        reference.child(id).setValue(addNewScore);

    }

}