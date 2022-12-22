package com.habitdev.sprout.enums;

public enum AnalyticConfigurationKeys {
    ANALYTIC_SHAREDPREF("ANALYTIC_SHARED.PREF"),
    IS_ON_ITEM_CLICK("IS_ON_ITEM_CLICK.BOOL"),
    HABIT("HABIT.HABIT")
    ;

    private final String value;

    AnalyticConfigurationKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
