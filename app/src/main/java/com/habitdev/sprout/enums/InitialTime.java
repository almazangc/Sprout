package com.habitdev.sprout.enums;

/**
 * Contains Initial Time for Wake and Sleep Time for Onboarding
 */
public enum InitialTime {

    WAKE_HOUR(8),
    WAKE_MINUTE(0),
    SLEEP_HOUR(20),
    SLEEP_MINUTE(30);

    private final int value;

    InitialTime(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
