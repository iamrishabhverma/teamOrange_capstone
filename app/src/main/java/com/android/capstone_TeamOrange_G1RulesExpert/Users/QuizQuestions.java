package com.android.capstone_TeamOrange_G1RulesExpert.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.capstone_TeamOrange_G1RulesExpert.HelperClasses.LocaleHelper;
import com.android.capstone_TeamOrange_G1RulesExpert.HelperClasses.QuestionsClass;
import com.android.q1learningapp.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestions extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private TextView queCounter, quizQuestion, currentScore;
    private ImageView QuitQuiz, imageUrl;
    private RelativeLayout optionsSet;
    private Button nextBtn, optionOne, optionTwo, optionThree, optionFour;
    List<QuestionsClass> questionList;
    private int count = 0;
    private int position = 0;
    private int score = 0;
    private String quizNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quiz_questions);

        queCounter = findViewById(R.id.q_question_Count);
        quizQuestion = findViewById(R.id.q_quiz_Questions);
        currentScore = findViewById(R.id.current_score_part);
        imageUrl = findViewById(R.id.questionImage);
        optionsSet = findViewById(R.id.q_options_container);
        nextBtn = findViewById(R.id.q_next_question);
        QuitQuiz = findViewById(R.id.QuitQuiz);

        optionOne = findViewById(R.id.q_option_1);
        optionTwo = findViewById(R.id.q_option_2);
        optionThree = findViewById(R.id.q_option_3);
        optionFour = findViewById(R.id.q_option_4);

        quizNumber = getIntent().getStringExtra("quizType");

        //Get Questions List from Model
        getQuestionsList();
        QuitQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogBox();
            }
        });

    }


    private void getQuestionsList() {

        questionList = new ArrayList<>();
        String currentLocale = LocaleHelper.getLanguage(QuizQuestions.this);

        if(quizNumber.equals("Challenge")){
            int MAX = 40;
            int startIndex = (int)(Math.random() * MAX +1);
            myRef.child("Quiz4").child("questions").child(currentLocale)
                    .orderByChild("orderNo")
                    .startAt(startIndex)
                    .endAt(startIndex + 3).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    if(datasnapshot.exists()){
                        for (DataSnapshot snapshot: datasnapshot.getChildren() ){
                            questionList.add(snapshot.getValue(QuestionsClass.class));
                        }
                        if(questionList.size() > 0){
                            setQuestion(position);
                            playAnim(quizQuestion, 0, questionList.get(position).getQuestion());
                            Glide.with(imageUrl.getContext())
                                    .load(questionList.get(position).getImageUrl())
                                    .override(250,250)
                                    .into(imageUrl);
                            //Option Click Functionality
                            for (int j = 0; j< 4 ; j++){
                                optionsSet.getChildAt(j).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        checkAnswer((Button) view);
                                    }
                                });
                            }
                            nextBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nextBtn.setEnabled(false);
                                    nextBtn.setAlpha(0.7f);
                                    enableOption(true);
                                    position++;
                                    /*Toast.makeText(QuizQuestions.this, position + "-" + questionList.size(), Toast.LENGTH_SHORT).show();*/
                                    if(position == questionList.size()){
                                        // No More Questions Left - Go to Score Activity
                                        Intent intent = new Intent(QuizQuestions.this, ScoresActivity.class);
                                        String _totalQuestions = String.valueOf(questionList.size());
                                        String _totalScores = String.valueOf(score);
                                        //Pass all fields to the next activity
                                        intent.putExtra("totalScores", _totalScores);
                                        intent.putExtra("totalQues", _totalQuestions);
                                        intent.putExtra("QuizNo", quizNumber);
                                        startActivity(intent);
                                    }
                                    count = 0;
                                    if(position < questionList.size()){
                                        playAnim(quizQuestion, 0, questionList.get(position).getQuestion());
                                        Glide.with(imageUrl.getContext())
                                                .load(questionList.get(position).getImageUrl())
                                                .override(300,300)
                                                .into(imageUrl);
                                    }
                                }
                            });
                        }
                        else{
                            finish();
                            // Data Does Not Exist
                            Toast.makeText(QuizQuestions.this,"Data Does Not Exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        // Data Does Not Exist
                        Toast.makeText(QuizQuestions.this,"Data Does Not Exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Data Dose Not Exist
                    Toast.makeText(QuizQuestions.this, error.getMessage() ,Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            myRef.child(quizNumber).child("questions").child(currentLocale).orderByChild("orderNo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    if (datasnapshot.exists()) {
                        for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                            questionList.add(snapshot.getValue(QuestionsClass.class));
                        }
                        if (questionList.size() > 0) {
                            setQuestion(position);
                            playAnim(quizQuestion, 0, questionList.get(position).getQuestion());
                            Glide.with(imageUrl.getContext())
                                    .load(questionList.get(position).getImageUrl())
                                    .override(250, 250)
                                    .into(imageUrl);
                            //Option Click Functionality
                            for (int j = 0; j < 4; j++) {
                                optionsSet.getChildAt(j).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        checkAnswer((Button) view);
                                    }
                                });
                            }
                            nextBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nextBtn.setEnabled(false);
                                    nextBtn.setAlpha(0.7f);
                                    enableOption(true);
                                    position++;
                                    /*Toast.makeText(QuizQuestions.this, position + "-" + questionList.size(), Toast.LENGTH_SHORT).show();*/
                                    if (position == questionList.size()) {
                                        // No More Questions Left - Go to Score Activity
                                        Intent intent = new Intent(QuizQuestions.this, ScoresActivity.class);
                                        String _totalQuestions = String.valueOf(questionList.size());
                                        String _totalScores = String.valueOf(score);
                                        //Pass all fields to the next activity
                                        intent.putExtra("totalScores", _totalScores);
                                        intent.putExtra("totalQues", _totalQuestions);
                                        intent.putExtra("QuizNo", quizNumber);
                                        startActivity(intent);
                                    }
                                    count = 0;
                                    if (position < questionList.size()) {
                                        playAnim(quizQuestion, 0, questionList.get(position).getQuestion());
                                        Glide.with(imageUrl.getContext())
                                                .load(questionList.get(position).getImageUrl())
                                                .override(300, 300)
                                                .into(imageUrl);
                                    }
                                }
                            });
                        } else {
                            // Data Does Not Exist
                            quizQuestion.setText(getString(R.string.no_result_found));
                            optionOne.setText(getString(R.string.no_result_found));
                            optionTwo.setText(getString(R.string.no_result_found));
                            optionThree.setText(getString(R.string.no_result_found));
                            optionFour.setText(getString(R.string.no_result_found));
                            finish();
                            Toast.makeText(QuizQuestions.this, "Data Does Not Exist", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Data Does Not Exist
                        Toast.makeText(QuizQuestions.this, "Data Does Not Exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Data Dose Not Exist
                    Toast.makeText(QuizQuestions.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void setQuestion(int pos) {
        quizQuestion.setText(questionList.get(pos).getQuestion());
        optionOne.setText(questionList.get(pos).getOptionA());
        optionTwo.setText(questionList.get(pos).getOptionB());
        optionThree.setText(questionList.get(pos).getOptionC());
        optionFour.setText(questionList.get(pos).getOptionD());

    }

    private void playAnim(final View view, final int value, final String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(400)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        if (value == 0 && count < 4) {
                            String option = "";
                            if(count == 0){
                                //Option A
                                option = questionList.get(position).getOptionA();
                            }else if(count == 1){
                                option = questionList.get(position).getOptionB();
                            }
                            else if(count == 2){
                                option = questionList.get(position).getOptionC();
                            }
                            else if(count == 3){
                                option = questionList.get(position).getOptionD();
                            }
                            playAnim(optionsSet.getChildAt(count), 0 , option);
                            count++;
                        }
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        //Data change
                        if (value == 0) {
                            try {
                                ((TextView)view).setText(data);
                                queCounter.setText(position+1+"/"+questionList.size());
                                currentScore.setText(getString(R.string.current_score)+" "+score);
                            }catch (ClassCastException ex){
                                ((Button)view).setText(data);
                            }
                            view.setTag(data);
                            playAnim(view, 1, data);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
    }

    @SuppressLint("NewApi")
    private void checkAnswer(Button selectedOption){
        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        if(selectedOption.getText().toString().equals(questionList.get(position).getCorrectAnswer())){
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF478C4A")));
            score++;
        }
        else{
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            Button correctOption = (Button) optionsSet.findViewWithTag(questionList.get(position).getCorrectAnswer());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6CBF6F")));
        }
    }

    @SuppressLint("NewApi")
    private void enableOption(boolean enable){
        for (int i = 0; i< 4 ; i++){
            optionsSet.getChildAt(i).setEnabled(enable);
            if(enable){
                optionsSet.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }
    }

    public void showAlertDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizQuestions.this);
        builder.setMessage("You have to start over if you quit now, Continue?")
                .setCancelable(false)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(QuizQuestions.this, AllQuizActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        showAlertDialogBox();
    }
}