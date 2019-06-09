package com.upgrad.quizing;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ScoreSheetActivity extends AppCompatActivity {
    private ArrayList<Score> scoreList = new ArrayList<Score>();
    private RecyclerView recyclerView;
    private ScoreAdapter mAdapter;
    private ScoreClass scoreClass;
    String quizname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_sheet);

        quizname = getIntent().getStringExtra("QUIZNAME");
        prepareScoreData();


    }

    private void prepareScoreData() {


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Quiz").child(quizname+"score");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String username = (String) dataSnapshot1.child("username").getValue();
                        String quizname = (String) dataSnapshot1.child("quizname").getValue();
                        int score = Integer.parseInt((String) dataSnapshot1.child("score").getValue());
                        Score scoreClass = new Score(score+" marks",username,quizname);
                        scoreList.add(scoreClass);


                }

                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

                mAdapter = new ScoreAdapter(scoreList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);


                recyclerView.addItemDecoration(new DividerItemDecoration(ScoreSheetActivity.this, LinearLayoutManager.VERTICAL));

                // set the adapter
                recyclerView.setAdapter(mAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
