package com.example.sprout.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.sprout.database.Assestment.AssessmentDao;
import com.example.sprout.database.Assestment.Assessment;
import com.example.sprout.database.User.User;
import com.example.sprout.database.User.UserDao;

@Database(entities = {User.class, Assessment.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract AssessmentDao assessmentDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .build();
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAssessmentAsyncTask(INSTANCE).execute();
        }
    };

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
            assessmentDao.insert(new Assessment("1: How often do you feel happy?", "Always", "Usually", "Seldom", "Never", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("2: Question number 2 right?", "Yes", "No", "I dont know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("3: Question number 3 right?", "Yes not", "No Yes", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("4: Question number 4 right?", "Yes", "Who are you?", "I don't know", "Life ain't fun", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("5: Question number 5 right?", "Who care about that", "Maybe", "I don't know anymore", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("6: Question number 6 right?", "Yes", "No", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("7: Question number 7 right?", "Yes", "No", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("8: Question number 8 right?", "Yes", "No", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("9: Question number 9 right?", "Yes", "No", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("10: Question number 10 right?", "Yes", "No", "I don't know", "I don't care",DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("11: Are you numb?", "Yes", "No", "I don't know", "Why should I care?", DEFAULT_SELECTED));

            return null;
        }
    };
}
