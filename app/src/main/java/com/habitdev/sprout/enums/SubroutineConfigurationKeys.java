package com.habitdev.sprout.enums;

/**
 * <p> Contains defined keys for shared preference, and savedInstances in handling configuration changes for Subroutine Menu
 */
public enum SubroutineConfigurationKeys {
    SUBROUTINE_SHAREDPREF("SUBROUTINE_SHAREDPREF.PREF"),
    SUBROUTINE_MODIFY_SHAREDPREF("SUBROUTINE_MODIFY_SHAREDPREF.PREF"),
    SUBROUTINE_MODIFY_DIALOG_SHAREDPREF("SUBROUTINE_MODIFY_DIALOG_SHARED.PREF"),
    SUBROUTINE_ADAPTER_SHAREDPREF("SUBROUTINE_ADAPTER_SHARED.PREF"),
    ITEM_POSITION_GSON("ITEM_POSITION_GSON.STRING"),
    IS_ON_SUBROUTINE_MODIFY("IS_ON_SUBROUTINE_MODIFY.BOOL"),
    IS_ON_SUBROUTINE_UPDATE("IS_ON_SUBROUTINE_UPDATE.BOOL"),
    ITEM_POSITION("ITEM_POSITION.INT"),
    IS_ON_SUBROUTINE_INSERT("IS_ON_SUBROUTINE_INSERT.BOOL"),
    TITLE("TITLE.STRING"),
    DESCRIPTION("DESCRIPTION.STRING"),
    CURRENT_SELECTED_COLOR("SELECTED_COLOR.INT"),
    OLD_SELECTED_COLOR("OLD_SELECTED_COLOR.INT"),
    HINT_TEXT("HINT.STRING"),
    HABIT("HABIT.HABIT");

    private final String key;

    SubroutineConfigurationKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
