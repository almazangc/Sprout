package com.habitdev.sprout.enums;

public enum TimeMilestone {

    AVG_HABIT_BREAK_DAY(21);

    int days;

    TimeMilestone(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}
