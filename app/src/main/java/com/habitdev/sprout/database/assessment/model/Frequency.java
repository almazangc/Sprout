package com.habitdev.sprout.database.assessment.model;

public enum Frequency {
// Basic for numerical representaion of frequency. Posive and Negative
    //for negative Question
    NEVER_(new Choices("Never", 1)),
    SELDOM_(new Choices("Seldom", 2)),
    RARELY_(new Choices("Rarely", 3)),
    OCCASIONALLY_(new Choices("Occasionally", 4)),
    SOMETIMES_(new Choices("Sometimes", 5)),
    OFTEN_(new Choices("Often", 6)),
    USUALLY_(new Choices("Usually", 7)),
    REGULARLY_(new Choices("Regularly", 8)),
    ALWAYS_(new Choices("Always", 9)),

    //for positive question
    NEVER(new Choices("Never", 9)),
    SELDOM(new Choices("Seldom", 8)),
    RARELY(new Choices("Rarely", 7)),
    OCCASIONALLY(new Choices("Occasionally", 6)),
    SOMETIMES(new Choices("Sometimes", 5)),
    OFTEN(new Choices("Often", 4)),
    USUALLY(new Choices("Usually", 3)),
    REGULARLY(new Choices("Regularly", 2)),
    ALWAYS(new Choices("Always", 1))
    ;


    private final Choices choices;

    Frequency(Choices choices) {
        this.choices = choices;
    }

    public Choices getValue() {
        return choices;
    }
}
