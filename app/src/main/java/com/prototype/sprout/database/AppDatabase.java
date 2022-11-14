package com.prototype.sprout.database;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.prototype.sprout.R;
import com.prototype.sprout.database.assessment.Assessment;
import com.prototype.sprout.database.assessment.AssessmentDao;
import com.prototype.sprout.database.converters.ArrayListConverter;
import com.prototype.sprout.database.habits_with_subroutines.HabitWithSubroutinesDao;
import com.prototype.sprout.database.habits_with_subroutines.Habits;
import com.prototype.sprout.database.habits_with_subroutines.Subroutines;
import com.prototype.sprout.database.note.Note;
import com.prototype.sprout.database.note.NoteDao;
import com.prototype.sprout.database.user.User;
import com.prototype.sprout.database.user.UserDao;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {User.class, Assessment.class, Note.class, Habits.class, Subroutines.class}, version = 1)
@TypeConverters({ArrayListConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static Context AppContext;

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAssessmentAsyncTask(INSTANCE).execute();
            new PopulateNoteAsyncTask(INSTANCE).execute();
            new PopulateHabitWithSubroutinesAsyncTask(INSTANCE).execute();
        }
    };

    //Added synchronized keyword
    public static synchronized AppDatabase getDbInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .build();
        }
        AppContext = context;
        return INSTANCE;
    }

    public abstract UserDao userDao();

    public abstract AssessmentDao assessmentDao();

    public abstract NoteDao noteDao();

    public abstract HabitWithSubroutinesDao habitsDao();

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
            assessmentDao.insert(new Assessment("5: Do you like banana?", "Yes", "No", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("6: Do you fear life?", "Yes not", "No Yes", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("7: If 1 + 1 - 2, What is the circumference of the sun?", "Yes?", "WTH", "I don't know", "Life ain't fun", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("8: Do you see you future?", "Who care about that", "A bit Foggy", "I don't know anymore", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("9: Is this a generic question?", "Yes", "No", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("10: What is the name of app?", "Sprout", "Don't Know", "Undecided", "I wanna change, it sucks", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("11: Do you always owo?", "Yes", "Absolutely Not", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("12: Is this question number 10?", "Yes", "No", "I don't know", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("13: If you answered yes then your wrong, this is question number 10?", "OK", "Meh", "LOL", "I don't care", DEFAULT_SELECTED));
            assessmentDao.insert(new Assessment("14: Last Question?", "Yes", "No", "I don't know", "Why should I care?", DEFAULT_SELECTED));

            return null;
        }
    }

    ;

    private static class PopulateNoteAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDao noteDao;

        public PopulateNoteAsyncTask(AppDatabase instance) {
            noteDao = instance.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Note 1", "April 28, 2000", "Subtitle in a nutshell", "cloud"));
            noteDao.insert(new Note("Test", "June 1, 1999", "A very short sentence. Second part of the sentence", "alzarin"));
            noteDao.insert(new Note("What?", "No date", "SampleSubTitle, but no thought. You are baka!!!!!", "sunflower"));
            noteDao.insert(new Note("Note 4", "Sample Date", "SampleSubTitle", "nephritis"));
            noteDao.insert(new Note("Note 4", "Sample Date", "SampleSubTitle", "sunflower"));
            noteDao.insert(new Note("No time to spend", "Clean Date", "SampleSubTitle, you need to learn how to integrate the application", "amethyst"));
            noteDao.insert(new Note("Note Sample 6 Title", "10.34.432/423423", "SampleSubTitle", "brightsky_blue"));
            return null;
        }
    }

    private static class PopulateHabitWithSubroutinesAsyncTask extends AsyncTask<Void, Void, Void> {

        private HabitWithSubroutinesDao habitWithSubroutinesDao;

        public PopulateHabitWithSubroutinesAsyncTask(AppDatabase instance) {
            habitWithSubroutinesDao = instance.habitsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Resources res = AppContext.getApplicationContext().getResources();
            Subroutines sample = new Subroutines(getString(R.string.sample_subroutine_title), getString(R.string.sample_subroutine_description));

            long id = habitWithSubroutinesDao.insertHabit(new Habits(res.getString(R.string.sample_habit_title), res.getString(R.string.sample_habit_description)));
            List<Subroutines> list = new ArrayList<>();
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));

            list.clear();
            id = habitWithSubroutinesDao.insertHabit(new Habits(res.getString(R.string.sample_habit_title), res.getString(R.string.sample_habit_description)));
            list.add(sample);
            list.add(sample);
            list.add(sample);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));

            list.clear();
            id = habitWithSubroutinesDao.insertHabit(new Habits(res.getString(R.string.sample_habit_title), res.getString(R.string.sample_habit_description)));
            list.add(sample);
            list.add(sample);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));

            list.clear();
            id = habitWithSubroutinesDao.insertHabit(new Habits(res.getString(R.string.sample_habit_title), res.getString(R.string.sample_habit_description)));
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));

            list.clear();
            id = habitWithSubroutinesDao.insertHabit(new Habits(res.getString(R.string.sample_habit_title), res.getString(R.string.sample_habit_description)));
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));

            list.clear();
            Habits habits = new Habits(res.getString(R.string.sample_habit_title), res.getString(R.string.sample_habit_description));
            habits.setOnReform(true);
            id = habitWithSubroutinesDao.insertHabit(habits);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));

            list.clear();
            habits = new Habits(res.getString(R.string.sample_habit_title), res.getString(R.string.sample_habit_description));
            habits.setOnReform(true);
            id = habitWithSubroutinesDao.insertHabit(habits);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));

//            habitDao.insert(new Habit(false, "Stress Eating", "Eating a lot due to stress", ArrayListConverter.fromString("[3,5,8]")));
//            habitDao.insert(new Habit(true, "Poor Sleep Management", "Not Sleeping on time daily", ArrayListConverter.fromString("[1, 2, 3, 4, 5]")));
//            habitDao.insert(new Habit(false, "Drinking", "Alcohol, Beverage, uncontrolled drinking", ArrayListConverter.fromString("[2,5,8,9,10]")));
//            habitDao.insert(new Habit(false, "Gambling", "Uncontrolled betting of cash", ArrayListConverter.fromString("[1,4,7,8,9]")));
//            habitDao.insert(new Habit(false, "Poor spending habits", "Unable to resist from spending", ArrayListConverter.fromString("[1,3,6,7,8]")));
//            habitDao.insert(new Habit(false, "Excessive profanity", "Uncontrolled toxicity", ArrayListConverter.fromString("[1,4,5,6,7]")));
//            habitDao.insert(new Habit(false, "Multi-tasking", "Jumping from one task to another not lack of focus", ArrayListConverter.fromString("[1,3,6,8,9]")));
//            habitDao.insert(new Habit(false, "Smoking", "Addicted to smoking, eg., vape", ArrayListConverter.fromString("[1,3,7,9]")));
//            habitDao.insert(new Habit(true, "Procrastination", "The action of delaying something", ArrayListConverter.fromString("[6,7,8,9,10]")));
//            habitDao.insert(new Habit(false, "Overthinking and worrying", "Thinking too much about something", ArrayListConverter.fromString("[1,2,4,5]")));
//            habitDao.insert(new Habit(false, "Cluttering your living/workspace", "Pileup, Messy or Unmanaged Living Room or Workspace", ArrayListConverter.fromString("[1,4,7,9]")));
//            habitDao.insert(new Habit(false, "Leaving the toilet seat up", "Some desc", ArrayListConverter.fromString("[1,3,6,8]")));
//            habitDao.insert(new Habit(false, "Leaving clothes on the floor", "Throwing used clothes anyone in the house", ArrayListConverter.fromString("[0,3,5,6]")));
//            habitDao.insert(new Habit(false, "Taking things personally", "arsagid jkjk", ArrayListConverter.fromString("[1,4,5,6]")));
//            habitDao.insert(new Habit(false, "Overusing slang", "Feeling slang", ArrayListConverter.fromString("[0,3,5,6]")));
//            habitDao.insert(new Habit(false, "A lot", "and more....", ArrayListConverter.fromString("[1,4,6,7]")));
//

            return null;
        }

        private String getString(int id) {
            return AppContext.getApplicationContext().getResources().getString(id);
        }

        private List<Subroutines> setFk_habit_uid(List<Subroutines> list, long id) {
            for (Subroutines subroutine : list) {
                subroutine.setFk_habit_uid(id);
            }
            return list;
        }
    }
}
