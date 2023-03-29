package com.habitdev.sprout.enums;

/**
 * Contains Milestone for Time Event eg, 18 days notify user has reach the average days of habits on reform
 */
public enum TimeMilestone {

    MIN_HABIT_BREAK_DAY(18),
    AVG_HABIT_BREAK_DAY(66),
    MAX_HABIT_BREAK_DAY(254),
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
