package com.habitdev.sprout.ui.habit_assessment.adapter.Model;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.util.List;

public class Result {
    private final long habit_uid;
    private double recommendation_score;
    private int total_count;

    public Result() {
        this.recommendation_score = 0;
        this.total_count = 0;
        this.habit_uid = -1;
    }

    public Result(long habit_uid, double recommendation_score, int total_count) {
        this.habit_uid = habit_uid;
        this.recommendation_score = recommendation_score;
        this.total_count = total_count;
    }

    public double getRecommendation_score() {
        return recommendation_score;
    }

    public void setRecommendation_score(double recommendation_score) {
        this.recommendation_score = recommendation_score;
    }

    public double getTotal_count() {
        return (double) total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public long getHabit_uid() {
        return habit_uid;
    }

    public String getFormattedConfidenceScore(){
       return new DecimalFormat("##%").format(recommendation_score);
    }

    public Result getHighestConfidenceScore(List<Result> results) {
        if (results == null || results.isEmpty()) {
            return null;
        }

        Result highestConfidenceScoreResult = results.get(0);
        double highestConfidenceScore = highestConfidenceScoreResult.getRecommendation_score();

        for (int i = 1; i < results.size(); i++) {
            Result currentResult = results.get(i);
            double currentConfidenceScore = currentResult.getRecommendation_score();

            if (currentConfidenceScore > highestConfidenceScore) {
                highestConfidenceScore = currentConfidenceScore;
                highestConfidenceScoreResult = currentResult;
            }
        }

        return highestConfidenceScoreResult;
    }

    @NonNull
    @Override
    public String toString() {
        return "Result{" +
                "uid=" + habit_uid +
                ", score=" + recommendation_score +
                ", total_count=" + total_count +
                '}';
    }
}
