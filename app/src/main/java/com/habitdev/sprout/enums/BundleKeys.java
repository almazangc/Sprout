package com.habitdev.sprout.enums;

public enum BundleKeys {

    EULA("EULA?KEY"),
    WAKE_HOUR("WAKE_HOUR?KEY"),
    WAKE_MINUTE("WAKE_MINUTE?KEY"),
    SLEEP_HOUR("SLEEP_HOUR?KEY"),
    SLEEP_MINUTE("SLEEP_MINUTE?KEY"),
    NICKNAME("NICKNAME?KEY"),
    ANALYSIS("ANALYSIS?KEY"),
    JOURNAL_NOTE("JOURNAL_NOTE?KEY");

    private final String KEY;

    BundleKeys(String KEY){
        this.KEY = KEY;
    }

    public String getKEY() {
        return KEY;
    }
}
