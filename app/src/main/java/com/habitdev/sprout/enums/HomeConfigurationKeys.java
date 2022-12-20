package com.habitdev.sprout.enums;

/**
 * <p> Contains defined keys for shared preference, and savedInstances in handling configuration changes for Home Menu
 */
public enum HomeConfigurationKeys {

    HOME_SHAREDPREF("HOME_SHARED.PREF"),
    HOME_ADD_DEFAULT_SHAREDPREF("HOME_ADD_DEFAULT_SHARED.PREF"),
    HOME_ADD_NEW_SHAREDPREF("HOME_ADD_NEW_SHARED.PREF"),
    HOME_HABIT_ON_MODIFY_SHARED_PREF("HOME_HABIT_ON_MODIFY_SHARED.PREF"),

    IS_ON_ADD_DEFAULT("ON_ADD_DEAFAULT.BOOL"),
    IS_ON_ADD_NEW("ON_ADD_NEW.BOOL"),
    IS_ON_ITEM_CLICK("ON_ITEM_CLICK"),
    IS_ON_MODIFY("ON_MODIFY.BOOL"),
    CURRENT_SELECTED_COLOR("SELECTED_COLOR.INT"),
    OLD_SELECTED_COLOR("OLD_SELECTED_COLOR.INT"),
    VIEW_VISIBILITY("VIEW_VISIBILITY.INT"),
    SELECTED_HABIT("SELECTED_HABIT.STRING"),
    SUBROUTINELISTGSON("SUBROUTINELISTGSON.STRING"),
    HABIT("HABIT.HABIT"),
    TITLE("TITLE.STRING"),
    DESCRIPTION("DESCRIPTION.STRING"),
    POSITION("POSITION.INT"),
    HINT_TEXT("HINT.STRING");

    private final String value;

    HomeConfigurationKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
