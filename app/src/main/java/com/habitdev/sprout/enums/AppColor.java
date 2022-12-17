package com.habitdev.sprout.enums;

/**
 * Contains defined colors for Habit, Subroutine, and Notes
 */
public enum AppColor {
    CLOUDS("CLOUDS"),
    ALZARIN("ALZARIN"),
    AMETHYST("AMETHYST"),
    BRIGHT_SKY_BLUE("BRIGHT_SKY_BLUE"),
    NEPHRITIS("NEPHRITIS"),
    SUNFLOWER("SUNFLOWER");

    private final String color;

    AppColor(String color){
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
