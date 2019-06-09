package com.upgrad.quizing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {
    Button addQuestion, viewScore, startQuiz, logout;
    EditText noOfQuestion, quizName;
    TextView simpleHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        logout = findViewById(R.id.logout);
        addQuestion = findViewById(R.id.add);
        viewScore = findViewById(R.id.scoreview);
        noOfQuestion = findViewById(R.id.number);
        simpleHeading = findViewById(R.id.textView6);
        quizName = findViewById(R.id.number2);
        startQuiz = findViewById(R.id.view2);

        final ScoreClass scoreClass = new ScoreClass();


        if (scoreClass.getLoginType().equals("student")){
            startQuiz.setVisibility(View.VISIBLE);
        }else{
            addQuestion.setVisibility(View.VISIBLE);
        }

        viewScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(quizName.getText().toString().isEmpty()){
                    quizName.setError("Fill this !");
                }else {
                    Toast.makeText(WelcomeActivity.this, "Clicked !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WelcomeActivity.this, ScoreSheetActivity.class);
                    intent.putExtra("QUIZNAME", quizName.getText().toString());
                    startActivity(intent);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
            }
        });

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quizName.getText().toString().isEmpty()){
                    quizName.setError("Fill this !");
                }else {
                    scoreClass.setScore(0);
                    scoreClass.setNoOfWrong(0);
                    scoreClass.setNoOfRight(0);
                    Intent intent = new Intent(WelcomeActivity.this, QuestionScreen.class);
                    scoreClass.setQuizName(quizName.getText().toString());
                    startActivity(intent);
                }
            }
        });

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizName.getText().toString().isEmpty()) {
                    quizName.setError("Fill this !");
                } else {
                    noOfQuestion.setVisibility(View.VISIBLE);
                    simpleHeading.setVisibility(View.VISIBLE);
                    if (!noOfQuestion.getText().toString().isEmpty()) {
                        Intent intent = new Intent(WelcomeActivity.this, AddQuestion.class);
                        intent.putExtra("NUMBER", Integer.valueOf(noOfQuestion.getText().toString()));
                        intent.putExtra("NAME", quizName.getText().toString());
                        scoreClass.setQuizName(quizName.getText().toString());
                        startActivity(intent);
                    }
                }
            }
        });



    }
}
