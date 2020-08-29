package com.android.capstone_TeamOrange_G1RulesExpert.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.capstone_TeamOrange_G1RulesExpert.Databases.ScoresHelper;
import com.android.capstone_TeamOrange_G1RulesExpert.Databases.SessionManager;
import com.android.capstone_TeamOrange_G1RulesExpert.HelperClasses.LocaleHelper;
import com.android.q1learningapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllScores extends AppCompatActivity {

    private LinearLayout scores_layout;
    TextView allScoresTotal, allScoresQUizNo, allScoresDate;
    List<ScoresHelper> scoresList;
    private int databaseOutput;
    private int position = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_scores);

/*        allScoresTotal = findViewById(R.id.allScores_output);
        allScoresQUizNo = findViewById(R.id.allScores_quizNo);
        allScoresDate = findViewById(R.id.allScores_date);*/
        scores_layout = findViewById(R.id.scores_layout);

        getScoresList();

        backBtn = findViewById(R.id.backButtonPress);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllScores.this, UserDashboard.class);
                startActivity(intent);
            }
        });
    }

    private void getScoresList(){
        scoresList = new ArrayList<>();
        String currentLocale = LocaleHelper.getLanguage(AllScores.this);

        //Start Session
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUserDetailsFromSession();

        //Get all the data from Session
        String phoneNumber = userDetails.get(SessionManager.KEY_PHONE_NUMBER);

        assert phoneNumber != null;
        reference.child("Scores").child(phoneNumber).orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.exists()){
                    for (DataSnapshot snapshot: datasnapshot.getChildren() ){
                        scoresList.add(snapshot.getValue(ScoresHelper.class));
                        databaseOutput = scoresList.size();

                        //Toast.makeText(AllScores.this,"Data Exist"+ position, Toast.LENGTH_SHORT).show();
                        if(position < databaseOutput){
                            createNewScoreLayout(position);
                        }
                        position++;
                    }

                }
                else{
                    createNoScoreLayout();
                    Toast.makeText(AllScores.this,"Data Does Not Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Data Dose Not Exist
                Toast.makeText(AllScores.this, error.getMessage() ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNewScoreLayout(int i){
        //allScoresTotal.setText(scoresList.get(position).getDate());
        //Toast.makeText(AllScores.this,"Data Does Not Exist", Toast.LENGTH_SHORT).show();

        View layout2 = LayoutInflater.from(this).inflate(R.layout.scorer_item, scores_layout, false);

        TextView scoresOutput = (TextView) layout2.findViewById(R.id.scores_output);
        TextView scoresDate = (TextView) layout2.findViewById(R.id.scores_date);
        TextView scoresQuizno = (TextView) layout2.findViewById(R.id.scores_quizNo);

        String scoresOutputTotal = scoresList.get(i).getFinalScores() + "/" + scoresList.get(i).getTotalScores();
        scoresOutput.setText(scoresOutputTotal);
        scoresDate.setText(scoresList.get(i).getDate());
        scoresQuizno.setText(scoresList.get(i).getQuizNumber());
        scores_layout.addView(layout2);
    }

    private void createNoScoreLayout(){
        View layout2 = LayoutInflater.from(this).inflate(R.layout.scorer_item, scores_layout, false);

        TextView scoresOutput = (TextView) layout2.findViewById(R.id.scores_output);
        TextView scoresDate = (TextView) layout2.findViewById(R.id.scores_date);
        TextView scoresQuizno = (TextView) layout2.findViewById(R.id.scores_quizNo);

        scoresOutput.setText("0");
        scoresDate.setText("");
        scoresQuizno.setText(getString(R.string.no_scores_to_show_yet));
        scores_layout.addView(layout2);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AllScores.this, UserDashboard.class);
        startActivity(intent);
    }

}

