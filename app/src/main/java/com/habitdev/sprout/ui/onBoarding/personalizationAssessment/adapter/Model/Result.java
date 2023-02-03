package com.habitdev.sprout.ui.onBoarding.personalizationAssessment.adapter.Model;

import androidx.annotation.NonNull;

public class Result {
    private final long habit_uid;
    private double score;
    private int total_count;

    public Result() {
        this.score = 0;
        this.total_count = 0;
        this.habit_uid = -1;
    }

    public Result(long habit_uid, double score, int total_count) {
        this.habit_uid = habit_uid;
        this.score = score;
        this.total_count = total_count;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
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

    @NonNull
    @Override
    public String toString() {
        return "Result{" +
                "uid=" + habit_uid +
                ", score=" + score +
                ", total_count=" + total_count +
                '}';
    }
}
