package com.upgrad.quizing;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class QuestionScreen extends AppCompatActivity {
    Button option1, option2, option3, option4;
    TextView questionTitle, questionNumber, timer,score;
    ScoreClass scoreClass;
    String correctOption;
    ImageView image;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressDialog progressDialog;
    ArrayList<Question> setOfQuestions;
    private String correctId;
    CountDownTimer countDownTimer;
    int j=0;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_screen);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        timer = findViewById(R.id.timer);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        score = findViewById(R.id.score);
        questionNumber = findViewById(R.id.qno);
        questionTitle = findViewById(R.id.questiontitle);
        image = findViewById(R.id.imageView3);


        database = FirebaseDatabase.getInstance();
        scoreClass = new ScoreClass();

        setOfQuestions = new ArrayList<>();
        myRef = database.getReference("Quiz").child(scoreClass.getQuizName());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait !");
        progressDialog.show();

        fetchQuestion();

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               flowOfQuestions(option1.getText().toString());
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowOfQuestions(option2.getText().toString());
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowOfQuestions(option3.getText().toString());
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowOfQuestions(option4.getText().toString());
            }
        });

    }

    void flowOfQuestions(String option){
        if(j<setOfQuestions.size()){
            //reset views -> fetch next question
            if(option.equalsIgnoreCase(correctOption)){
                //add to score //correct answer
                scoreClass.setNoOfRight(scoreClass.getNoOfRight()+1);
                 scoreClass.setScore(scoreClass.getScore()+Integer.valueOf(score.getText().toString()));
            }else{//even if the question is right or wrong it should go to the next question if there are more questions
            scoreClass.setNoOfWrong(scoreClass.getNoOfWrong()+1);}
            timer.setText("");
            countDownTimer.cancel();
           Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/" +
                   "hostelapp-236008.appspot.com/o/images%2Fquizup.png?" +
                   "alt=media&token=d6ab1321-6470-48b0-aed2-a2173153111d").into(image);
            setQuestion(setOfQuestions.get(j));

        }else{
            //move to score activity // questions get over
            if(option.equalsIgnoreCase(correctOption)){
                //add to score //correct answer
                scoreClass.setNoOfRight(scoreClass.getNoOfRight()+1);
                scoreClass.setScore(scoreClass.getScore()+Integer.valueOf(score.getText().toString()));
            }else{
            scoreClass.setNoOfWrong(scoreClass.getNoOfWrong()+1);}
            Intent intent = new Intent(QuestionScreen.this,ScoreActivity.class);
            if (option.equals("timer")){
                intent.putExtra("SIGNAL",0);
            }else{
                intent.putExtra("SIGNAL",1);
            }
            countDownTimer.cancel();
            startActivity(intent);
                finish();
        }
    }

    private void fetchQuestion() {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                Question question = postSnapshot.getValue(Question.class);
                setOfQuestions.add(question);
                }
                progressDialog.hide();

                setQuestion(setOfQuestions.get(0));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value


                Log.w("TEST", "Failed to read value.", error.toException());
            }
        });
    }


    void setQuestion(Question question){
        questionTitle.setText(question.getQuestionTitle());
        option1.setText(question.getOption1());
        option2.setText(question.getOption2());
        option3.setText(question.getOption3());
        option4.setText(question.getOption4());

        questionNumber.setText(String.valueOf(j+1));
        correctId = question.getCorrectOption();
        score.setText(question.getQuestionScore());
        Glide.with(this).load(question.getQuestionImage()).into(image);
        correctOption = "option";

        switch (Integer.valueOf(correctId)){
            case 1: correctOption = question.getOption1();
                break;

            case 2:  correctOption = question.getOption2();
                break;

            case 3:  correctOption = question.getOption3();
                break;

            case 4:  correctOption = question.getOption4();
                break;

            default: correctOption = question.getOption1();

        }
        j++;//increase count of question
        countDownTimer= new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Time remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //take to finish activity
                // pass 1 (complete quiz submission) pass 2 (incomplete quiz submission)
             flowOfQuestions("timer");
            }
        }.start();

    }
}
