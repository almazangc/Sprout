package com.habitdev.sprout.enums;

public enum SettingConfigurationKeys {
    SETTING_SHAREDPRED("SETTING_SHARED.PREF"),
    IS_CUSTOM_PROFILE("IS_CUSTOM_PROFILE.BOOL"),
    CUSTOM_PROFILE_PATH("CUSTOM_PROFILR_PATH.STRING");

    private final String key;

    SettingConfigurationKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
