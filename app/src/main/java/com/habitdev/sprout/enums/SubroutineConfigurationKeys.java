package com.habitdev.sprout.enums;

/**
 * <p> Contains defined keys for shared preference, and savedInstances in handling configuration changes for Subroutine Menu
 */
public enum SubroutineConfigurationKeys {

    SUBROUTINE_SHAREDPREF("SUBROUTINE_SHAREDPREF.PREF"),
    SUBROUTINE_MODIFY_SHAREDPREF("SUBROUTINE_MODIFY_SHAREDPREF.PREF"),
    IS_ON_SUBROUTINE_MODIFY("IS_ON_SUBROUTINE_MODIFY.BOOL"),
    HABIT("HABIT.HABIT");

    private final String value;

    SubroutineConfigurationKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
