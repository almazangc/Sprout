package com.habitdev.sprout.enums;

/**
 * Contains Milestone for Time Event eg, 21 days notify user has reach the average days of habits on reform
 */
public enum TimeMilestone {

    AVG_HABIT_BREAK_DAY(21),
    DAILY(1),
    WEEKLY(7);

    private final int days;

    TimeMilestone(int days) {
       this.days = days;
    }

    public int getDays() {
        return days;
    }
}
