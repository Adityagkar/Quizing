package com.upgrad.quizing;

public class Score {
    String score;
    String username;
    String quizname;

    public Score(String score, String username, String quizname) {
        this.score = score;
        this.username = username;
        this.quizname = quizname;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQuizname() {
        return quizname;
    }

    public void setQuizname(String quizname) {
        this.quizname = quizname;
    }
}
