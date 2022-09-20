package com.prototype.sprout.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.prototype.sprout.database.Assessment.Assessment;
import com.prototype.sprout.database.Assessment.AssessmentDao;
import com.prototype.sprout.database.User.User;
import com.prototype.sprout.database.User.UserDao;

@Database(entities = {User.class, Assessment.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAssessmentAsyncTask(INSTANCE).execute();
//            new PopulateUserAsyncTask(INSTANCE).execute();
        }
    };

    public static AppDatabase getDbInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .build();
        }
        return INSTANCE;
    }

    public abstract UserDao userDao();

    public abstract AssessmentDao assessmentDao();

    private static class PopulateAssessmentAsyncTask extends AsyncTask<Void, Void, Void> {

        private AssessmentDao assessmentDao;

        public PopulateAssessmentAsyncTask(AppDatabase instance) {
            assessmentDao = instance.assessmentDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            final String DEFAULT_SELECTED = "null";

            assessmentDao.insert(new Assessment("Question", "A", "B", "C", "D", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("Which habit troubles you, This is a very long questions as an input to the app?", "Wasting Time", "Smoking and Drinking", "No Motivation to work", "Over Spending", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("Which habit troubles you?", "Wasting Time", "Smoking and Drinking", "No Motivation to work", "Over Spending", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("How often do you feel happy?", "Always", "Usually", "Seldom", "Never", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("Do you like banana?", "Yes", "No", "I dont know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("Do you fear life?", "Yes not", "No Yes", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("If 1 + 1 - 2, What is the circumference of the sun?", "Yes?", "WTH", "I don't know", "Life ain't fun", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("Do you see you future?", "Who care about that", "A bit Foggy", "I don't know anymore", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("Is this a generic question?", "Yes", "No", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("What is the name of app?", "Sprout", "Dont Know", "Undeciced", "I wanna change, it sucks", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("Do you always owo?", "Yes", "Absolutely Not", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("Is this question number 10?", "Yes", "No","I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("If you answered yes then your wrong, this is question number 10?", "OK", "Meh", "LOL", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("Last Question?", "Yes", "No", "I don't know", "Why should I care?", DEFAULT_SELECTED));

            return null;
        }
    };

    private static class PopulateUserAsyncTask extends AsyncTask<Void, Void, Void> {

        private UserDao userDao;

        public PopulateUserAsyncTask(AppDatabase instance) {
            userDao = instance.userDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            userDao.insert(new User("Vit", "Alien", 8, 30, 20, 55, true));
            return null;
        }
    };
}
