package com.prototype.sprout.model;

// Key Encapsulation for passing args
public class BundleKey {

    private static final String KEY_EULA = "eula.key";
    private static final String KEY_WAKE_HOUR = "wakeHour.key";
    private static final String KEY_WAKE_MINUTE = "wakeMinute.key";
    private static final String KEY_SLEEP_HOUR = "sleepHour.key";
    private static final String KEY_SLEEP_MINUTE = "sleepMinute.key";
    private static final String KEY_NICKNAME = "nickname.key";
    private static final String KEY_ANALYSIS = "analysis.key";

    public String getKEY_EULA() {
        return KEY_EULA;
    }

    public String getKEY_WAKE_HOUR() {
        return KEY_WAKE_HOUR;
    }

    public String getKEY_WAKE_MINUTE() {
        return KEY_WAKE_MINUTE;
    }

    public String getKEY_SLEEP_HOUR() {
        return KEY_SLEEP_HOUR;
    }

    public String getKEY_SLEEP_MINUTE() {
        return KEY_SLEEP_MINUTE;
    }

    public String getKEY_NICKNAME() {
        return KEY_NICKNAME;
    }

    public String getKEY_ANALYSIS(){
        return KEY_ANALYSIS;
    }
}
