package com.habitdev.sprout.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.assessment.AssessmentDao;
import com.habitdev.sprout.database.assessment.model.Answer;
import com.habitdev.sprout.database.assessment.model.Choices;
import com.habitdev.sprout.database.assessment.model.Question;
import com.habitdev.sprout.database.comment.CommentDao;
import com.habitdev.sprout.database.comment.model.Comment;
import com.habitdev.sprout.database.converters.ArrayListConverter;
import com.habitdev.sprout.database.habit.HabitFireStoreRepository;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesDao;
import com.habitdev.sprout.database.habit.model.HabitFireStore;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.database.note.NoteDao;
import com.habitdev.sprout.database.note.model.Note;
import com.habitdev.sprout.database.user.UserDao;
import com.habitdev.sprout.database.user.model.User;
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
//            new PopulateNoteAsyncTask(INSTANCE).execute();
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

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            HabitFireStoreRepository repository = new HabitFireStoreRepository();

            repository.fetchData(new HabitFireStoreRepository.FetchCallback() {
                @Override
                public void onFetchHabitSuccess(List<HabitFireStore> result) {
                    for (HabitFireStore habitFireStore : result) {
                        Habits habits = new Habits(habitFireStore.getTitle(), habitFireStore.getDescription(), habitFireStore.getColor(), false, false);
                        Log.d("tag", "doInBackground: adding with firestore" + habits.toString());
                        Subroutines sample = new Subroutines(getString(R.string.sample_subroutine_title), getString(R.string.sample_subroutine_description), AppColor.BRIGHT_SKY_BLUE.getColor(), false);
                        habits.setTotal_subroutine(3);
                        long id = habitWithSubroutinesDao.insertHabit(habits);
                        List<Subroutines> list = new ArrayList<>();
                        list.add(sample);
                        list.add(sample);
                        list.add(sample);
                        habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));
                    }
                }

                @Override
                public void onFetchHabitFailure(Exception e) {
                    Log.e("tag", "onFetchHabitFailure: ");
                    // add sample for now
                    Log.d("tag", "doInBackground: adding normnally");
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
                    habits = new Habits("Poor Sleep Management", "Not Sleeping on time daily", AppColor.SUNFLOWER.getColor(), false, false);
                    habits.setTotal_subroutine(3);
                    id = habitWithSubroutinesDao.insertHabit(habits);
                    list.add(new Subroutines("Relax.", "Find calming, relaxing activities to do before bedtime.", AppColor.SUNFLOWER.getColor(), false));
                    list.add(new Subroutines("Adjust your bedtime, but be patient", "If you’re aiming to go to sleep earlier, try slowly scaling back your bedtime until you are at the desired hour.", AppColor.NEPHRITIS.getColor(),false));
                    list.add(new Subroutines("Do not nap, even if you feel tired", "Napping can interfere with going to sleep at night. Pelayo recommends scheduling exercise when you feel like napping. “The exercise will chase away the sleepiness. Then you can save up that drive to sleep for later,” he says.", AppColor.BRIGHT_SKY_BLUE.getColor(), false));
                    habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));
                }
            });


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
