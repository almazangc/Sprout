package com.habitdev.sprout.enums;

/**
 * <p> Contains defined keys for shared preference, and savedInstances in handling configuration changes for Onboarding
 */
public enum OnBoardingConfigurationKeys {

    WAKEUP_SHAREDPREF("WAKEUP_SHARED.PREF"),
    SLEEP_SHAREDPREF("SLEEP_SHARED.PREF"),
    HOUR("HOUR.INT"),
    MINUTE("MINUTE.INT"),

    POSITION("POSITION.INT"),
    SELECTED("SELECTED.STRING");

    private final String value;

    OnBoardingConfigurationKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
