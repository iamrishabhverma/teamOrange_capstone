package com.android.capstone_TeamOrange_G1RulesExpert.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.q1learningapp.R;

public class AllQuizActivity extends AppCompatActivity {

    RelativeLayout firstQuizBox, secondQuizBox, thirdQuizBox, fourthQuizBox, challengeBox;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_quiz);

        //Hooks
        firstQuizBox = findViewById(R.id.quiz_First);
        secondQuizBox = findViewById(R.id.quiz_Second);
        thirdQuizBox = findViewById(R.id.quiz_Third);
        fourthQuizBox = findViewById(R.id.quiz_Fourth);
        challengeBox = findViewById(R.id.challenge_box_set);
        backBtn = findViewById(R.id.backButtonPress);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllQuizActivity.this, UserDashboard.class);
                startActivity(intent);
            }
        });

        firstQuizBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveQuestionsForFirstQuiz("Quiz1");
            }
        });
        secondQuizBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveQuestionsForFirstQuiz("Quiz2");
            }
        });
        thirdQuizBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveQuestionsForFirstQuiz("Quiz3");
            }
        });
        fourthQuizBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveQuestionsForFirstQuiz("Quiz4");
            }
        });
        challengeBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveQuestionsForFirstQuiz("Challenge");
            }
        });
    }

    private void retrieveQuestionsForFirstQuiz(String quizType) {
        Intent intent = new Intent(AllQuizActivity.this, QuizQuestions.class);
        intent.putExtra("quizType", quizType);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AllQuizActivity.this, UserDashboard.class);
        startActivity(intent);
    }
}