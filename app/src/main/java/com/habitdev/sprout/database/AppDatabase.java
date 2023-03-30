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

import com.habitdev.sprout.database.assessment.AssessmentDao;
import com.habitdev.sprout.database.assessment.model.Answer;
import com.habitdev.sprout.database.assessment.model.Choices;
import com.habitdev.sprout.database.assessment.model.Frequency;
import com.habitdev.sprout.database.assessment.model.Question;
import com.habitdev.sprout.database.comment.CommentDao;
import com.habitdev.sprout.database.comment.model.Comment;
import com.habitdev.sprout.database.converters.ArrayListConverter;
import com.habitdev.sprout.database.habit.firestore.HabitFireStoreRepository;
import com.habitdev.sprout.database.habit.firestore.SubroutineFireStoreRepository;
import com.habitdev.sprout.database.habit.model.firestore.HabitFireStore;
import com.habitdev.sprout.database.habit.model.firestore.SubroutineFireStore;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.model.room.Subroutines;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesDao;
import com.habitdev.sprout.database.achievement.AchievementDao;
import com.habitdev.sprout.database.achievement.model.Achievement;
import com.habitdev.sprout.database.note.NoteDao;
import com.habitdev.sprout.database.note.model.Note;
import com.habitdev.sprout.database.quotes.QuotesRepository;
import com.habitdev.sprout.database.quotes.model.Quotes;
import com.habitdev.sprout.database.user.UserDao;
import com.habitdev.sprout.database.user.model.User;
import com.habitdev.sprout.enums.AppColor;

import java.util.ArrayList;
import java.util.List;

/**
 * AppDatabase is a singleton class for handling room entities, typeConverter and can be used to pre-populate database in the installation of application
 */
@Database(entities = {User.class, Question.class, Choices.class, Answer.class, Note.class, Habits.class, Subroutines.class, Comment.class, Achievement.class}, version = 1)
@TypeConverters({ArrayListConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static Context AppContext;

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new FetchFirestoreDatabaseAsyncTask(INSTANCE).execute();
            new PopulateAssessmentAsyncTask(INSTANCE).execute();
            new PopulateHabitWithSubroutinesAsyncTask(INSTANCE).execute();
            new PopulateAchievementAsyncTask(INSTANCE).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
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

    public abstract AchievementDao achievementDao();

    private static class FetchFirestoreDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {

        public FetchFirestoreDatabaseAsyncTask(AppDatabase instance) {}

        @Override
        protected Void doInBackground(Void... voids) {
            QuotesRepository quotesRepository = new QuotesRepository();
            HabitFireStoreRepository habitRepository = new HabitFireStoreRepository();
            SubroutineFireStoreRepository subroutineRepository = new SubroutineFireStoreRepository();

            quotesRepository.fetchData(new QuotesRepository.FetchCallback() {
                @Override
                public void onFetchQuoteSuccess(List<Quotes> quotesList) {
                    //success
                    Log.d("tag", "onFetchQuoteSuccess: ");
                }

                @Override
                public void onFetchQuoteFailure(Exception e) {
                    Log.e("tag", e.getMessage());
                    Log.d("tag", "onFetchQuoteFailure: ");
                }
            });

            habitRepository.fetchData(new HabitFireStoreRepository.FetchCallback() {
                @Override
                public void onFetchHabitSuccess(List<HabitFireStore> habitFireStoreList) {
                    //success
                    Log.d("tag", "onFetchHabitSuccess: ");
                }

                @Override
                public void onFetchHabitFailure(Exception e) {
                    Log.e("tag", e.getMessage());
                    Log.d("tag", "onFetchHabitFailure: ");
                }
            });

            subroutineRepository.fetchData(new SubroutineFireStoreRepository.FetchCallback() {
                @Override
                public void onFetchSubroutineSuccess(List<SubroutineFireStore> result) {
                    //success
                    Log.d("tag", "onFetchSubroutineSuccess: ");
                }

                @Override
                public void onFetchSubroutineFailure(Exception e) {
                    Log.e("tag", e.getMessage());
                    Log.d("tag", "onFetchSubroutineFailure: ");
                }
            });
            return null;

        }
    }

    private static class PopulateAssessmentAsyncTask extends AsyncTask<Void, Void, Void> {

        private final AssessmentDao assessmentDao;

        public PopulateAssessmentAsyncTask(AppDatabase instance) {
            assessmentDao = instance.assessmentDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            long uid;
            List<Choices> choices = new ArrayList<>();

            uid = insertQuestion(new Question("How contented are with your current self?"));
            choices.add(new Choices("Not Enough", 4));
            choices.add(new Choices("Contented", 3));
            choices.add(new Choices("A little bit contented", 2));
            choices.add(new Choices("Overly contented", 0));
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //1
            uid = insertQuestion(new Question("How often do you put off tasks or assignments until the last minute?", -1, 1));
            choices.clear();
            choices.add(Frequency.NEVER_.getValue());
            choices.add(Frequency.SELDOM_.getValue());
            choices.add(Frequency.RARELY_.getValue());
            choices.add(Frequency.OCCASIONALLY_.getValue());
            choices.add(Frequency.SOMETIMES_.getValue());
            choices.add(Frequency.OFTEN_.getValue());
            choices.add(Frequency.REGULARLY_.getValue());
            choices.add(Frequency.ALWAYS_.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //1
            uid = insertQuestion(new Question("How often do you feel overwhelmed with the amount of tasks you have to complete?", -1, 1));
            choices.clear();
            choices.add(Frequency.NEVER_.getValue());
            choices.add(Frequency.SELDOM_.getValue());
            choices.add(Frequency.RARELY_.getValue());
            choices.add(Frequency.OCCASIONALLY_.getValue());
            choices.add(Frequency.SOMETIMES_.getValue());
            choices.add(Frequency.USUALLY_.getValue());
            choices.add(Frequency.REGULARLY_.getValue());
            choices.add(Frequency.ALWAYS_.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //1
            uid = insertQuestion(new Question("How often do you find yourself not understanding the material due to procrastination?", -1, 1));
            choices.clear();
            choices.add(Frequency.NEVER_.getValue());
            choices.add(Frequency.SELDOM_.getValue());
            choices.add(Frequency.RARELY_.getValue());
            choices.add(Frequency.SOMETIMES_.getValue());
            choices.add(Frequency.OFTEN_.getValue());
            choices.add(Frequency.USUALLY_.getValue());
            choices.add(Frequency.REGULARLY_.getValue());
            choices.add(Frequency.ALWAYS_.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //2
            uid = insertQuestion(new Question("How often do you stick to a schedule or to-do list for completing tasks?", 1, 2));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //2
            uid = insertQuestion(new Question("How often do you prioritize tasks and focus on the most important or urgent ones first?", 1, 2));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.REGULARLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //2
            uid = insertQuestion(new Question("How often do you use a timer to stay on track during study sessions?", 1, 2));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.REGULARLY.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //2
            uid = insertQuestion(new Question("How often do you eliminate unnecessary activities or distractions from your schedule?", 1, 2));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //2
            uid = insertQuestion(new Question("How often do you make a plan for the next day before going to bed?", 1, 2));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.REGULARLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //3
            uid = insertQuestion(new Question("How often do you develop a consistent study schedule and stick to it?", 1, 3));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.REGULARLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //3
            uid = insertQuestion(new Question("How often do you find a study method that works for you, such as flashcards or summarizing notes?", 1, 3));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.REGULARLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //3
            uid = insertQuestion(new Question("How often do you take frequent breaks during study sessions?", 1, 3));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //3
            uid = insertQuestion(new Question("How often do you use a timer to keep track of your study time?", 1, 3));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //3
            uid = insertQuestion(new Question("How often do you NOT review material regularly and practice with practice tests or quizzes?", -1, 3));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM_.getValue());
            choices.add(Frequency.RARELY_.getValue());
            choices.add(Frequency.OFTEN_.getValue());
            choices.add(Frequency.USUALLY_.getValue());
            choices.add(Frequency.REGULARLY_.getValue());
            choices.add(Frequency.ALWAYS_.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //4
            uid = insertQuestion(new Question("How often do you find yourself constantly checking social media, phone, or other electronic devices while studying?", -1, 4));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM_.getValue());
            choices.add(Frequency.SOMETIMES_.getValue());
            choices.add(Frequency.OFTEN_.getValue());
            choices.add(Frequency.USUALLY_.getValue());
            choices.add(Frequency.REGULARLY_.getValue());
            choices.add(Frequency.ALWAYS_.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //4
            uid = insertQuestion(new Question("How often do you eliminate electronic distractions, such as social media or phone notifications while studying?", 1, 4));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.REGULARLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //4
            uid = insertQuestion(new Question("How often do you create a designated study space free from distractions?", 1, 4));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //4
            uid = insertQuestion(new Question("How often do you use apps or website blockers during study time?", 1, 4));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.REGULARLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //4
            uid = insertQuestion(new Question("How often do you put your phone on silent or in another room while studying?", 1, 4));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.REGULARLY.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //4
            uid = insertQuestion(new Question("How often do you use noise-canceling headphones to block out background noise while studying?", 1, 4));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //5
            uid = insertQuestion(new Question("How often do you establish a consistent sleep schedule and aim for 7-8 hours of sleep per night?", 1, 5));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.REGULARLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //5
            uid = insertQuestion(new Question("How often do you avoid screens and stimulating activities before bedtime?", 1, 5));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.REGULARLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //5
            uid = insertQuestion(new Question("How often do you create a bedtime environment to relax and wind down?", 1, 5));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //5
            uid = insertQuestion(new Question("How often do you keep your bedroom cool, dark, and quiet for a good night's sleep?", 1, 5));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.REGULARLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //6
            uid = insertQuestion(new Question("How often do you use positive affirmations to stay motivated?", 1, 6));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.REGULARLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //6
            uid = insertQuestion(new Question("How often do you take a short break to do something you enjoy after completing a task?", 1, 6));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.REGULARLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //6
            uid = insertQuestion(new Question("How often do you practice stress-relieving activities, such as meditation or yoga?", 1, 6));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //6
            uid = insertQuestion(new Question("How often do you reflect on your own learning process and identify strengths and achievements?", 1, 6));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.SELDOM.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.OCCASIONALLY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.USUALLY.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //6
            uid = insertQuestion(new Question("How often do you feel unmotivated and disengaged in coursework?", -1, 6));
            choices.clear();
            choices.add(Frequency.NEVER_.getValue());
            choices.add(Frequency.SELDOM_.getValue());
            choices.add(Frequency.RARELY_.getValue());
            choices.add(Frequency.OCCASIONALLY_.getValue());
            choices.add(Frequency.SOMETIMES_.getValue());
            choices.add(Frequency.OFTEN_.getValue());
            choices.add(Frequency.USUALLY_.getValue());
            choices.add(Frequency.ALWAYS_.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //6
            uid = insertQuestion(new Question("How often do you have a negative attitude and mindset towards your studies?", -1, 6));
            choices.clear();
            choices.add(Frequency.NEVER_.getValue());
            choices.add(Frequency.SELDOM_.getValue());
            choices.add(Frequency.RARELY_.getValue());
            choices.add(Frequency.OFTEN_.getValue());
            choices.add(Frequency.USUALLY_.getValue());
            choices.add(Frequency.REGULARLY_.getValue());
            choices.add(Frequency.ALWAYS_.getValue());
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

    private static class PopulateHabitWithSubroutinesAsyncTask extends AsyncTask<Void, Void, Void> {

        private final HabitWithSubroutinesDao habitWithSubroutinesDao;

        public PopulateHabitWithSubroutinesAsyncTask(AppDatabase instance) {
            habitWithSubroutinesDao = instance.habitsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HabitFireStoreRepository habitRepository = new HabitFireStoreRepository();
            SubroutineFireStoreRepository subroutineRepository = new SubroutineFireStoreRepository();

            final List<SubroutineFireStore>[] subroutinesList = new List[]{new ArrayList<>()};

            //Fetch subroutines first
            subroutineRepository.fetchData(new SubroutineFireStoreRepository.FetchCallback() {
                @Override
                public void onFetchSubroutineSuccess(List<SubroutineFireStore> result) {
                    if (!result.isEmpty()) {
                        subroutinesList[0] = result;
                        for (SubroutineFireStore subroutine : result) {
                            subroutinesList[0] = result;
                        }
                    }
                    Log.d("tag", "onFetchSubroutineSuccess: appdb");
                }

                @Override
                public void onFetchSubroutineFailure(Exception e) {
                    Log.e("tag", e.getMessage());
                }
            });

            habitRepository.fetchData(new HabitFireStoreRepository.FetchCallback() {
                @Override
                public void onFetchHabitSuccess(List<HabitFireStore> result) {
                    if (!result.isEmpty()) { // check for empty result no data fetch from firebase, otherwise populate offline
                        if (!subroutinesList[0].isEmpty()) {  // check for subroutine fetched from firebase, otherwise populate offline
                            for (HabitFireStore habit : result) {
                                Habits habits = new Habits(habit.getTitle(), habit.getDescription(), habit.getColor(), false, false);
                                Log.d("tag", "onFetchHabitSuccess: " + habits.toString());
                                List<Subroutines> list = getSubroutine_by_fk_uid(habit.getPk_uid(), subroutinesList[0]);
                                habits.setTotal_subroutine(list.size());
                                long id = habitWithSubroutinesDao.insertHabit(habits);
                                habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(list, id));
                            }
                        } else {
                            populateOffline();
                        }
                    } else {
                        populateOffline();
                    }
                }

                @Override
                public void onFetchHabitFailure(Exception e) {
                    Log.d("tag", "onFetchHabitFailure: ");
                }
            });
            return null;
        }

        private void populateOffline() {
            //Procrastination
            Habits habit = new Habits(
                    "Procrastination",
                    "Putting off tasks or assignments until the last minute can lead to increased stress and a lack of understanding of the material.",
                    AppColor.CLOUDS.getColor(),
                    false,
                    false
            );

            List<Subroutines> subroutinesList = new ArrayList<>();

            subroutinesList.add(
                    new Subroutines(
                            "Time management",
                            "Set specific deadlines for yourself and break down larger tasks into smaller, manageable chunks.",
                            AppColor.CLOUDS.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Focus enhancement",
                            "Eliminate distractions and create a designated study space.",
                            AppColor.ALZARIN.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Time-boxing",
                            "Use a timer to focus on one task for a specific amount of time.",
                            AppColor.AMETHYST.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Prioritization",
                            "Prioritize your to-do list and tackle the most important task first.",
                            AppColor.BRIGHT_SKY_BLUE.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Motivation boost",
                            "Use a reward system for completing tasks on time.",
                            AppColor.NEPHRITIS.getColor(),
                            false
                    )
            );

            habit.setTotal_subroutine(subroutinesList.size());
            long uid = habitWithSubroutinesDao.insertHabit(habit);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(subroutinesList, uid));

            //Lack of time management
            habit = new Habits(
                    "Lack of time management",
                    "Not effectively managing one's time can lead to feeling overwhelmed and difficulty completing assignments on time.",
                    AppColor.ALZARIN.getColor(),
                    false,
                    false
            );

            subroutinesList.clear();

            subroutinesList.add(
                    new Subroutines(
                            "Task planning",
                            "Create a schedule or to-do list of tasks and stick to it.",
                            AppColor.SUNFLOWER.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Priority setting",
                            "Prioritize tasks and focus on the most important or urgent ones first.",
                            AppColor.CLOUDS.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Time tracking",
                            "Use a timer to stay on track during study sessions.",
                            AppColor.ALZARIN.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Distraction elimination",
                            "Eliminate unnecessary activities or distractions from your schedule.",
                            AppColor.AMETHYST.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Future planning",
                            "Make a plan for the next day before going to bed.",
                            AppColor.BRIGHT_SKY_BLUE.getColor(),
                            false
                    )
            );

            habit.setTotal_subroutine(subroutinesList.size());
            uid = habitWithSubroutinesDao.insertHabit(habit);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(subroutinesList, uid));

            //Poor study practices
            habit = new Habits(
                    "Poor study practices",
                    "Not studying in a focused and consistent manner can lead to poor performance on exams and assignments.",
                    AppColor.AMETHYST.getColor(),
                    false,
                    false
            );

            subroutinesList.clear();

            subroutinesList.add(
                    new Subroutines(
                            "Schedule consistency",
                            "Develop a consistent study schedule and stick to it.",
                            AppColor.NEPHRITIS.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Break intervals",
                            "Take frequent breaks during study sessions.",
                            AppColor.CLOUDS.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Study method",
                            "Find a study method that works for you, such as flashcards or summarizing notes.",
                            AppColor.SUNFLOWER.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Time management",
                            "Use a timer to keep track of your study time.",
                            AppColor.ALZARIN.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Regular review",
                            "Review material regularly and practice with practice tests or quizzes.",
                            AppColor.AMETHYST.getColor(),
                            false
                    )
            );

            habit.setTotal_subroutine(subroutinesList.size());
            uid = habitWithSubroutinesDao.insertHabit(habit);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(subroutinesList, uid));

            //Easily Distracted
            habit = new Habits(
                    "Easily Distracted",
                    "Constantly checking social media, phone, or other electronic devices can distract from studying and lead to lower productivity.",
                    AppColor.BRIGHT_SKY_BLUE.getColor(),
                    false,
                    false
            );

            subroutinesList.clear();

            subroutinesList.add(
                    new Subroutines(
                            "Distraction elimination",
                            "Eliminate electronic distractions, such as social media or phone notifications.",
                            AppColor.BRIGHT_SKY_BLUE.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Study environment",
                            "Create a designated study space free from distractions.",
                            AppColor.NEPHRITIS.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Digital blocking",
                            "Use apps or website blockers during study time.",
                            AppColor.SUNFLOWER.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Phone management",
                            "Put your phone on silent or in another room while studying.",
                            AppColor.CLOUDS.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Noise reduction",
                            "Use noise-canceling headphones to block out background noise.",
                            AppColor.ALZARIN.getColor(),
                            false
                    )
            );

            habit.setTotal_subroutine(subroutinesList.size());
            uid = habitWithSubroutinesDao.insertHabit(habit);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(subroutinesList, uid));

            //Poor sleep management
            habit = new Habits(
                    "Poor sleep management",
                    "Not getting enough sleep can negatively impact memory, concentration, and overall well-being, making it harder to study effectively.",
                    AppColor.BRIGHT_SKY_BLUE.getColor(),
                    false,
                    false
            );

            subroutinesList.clear();

            subroutinesList.add(
                    new Subroutines(
                            "Sleep schedule",
                            "Establish a consistent sleep schedule and aim for 7-8 hours of sleep per night.",
                            AppColor.BRIGHT_SKY_BLUE.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Pre-sleep preparation",
                            "Avoid screens and stimulating activities before bedtime.",
                            AppColor.NEPHRITIS.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Bedtime wind down",
                            "Create a bedtime environment to relax and wind down.",
                            AppColor.SUNFLOWER.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Sleep environment",
                            "Keep your bedroom cool, dark, and quiet.",
                            AppColor.CLOUDS.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Noise mitigation",
                            "Use a white noise machine to block out any background noise.",
                            AppColor.ALZARIN.getColor(),
                            false
                    )
            );

            habit.setTotal_subroutine(subroutinesList.size());
            uid = habitWithSubroutinesDao.insertHabit(habit);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(subroutinesList, uid));

            //Negative outlook
            habit = new Habits(
                    "Negative Outlook",
                    "Having a negative attitude and mindset can make it harder to stay motivated and engaged in coursework.",
                    AppColor.NEPHRITIS.getColor(),
                    false,
                    false
            );

            subroutinesList.clear();

            subroutinesList.add(
                    new Subroutines(
                            "Positive reinforcement",
                            "Use positive affirmations to stay motivated.",
                            AppColor.AMETHYST.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Break activities",
                            "Take a short break to do something you enjoy after completing a task.",
                            AppColor.BRIGHT_SKY_BLUE.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Stress management",
                            "Practice stress-relieving activities, such as meditation or yoga.",
                            AppColor.NEPHRITIS.getColor(),
                            false
                    )
            );

            subroutinesList.add(
                    new Subroutines(
                            "Self-reflection",
                            "Reflect on your own learning process and identify strengths and achievements.",
                            AppColor.SUNFLOWER.getColor(),
                            false
                    )
            );

            habit.setTotal_subroutine(subroutinesList.size());
            uid = habitWithSubroutinesDao.insertHabit(habit);
            habitWithSubroutinesDao.insertSubroutines(setFk_habit_uid(subroutinesList, uid));
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

        private List<Subroutines> getSubroutine_by_fk_uid(long uid, List<SubroutineFireStore> subroutineList) {
            List<Subroutines> list = new ArrayList<>();
            for (SubroutineFireStore subroutine : subroutineList) {
                if (uid == subroutine.getFk_habit_uid()) {
                    Subroutines item = new Subroutines(subroutine.getTitle(), subroutine.getDescription(), subroutine.getColor(), false);
                    list.add(item);
                }
            }
            return list;
        }
    }

    private static class PopulateSampleNote extends AsyncTask<Void, Void, Void> {

        private final NoteDao noteDao;

        public PopulateSampleNote(AppDatabase instance) {
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

    private static class PopulateAchievementAsyncTask extends AsyncTask<Void, Void, Void> {

        private final AchievementDao achievementDao;

        public PopulateAchievementAsyncTask(AppDatabase instance) {
            achievementDao = instance.achievementDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Triggered when first time adding new habit on reform. This is triggered in analysis fragment when clicking and cofirming the habit to be added.
            Achievement FirstStep = new Achievement("First Step", "Take a leap in reforming a bad habit", 0, 1);
            achievementDao.insertAchievement(FirstStep);

            //Triggered in adding notes
            Achievement NoteI = new Achievement("First Note", "Write down your thoughts", 0, 1);
            achievementDao.insertAchievement(NoteI);
            Achievement NoteII = new Achievement("Piece by Piece", "Journal your progress", 1, NoteI.getTitle(), achievementDao.getAchievementByTitle(NoteI.getTitle()), 10);
            achievementDao.insertAchievement(NoteII);
            Achievement NoteIII = new Achievement("Better Understanding", "Lots of notes written", 1, NoteII.getTitle(), achievementDao.getAchievementByTitle(NoteII.getTitle()), 30);
            achievementDao.insertAchievement(NoteIII);
            Achievement NoteIV = new Achievement("Chapter", "Keep writing you thoughts", 1, NoteIII.getTitle(), achievementDao.getAchievementByTitle(NoteIII.getTitle()), 60);
            achievementDao.insertAchievement(NoteIV);
            Achievement NoteV = new Achievement("Journal", "You have written your thoughts", 1, NoteIV.getTitle(),achievementDao.getAchievementByTitle(NoteIV.getTitle()), 100);
            achievementDao.insertAchievement(NoteV);

            //Triggerred by duration of appllication since installed
            Achievement WEEK = new Achievement("Been a week", "The application was installed for 7 days", 0, 1);
            achievementDao.insertAchievement(WEEK);
            Achievement MONTH = new Achievement("You have stayed", "The application was installed for 30 days", 0, 1);
            achievementDao.insertAchievement(MONTH);
            Achievement THREE_MONTH = new Achievement("Keep growing", "The application was installed for 90 days", 0, 1);
            achievementDao.insertAchievement(THREE_MONTH);
            Achievement AYEAR = new Achievement("Great Dedication", "The application was installed for 365 days", 0, 1);
            achievementDao.insertAchievement(AYEAR);

            //Triggered by swiping in left and right direction in top bar to navigate between menus
            Achievement LEFTSWIPE = new Achievement("Secret", "Hidden", 0, 1);
            achievementDao.insertAchievement(LEFTSWIPE);
            Achievement RIGHTSWIPE = new Achievement("Secret", "Hidden", 0, 1);
            achievementDao.insertAchievement(RIGHTSWIPE);

            //Triggered when first time adding a note in journal
//            achievementDao.insertAchievement(new Achievement("Making progress", "More notes, better self understanding", 1, 10));
//
//            achievementDao.insertAchievement(new Achievement("sample", "description", 1, 5, 15, null, false));
//
//            achievementDao.insertAchievement(new Achievement("sample2", "description2", 1, 30, 75, null, false));
//
//            achievementDao.insertAchievement(new Achievement("sample3", "description", 1, 10, 10, "April 29, 2000", true));
//
//            achievementDao.insertAchievement(new Achievement("sample4", "description", 0, 23, 23, "January 12, 2000", true));
//
//            achievementDao.insertAchievement(new Achievement("sample", "description", 0, 1, 1, null, false));
            return null;
        }
    }
}
