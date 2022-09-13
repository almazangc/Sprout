package com.example.sprout.Database;

import android.content.Context;

public class PopulateAssestmentDatabase{

    private final Context context;

    public PopulateAssestmentDatabase(Context context) {
        this.context = context;
    }

    public void populateAssestmentDatabase(){
        insertNewQuestion("Which habit troubles you?", "Wasting Time", "Smoking and Drinking", "No Motivation to work", "Over Spending");
        insertNewQuestion("1: How often do you feel happy?", "Always", "Usually", "Seldom", "Never");
        insertNewQuestion("2: Question number 2 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("3: Question number 3 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("4: Question number 4 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("5: Question number 5 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("6: Question number 6 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("7: Question number 7 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("8: Question number 8 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("9: Question number 9 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("10: Question number 10 right?", "Yes", "No", "I dont know", "I don't care");
        insertNewQuestion("11: Are you numb?", "Yes", "No", "I dont know", "Why should I care?");
    }

    private void insertNewQuestion(String question, String aselect, String bselect, String cselect, String dselect){
        AppDatabase db = AppDatabase.getDbInstance(context);
        Assestment assestment = new Assestment();
        assestment.question = question;
        assestment.aselect = aselect;
        assestment.bselect = bselect;
        assestment.cselect = cselect;
        assestment.dselect = dselect;
        db.assestmentDao().insert(assestment);
    }
}
