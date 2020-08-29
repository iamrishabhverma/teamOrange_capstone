package com.android.capstone_TeamOrange_G1RulesExpert.Databases;

public class ScoresHelper {

    String finalScores;
    String totalScores;
    String quizNumber;
    String userId;
    String date;

    public ScoresHelper() {
    }

    public ScoresHelper(String finalScores, String totalScores, String quizNumber, String userId, String date) {
        this.finalScores = finalScores;
        this.totalScores = totalScores;
        this.quizNumber = quizNumber;
        this.userId = userId;
        this.date = date;
    }

    public String getFinalScores() {
        return finalScores;
    }

    public void setFinalScores(String finalScores) {
        this.finalScores = finalScores;
    }

    public String getTotalScores() {
        return totalScores;
    }

    public void setTotalScores(String totalScores) {
        this.totalScores = totalScores;
    }

    public String getQuizNumber() {
        return quizNumber;
    }

    public void setQuizNumber(String quizNumber) {
        this.quizNumber = quizNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
