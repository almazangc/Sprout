package com.habitdev.sprout.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.assessment.model.Answer;
import com.habitdev.sprout.database.assessment.AssessmentDao;
import com.habitdev.sprout.database.assessment.model.Choices;
import com.habitdev.sprout.database.assessment.model.Question;
import com.habitdev.sprout.database.comment.CommentDao;
import com.habitdev.sprout.database.comment.model.Comment;
import com.habitdev.sprout.database.converters.ArrayListConverter;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesDao;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.database.note.model.Note;
import com.habitdev.sprout.database.note.NoteDao;
import com.habitdev.sprout.database.user.model.User;
import com.habitdev.sprout.database.user.UserDao;
import com.habitdev.sprout.enums.AppColor;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {User.class, Question.class, Choices.class, Answer.class, Note.class, Habits.class, Subroutines.class, Comment.class}, version = 1)
@TypeConverters({ArrayListConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static Context AppContext;

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
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

    public abstract CommentDao commentDao();

    private static class PopulateAssessmentAsyncTask extends AsyncTask<Void, Void, Void> {

        private final AssessmentDao assessmentDao;

        public PopulateAssessmentAsyncTask(AppDatabase instance) {
            assessmentDao = instance.assessmentDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            long uid;
            uid = insertQuestion(new Question("1: How long does it take to complete the application?"));
            List<Choices> choices = new ArrayList<>();
            choices.add(new Choices("A Month"));
            choices.add(new Choices("2 Years"));
            choices.add(new Choices("Never"));
            choices.add(new Choices("Long Time"));
            choices.add(new Choices("Until then"));
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            uid = insertQuestion(new Question("2:How contented are with your current self?"));
            choices.clear();
            choices.add(new Choices("Not Enough"));
            choices.add(new Choices("Contented"));
            choices.add(new Choices("A little bit contented"));
            choices.add(new Choices("Overly contented"));
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            uid = insertQuestion(new Question("3: What is the application name?"));
            choices.clear();
            choices.add(new Choices("Habit"));
            choices.add(new Choices("Sprout"));
            choices.add(new Choices("Peanut"));
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            uid = insertQuestion(new Question("4: Question 4?"));
            choices.clear();
            choices.add(new Choices("Select A"));
            choices.add(new Choices("Select B"));
            choices.add(new Choices("Select C"));
            choices.add(new Choices("Select D"));
            choices.add(new Choices("Select E"));
            choices.add(new Choices("Select F"));
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

//            assessmentsDao.insert(new Assessments(getString(R.string.Q0), getString(R.string.Q0_SEL1), getString(R.string.Q0_SEL2), getString(R.string.Q0_SEL3), getString(R.string.Q0_SEL4), DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("1: Question", "A", "B", "C", "D", DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("2: Which habit troubles you, This is a very long questions as an input to the app?", "Wasting Time", "Smoking and Drinking", "No Motivation to work", "Over Spending", DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("3: Which habit troubles you?", "Wasting Time", "Smoking and Drinking", "No Motivation to work", "Over Spending", DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("4: How often do you feel happy?", "Always", "Usually", "Seldom", "Never", DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("5: Do you like banana?", "Yes", "No", "I don't know", "I don't care", DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("6: Do you fear life?", "Yes not", "No Yes", "I don't know", "I don't care", DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("7: If 1 + 1 - 2, What is the circumference of the sun?", "Yes?", "WTH", "I don't know", "Life ain't fun", DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("8: Do you see you future?", "Who care about that", "A bit Foggy", "I don't know anymore", "I don't care", DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("9: Is this a generic question?", "Yes", "No", "I don't know", "I don't care", DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("10: What is the name of app?", "Sprout", "Don't Know", "Undecided", "I wanna change, it sucks", DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("11: Do you always owo?", "Yes", "Absolutely Not", "I don't know", "I don't care", DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("12: Is this question number 10?", "Yes", "No", "I don't know", "I don't care", DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("13: If you answered yes then your wrong, this is question number 10?", "OK", "Meh", "LOL", "I don't care", DEFAULT_SELECTED));
//            assessmentsDao.insert(new Assessments("14: Last Question?", "Yes", "No", "I don't know", "Why should I care?", DEFAULT_SELECTED));
            return null;
        }

        private String getString(int id) {
            return AppContext.getApplicationContext().getResources().getString(id);
        }

        private List<Choices> setFk_Question_uid(List<Choices> list, long id) {
            for (Choices choices : list) {
                choices.setFk_question_uid(id);
            }
            return list;
        }

        private long insertQuestion(Question question) {
            return assessmentDao.insertQuestion(question);
        }
    }

    private static class PopulateNoteAsyncTask extends AsyncTask<Void, Void, Void> {

        private final NoteDao noteDao;

        public PopulateNoteAsyncTask(AppDatabase instance) {
            noteDao = instance.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Note 1", "", "Subtitle in a nutshell", AppColor.CLOUDS.getColor()));
            noteDao.insert(new Note("Test", "", "A very short sentence. Second part of the sentence", AppColor.SUNFLOWER.getColor()));
            noteDao.insert(new Note("What?", "", "SampleSubTitle, but no thought. You are baka!!!!!", AppColor.BRIGHT_SKY_BLUE.getColor()));
            noteDao.insert(new Note("Note 4", "", "SampleSubTitle", AppColor.NEPHRITIS.getColor()));
            noteDao.insert(new Note("Note 4", "", "SampleSubTitle", AppColor.AMETHYST.getColor()));
            noteDao.insert(new Note("No time to spend", "", "SampleSubTitle, you need to learn how to integrate the application", AppColor.ALZARIN.getColor()));
            noteDao.insert(new Note("Note Sample 6 Title", "date?", "SampleSubTitle", AppColor.SUNFLOWER.getColor()));
            return null;
        }
    }

    private static class PopulateHabitWithSubroutinesAsyncTask extends AsyncTask<Void, Void, Void> {

        private final HabitWithSubroutinesDao habitWithSubroutinesDao;

        public PopulateHabitWithSubroutinesAsyncTask(AppDatabase instance) {
            habitWithSubroutinesDao = instance.habitsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Subroutines sample = new Subroutines(getString(R.string.sample_subroutine_title), getString(R.string.sample_subroutine_description), AppColor.BRIGHT_SKY_BLUE.getColor(), false);
            Habits habits = new Habits(getString(R.string.sample_habit_title), getString(R.string.sample_habit_description), AppColor.CLOUDS.getColor(),false, false);
            habits.setTotal_subroutine(5);
            long id = habitWithSubroutinesDao.insertHabit(habits);
            List<Subroutines> list = new ArrayList<>();
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));

            list.clear();
            habits.setTotal_subroutine(3);
            id = habitWithSubroutinesDao.insertHabit(habits);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));

            list.clear();
            habits.setTotal_subroutine(2);
            id = habitWithSubroutinesDao.insertHabit(habits);
            list.add(sample);
            list.add(sample);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));

            list.clear();
            habits.setTotal_subroutine(8);
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

            list.clear();
            habits.setTotal_subroutine(10);
            id = habitWithSubroutinesDao.insertHabit(habits);
            list.add(sample);
            list.add(sample);
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
            habits.setTotal_subroutine(4);
            id = habitWithSubroutinesDao.insertHabit(habits);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            list.add(sample);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));

            list.clear();
            habits.setTotal_subroutine(8);
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

            list.clear();
            habits.setTotal_subroutine(8);
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

            list.clear();
            habits = new Habits("Poor Sleep Management", "Not Sleeping on time daily", AppColor.SUNFLOWER.getColor(), false, false);
            habits.setTotal_subroutine(3);
            id = habitWithSubroutinesDao.insertHabit(habits);
            list.add(new Subroutines("Relax.", "Find calming, relaxing activities to do before bedtime.", AppColor.SUNFLOWER.getColor(), false));
            list.add(new Subroutines("Adjust your bedtime, but be patient", "If you’re aiming to go to sleep earlier, try slowly scaling back your bedtime until you are at the desired hour.", AppColor.NEPHRITIS.getColor(),false));
            list.add(new Subroutines("Do not nap, even if you feel tired", "Napping can interfere with going to sleep at night. Pelayo recommends scheduling exercise when you feel like napping. “The exercise will chase away the sleepiness. Then you can save up that drive to sleep for later,” he says.", AppColor.BRIGHT_SKY_BLUE.getColor(), false));
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
