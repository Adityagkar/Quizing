package com.upgrad.quizing;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.MyViewHolder> {

    private List<Score> scoreList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView studentName, score, subject;

        public MyViewHolder(View view) {
            super(view);
            studentName = (TextView) view.findViewById(R.id.studentname);
            score = (TextView) view.findViewById(R.id.score);
            subject = (TextView) view.findViewById(R.id.subject);
        }
    }


    public ScoreAdapter(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Score scoreClass = scoreList.get(position);
        holder.studentName.setText(scoreClass.getUsername());
        holder.score.setText(scoreClass.getScore()+"");
        holder.subject.setText(scoreClass.getQuizname());
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }
}