package com.habitdev.sprout.database.assessment.model;

public enum Frequency {
//Numerical value | weight for each choices representaion of frequency for choices in assessment tool.

    //Never: 0%
    //Seldom: 10%
    //Rarely: 20%
    //Occasionally: 30%
    //Sometimes: 40%
    //Often: 50%
    //Usually: 60%
    //Regularly: 70%
    //Always: 100%

    //for negative Question
    NEVER_(new Choices("Never", 0)),
    SELDOM_(new Choices("Seldom", 0.1d)),
    RARELY_(new Choices("Rarely", 0.2d)),
    OCCASIONALLY_(new Choices("Occasionally", .3d)),
    SOMETIMES_(new Choices("Sometimes", .4d)),
    OFTEN_(new Choices("Often", .5)),
    USUALLY_(new Choices("Usually", .6d)),
    REGULARLY_(new Choices("Regularly", .7d)),
    ALWAYS_(new Choices("Always", 1)),

    //Never: 100%
    //Seldom: 70%
    //Rarely: 60%
    //Occasionally: 50%
    //Sometimes: 40%
    //Often: 30%
    //Usually: 20%
    //Regularly: 10%
    //Always: 0%F

    //for positive question
    NEVER(new Choices("Never", 1)),
    SELDOM(new Choices("Seldom", .7d)),
    RARELY(new Choices("Rarely", .6d)),
    OCCASIONALLY(new Choices("Occasionally", .5d)),
    SOMETIMES(new Choices("Sometimes", .4d)),
    OFTEN(new Choices("Often", .3d)),
    USUALLY(new Choices("Usually", .2d)),
    REGULARLY(new Choices("Regularly", .1d)),
    ALWAYS(new Choices("Always", 0));

    private final Choices choices;

    Frequency(Choices choices) {
        this.choices = choices;
    }

    public Choices getValue() {
        return choices;
    }
}
