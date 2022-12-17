package com.habitdev.sprout.enums;

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
