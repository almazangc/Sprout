package com.example.sprout.database.Assestment;

import android.content.Context;

import com.example.sprout.database.AppDatabase;

public class PopulateAssessmentDatabase {

    public final String DEFAULT_SELECTED = "unset";
    private final Context context;

    public PopulateAssessmentDatabase(Context context) {
        this.context = context;
    }

    public PopulateAssessmentDatabase() {
        this.context = null;
    }

    public void populateAssessmentDatabase() {
        insertNewQuestion("Which habit troubles you, This is a very long questions as an input to the app?", "Wasting Time", "Smoking and Drinking", "No Motivation to work", "Over Spending");
        insertNewQuestion("Which habit troubles you?", "Wasting Time", "Smoking and Drinking", "No Motivation to work", "Over Spending");
        insertNewQuestion("1: How often do you feel happy?", "Always", "Usually", "Seldom", "Never");
        insertNewQuestion("2: Question number 2 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("3: Question number 3 right?", "Yes not", "No Yes", "I don't know", "I don't care");
        insertNewQuestion("4: Question number 4 right?", "Yes", "Who are you?", "I don't know", "Life ain't fun");
        insertNewQuestion("5: Question number 5 right?", "Who care about that", "Maybe", "I don't know anymore", "I don't care");
        insertNewQuestion("6: Question number 6 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("7: Question number 7 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("8: Question number 8 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("9: Question number 9 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("10: Question number 10 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("11: Are you numb?", "Yes", "No", "I don't know", "Why should I care?");
    }

    private void insertNewQuestion(String question, String aSelect, String bSelect, String cSelect, String dSelect) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(context);
        appDatabase.assessmentDao().insert(new Assessment(question, aSelect, bSelect, cSelect, dSelect, DEFAULT_SELECTED));
    }
}
