package com.prototype.sprout.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.prototype.sprout.database.assessment.Assessment;
import com.prototype.sprout.database.assessment.AssessmentDao;
import com.prototype.sprout.database.habit.Habit;
import com.prototype.sprout.database.habit.HabitDao;
import com.prototype.sprout.database.user.User;
import com.prototype.sprout.database.user.UserDao;

@Database(entities = {User.class, Assessment.class, Habit.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAssessmentAsyncTask(INSTANCE).execute();
//            new PopulateUserAsyncTask(INSTANCE).execute();
            new PopulatedHabitAsyncTask(INSTANCE).execute();
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

    public abstract HabitDao habitDao();


    private static class PopulatedHabitAsyncTask extends AsyncTask<Void, Void, Void>{

        private HabitDao habitDao;

        public PopulatedHabitAsyncTask(AppDatabase instance) {
            this.habitDao = instance.habitDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            habitDao.insert(new Habit("Stress Eating", Converter.fromString("[3,5,8]")));
            habitDao.insert(new Habit("Poor Sleep Management", Converter.fromString("[3,4,5,6]")));
            habitDao.insert(new Habit("Drinking", Converter.fromString("[2,5,8,9,10]")));
            habitDao.insert(new Habit("Gambling", Converter.fromString("[1,4,7,8,9]")));
            habitDao.insert(new Habit("Poor spending habits", Converter.fromString("[1,3,6,7,8]")));
            habitDao.insert(new Habit("Excessive profanity", Converter.fromString("[1,4,5,6,7]")));
            habitDao.insert(new Habit("Multi-tasking", Converter.fromString("[1,3,6,8,9]")));
            habitDao.insert(new Habit("Smoking", Converter.fromString("[1,3,7,9]")));
            habitDao.insert(new Habit("Procrastination", Converter.fromString("[1,2,4,6,9]")));
            habitDao.insert(new Habit("Overthinking and worrying", Converter.fromString("[1,2,4,5]")));
            habitDao.insert(new Habit("Cluttering your living/workspace", Converter.fromString("[1,4,7,9]")));
            habitDao.insert(new Habit("Leaving the toilet seat up", Converter.fromString("[1,3,6,8]")));
            habitDao.insert(new Habit("Leaving clothes on the floor",Converter.fromString("[0,3,5,6]")));
            habitDao.insert(new Habit("Taking things personally", Converter.fromString("[1,4,5,6]")));
            habitDao.insert(new Habit("Overusing slang", Converter.fromString("[0,3,5,6]")));
            habitDao.insert(new Habit("Alot", Converter.fromString("[1,4,6,7]")));
            return null;
        }
    }

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
            userDao.insert(new User("Vit", "Alien", 0, 8, 30, 20, 55, true, true, true));
            return null;
        }
    }
}
