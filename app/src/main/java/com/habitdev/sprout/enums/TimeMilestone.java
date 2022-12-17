package com.habitdev.sprout.enums;

/**
 * Contains Milestone for Time Event eg, 21 days notify user has reach the average days of habits on reform
 */
public enum TimeMilestone {

    AVG_HABIT_BREAK_DAY();

    private final int days;

    TimeMilestone() {
        this.days = 21;
    }

    public int getDays() {
        return days;
    }
}
