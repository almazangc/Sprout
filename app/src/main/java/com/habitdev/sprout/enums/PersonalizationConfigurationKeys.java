package com.habitdev.sprout.enums;

public enum PersonalizationConfigurationKeys {

    POSITION("POSITION.INT"),
    SELECTED("SELECTED.STRING");

    private final String value;

    PersonalizationConfigurationKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
