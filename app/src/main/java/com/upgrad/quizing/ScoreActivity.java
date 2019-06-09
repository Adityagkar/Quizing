package com.upgrad.quizing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class ScoreActivity extends AppCompatActivity {

    TextView finalScore,  rightQuestion, wrongQuestion, timeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        timeout = findViewById(R.id.timeout);
        finalScore = findViewById(R.id.scores);
        rightQuestion = findViewById(R.id.textView10);
        wrongQuestion = findViewById(R.id.textView12);

        int signal = getIntent().getIntExtra("SIGNAL",1);

        if(signal==0){
            timeout.setVisibility(View.VISIBLE);
        }else{
            timeout.setVisibility(View.GONE);
        }

        ScoreClass scoreClass = new ScoreClass();
        finalScore.setText(scoreClass.getScore()+"");
        rightQuestion.setText(scoreClass.getNoOfRight()+"");
        wrongQuestion.setText(scoreClass.getNoOfWrong()+"");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Quiz");
        String temp = String.valueOf(UUID.randomUUID());
        myRef.child(scoreClass.getQuizName()+"score").child(temp).child("username").setValue(scoreClass.getUserName());
        myRef.child(scoreClass.getQuizName()+"score").child(temp).child("score").setValue(scoreClass.getScore()+"");
        myRef.child(scoreClass.getQuizName()+"score").child(temp).child("quizname").setValue(scoreClass.getQuizName());


    }
}
