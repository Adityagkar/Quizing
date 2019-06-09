package com.upgrad.quizing;

public class ScoreClass {
    static String userName;
    static int score=0;
    static int noOfRight=0;
    static int noOfWrong=0;
    static String quizName;
    static String loginType;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public ScoreClass() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        ScoreClass.score = score;
    }

    public static int getNoOfRight() {
        return noOfRight;
    }

    public static void setNoOfRight(int noOfRight) {
        ScoreClass.noOfRight = noOfRight;
    }

    public static int getNoOfWrong() {
        return noOfWrong;
    }

    public static void setNoOfWrong(int noOfWrong) {
        ScoreClass.noOfWrong = noOfWrong;
    }
}
