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
import com.prototype.sprout.database.converters.ArrayListConverter;
import com.prototype.sprout.database.habit.Habit;
import com.prototype.sprout.database.habit.HabitDao;
import com.prototype.sprout.database.habit.sub_routine.Routine;
import com.prototype.sprout.database.habit.sub_routine.RoutineDao;
import com.prototype.sprout.database.user.User;
import com.prototype.sprout.database.user.UserDao;

@Database(entities = {User.class, Assessment.class, Habit.class, Routine.class}, version = 1)
@TypeConverters({ArrayListConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAssessmentAsyncTask(INSTANCE).execute();
            new PopulateUserAsyncTask(INSTANCE).execute();
            new PopulateRoutineAsyncTask(INSTANCE).execute();
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

    public abstract RoutineDao routineDao();


    private static class PopulatedHabitAsyncTask extends AsyncTask<Void, Void, Void>{

        private HabitDao habitDao;

        public PopulatedHabitAsyncTask(AppDatabase instance) {
            this.habitDao = instance.habitDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            habitDao.insert(new Habit("Stress Eating", "Eating a lot due to stress", ArrayListConverter.fromString("[3,5,8]")));
            habitDao.insert(new Habit("Poor Sleep Management", "Not Sleeeping on time daily", ArrayListConverter.fromString("[3,4,5,6]")));
            habitDao.insert(new Habit("Drinking", "Alcohol, Beverage, uncontrolled drinking" ,ArrayListConverter.fromString("[2,5,8,9,10]")));
            habitDao.insert(new Habit("Gambling", "Uncontrolled betting of cash" , ArrayListConverter.fromString("[1,4,7,8,9]")));
            habitDao.insert(new Habit("Poor spending habits", "Unable to resist from spending", ArrayListConverter.fromString("[1,3,6,7,8]")));
            habitDao.insert(new Habit("Excessive profanity", "Uncontrolled toxicity", ArrayListConverter.fromString("[1,4,5,6,7]")));
            habitDao.insert(new Habit("Multi-tasking", "Jumping from one task to another not lack of fucos", ArrayListConverter.fromString("[1,3,6,8,9]")));
            habitDao.insert(new Habit("Smoking", "Addicted to smoking, eg., vape",ArrayListConverter.fromString("[1,3,7,9]")));
            habitDao.insert(new Habit("Procrastination", "The action of delaying something", ArrayListConverter.fromString("[1,2,4,6,9]")));
            habitDao.insert(new Habit("Overthinking and worrying", "Thingking too much about something",ArrayListConverter.fromString("[1,2,4,5]")));
            habitDao.insert(new Habit("Cluttering your living/workspace", "Piledup, Messy or Unmange Living Room or Workspace",ArrayListConverter.fromString("[1,4,7,9]")));
            habitDao.insert(new Habit("Leaving the toilet seat up", "Some desc",ArrayListConverter.fromString("[1,3,6,8]")));
            habitDao.insert(new Habit("Leaving clothes on the floor", "Throwing used clothes anyone in the house",ArrayListConverter.fromString("[0,3,5,6]")));
            habitDao.insert(new Habit("Taking things personally", "arsagid jkjk", ArrayListConverter.fromString("[1,4,5,6]")));
            habitDao.insert(new Habit("Overusing slang", "Feeling slang", ArrayListConverter.fromString("[0,3,5,6]")));
            habitDao.insert(new Habit("Alot", "and more....",ArrayListConverter.fromString("[1,4,6,7]")));
            return null;
        }
    }

    private static class PopulateRoutineAsyncTask extends AsyncTask<Void, Void, Void> {

        private RoutineDao routineDao;

        public PopulateRoutineAsyncTask(AppDatabase instance) {
            routineDao = instance.routineDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            routineDao.insert(new Routine("Walking Dog", "Getting pet Accustomeed"));
            routineDao.insert(new Routine("Walking Dog", "Getting pet Accustomeed"));
            routineDao.insert(new Routine("Walking Dog", "Getting pet Accustomeed"));
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
            assessmentDao.insert(new Assessment("1: Question", "A", "B", "C", "D", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("2: Which habit troubles you, This is a very long questions as an input to the app?", "Wasting Time", "Smoking and Drinking", "No Motivation to work", "Over Spending", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("3: Which habit troubles you?", "Wasting Time", "Smoking and Drinking", "No Motivation to work", "Over Spending", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("4: How often do you feel happy?", "Always", "Usually", "Seldom", "Never", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("5: Do you like banana?", "Yes", "No", "I dont know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("6: Do you fear life?", "Yes not", "No Yes", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("7: If 1 + 1 - 2, What is the circumference of the sun?", "Yes?", "WTH", "I don't know", "Life ain't fun", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("8: Do you see you future?", "Who care about that", "A bit Foggy", "I don't know anymore", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("9: Is this a generic question?", "Yes", "No", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("10: What is the name of app?", "Sprout", "Dont Know", "Undeciced", "I wanna change, it sucks", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("11: Do you always owo?", "Yes", "Absolutely Not", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("12: Is this question number 10?", "Yes", "No","I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("13: If you answered yes then your wrong, this is question number 10?", "OK", "Meh", "LOL", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("14: Last Question?", "Yes", "No", "I don't know", "Why should I care?", DEFAULT_SELECTED));

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
            userDao.insert(new User("Vit", "Alien", 0, 8, 30, 20, 55, true, false, false));
            return null;
        }
    }
}
