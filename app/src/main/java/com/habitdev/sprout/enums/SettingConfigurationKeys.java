package com.habitdev.sprout.enums;

public enum SettingConfigurationKeys {
    SETTING_SHAREDPRED("SETTING_SHARED.PREF"),
    ISPROFILEMODIFIED("IS_PROFILE_MODIFIED.BOOL");

    private final String key;

    SettingConfigurationKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
