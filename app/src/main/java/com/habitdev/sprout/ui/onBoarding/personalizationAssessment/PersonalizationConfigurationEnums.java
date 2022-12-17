package com.habitdev.sprout.ui.onBoarding.personalizationAssessment;

public enum PersonalizationConfigurationEnums {

    POSITION("POSITION.INT"),
    SELECTED("SELECTED.STRING");

    private final String value;

    PersonalizationConfigurationEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
