package com.habitdev.sprout.database.assessment.model;

public enum Frequency {

//Numerical value | weight for each choices representaion of frequency for choices in assessment tool.
    //Never: 0%
    //Rarely: 20%
    //Sometimes: 60%
    //Often: 50%
    //Always: 100%

    //for negative Question //max 5 choice and numerical
    NEVER_(new Choices("Never", 0)),
    RARELY_(new Choices("Rarely", 0.4d)),
    SOMETIMES_(new Choices("Sometimes", .6d)),
    OFTEN_(new Choices("Often", .8)),
    ALWAYS_(new Choices("Always", 1)),

    //Never: 100%
    //Rarely: 80%
    //Sometimes: 60%
    //Often: 40%
    //Always: 0%F

    //for positive question
    NEVER(new Choices("Never", 1)),
    RARELY(new Choices("Rarely", .8d)),
    SOMETIMES(new Choices("Sometimes", .6d)),
    OFTEN(new Choices("Often", .4d)),
    ALWAYS(new Choices("Always", 0));

    private final Choices choices;

    Frequency(Choices choices) {
        this.choices = choices;
    }

    public Choices getValue() {
        return choices;
    }
}
