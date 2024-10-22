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

import com.habitdev.sprout.database.achievement.AchievementDao;
import com.habitdev.sprout.database.achievement.model.Achievement;
import com.habitdev.sprout.database.assessment.AssessmentDao;
import com.habitdev.sprout.database.assessment.model.Answer;
import com.habitdev.sprout.database.assessment.model.AssessmentRecord;
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
@Database(entities = {User.class, Question.class, Choices.class, Answer.class, AssessmentRecord.class, Note.class, Habits.class, Subroutines.class, Comment.class, Achievement.class}, version = 1)
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
            choices.add(new Choices("Not Enough", .8));
            choices.add(new Choices("Contented", .5));
            choices.add(new Choices("A little bit contented", .3));
            choices.add(new Choices("Overly contented", 0));
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //1
            uid = insertQuestion(new Question("How often do you put off tasks or assignments until the last minute?", -1, 1));
            choices.clear();
            choices.add(Frequency.NEVER_.getValue());
            choices.add(Frequency.RARELY_.getValue());
            choices.add(Frequency.SOMETIMES_.getValue());
            choices.add(Frequency.OFTEN_.getValue());
            choices.add(Frequency.ALWAYS_.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //1
            uid = insertQuestion(new Question("How often do you feel overwhelmed with the amount of tasks you have to complete?", -1, 1));
            choices.clear();
            choices.add(Frequency.NEVER_.getValue());
            choices.add(Frequency.RARELY_.getValue());
            choices.add(Frequency.SOMETIMES_.getValue());
            choices.add(Frequency.OFTEN_.getValue());
            choices.add(Frequency.ALWAYS_.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //1
            uid = insertQuestion(new Question("How often do you find yourself not understanding the material due to procrastination?", -1, 1));
            choices.clear();
            choices.add(Frequency.NEVER_.getValue());
            choices.add(Frequency.RARELY_.getValue());
            choices.add(Frequency.SOMETIMES_.getValue());
            choices.add(Frequency.OFTEN_.getValue());
            choices.add(Frequency.ALWAYS_.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //2
            uid = insertQuestion(new Question("How often do you stick to a schedule or to-do list for completing tasks?", 1, 2));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //2
            uid = insertQuestion(new Question("How often do you prioritize tasks and focus on the most important or urgent ones first?", 1, 2));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //2
            uid = insertQuestion(new Question("How often do you use a timer to stay on track during study sessions?", 1, 2));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //2
            uid = insertQuestion(new Question("How often do you eliminate unnecessary activities or distractions from your schedule?", 1, 2));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //2
            uid = insertQuestion(new Question("How often do you make a plan for the next day before going to bed?", 1, 2));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //3
            uid = insertQuestion(new Question("How often do you develop a consistent study schedule and stick to it?", 1, 3));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //3
            uid = insertQuestion(new Question("How often do you find a study method that works for you, such as flashcards or summarizing notes?", 1, 3));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //3
            uid = insertQuestion(new Question("How often do you take frequent breaks during study sessions?", 1, 3));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //3
            uid = insertQuestion(new Question("How often do you use a timer to keep track of your study time?", 1, 3));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //3
            uid = insertQuestion(new Question("How often do you NOT review material regularly and practice with practice tests or quizzes?", -1, 3));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //4
            uid = insertQuestion(new Question("How often do you find yourself constantly checking social media, phone, or other electronic devices while studying?", -1, 4));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //4
            uid = insertQuestion(new Question("How often do you eliminate electronic distractions, such as social media or phone notifications while studying?", 1, 4));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //4
            uid = insertQuestion(new Question("How often do you create a designated study space free from distractions?", 1, 4));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //4
            uid = insertQuestion(new Question("How often do you use apps or website blockers during study time?", 1, 4));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //4
            uid = insertQuestion(new Question("How often do you put your phone on silent or in another room while studying?", 1, 4));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //4
            uid = insertQuestion(new Question("How often do you use noise-canceling headphones to block out background noise while studying?", 1, 4));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //5
            uid = insertQuestion(new Question("How often do you establish a consistent sleep schedule and aim for 7-8 hours of sleep per night?", 1, 5));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //5
            uid = insertQuestion(new Question("How often do you avoid screens and stimulating activities before bedtime?", 1, 5));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //5
            uid = insertQuestion(new Question("How often do you create a bedtime environment to relax and wind down?", 1, 5));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //5
            uid = insertQuestion(new Question("How often do you keep your bedroom cool, dark, and quiet for a good night's sleep?", 1, 5));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //6
            uid = insertQuestion(new Question("How often do you use positive affirmations to stay motivated?", 1, 6));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //6
            uid = insertQuestion(new Question("How often do you take a short break to do something you enjoy after completing a task?", 1, 6));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //6
            uid = insertQuestion(new Question("How often do you practice stress-relieving activities, such as meditation or yoga?", 1, 6));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //6
            uid = insertQuestion(new Question("How often do you reflect on your own learning process and identify strengths and achievements?", 1, 6));
            choices.clear();
            choices.add(Frequency.NEVER.getValue());
            choices.add(Frequency.RARELY.getValue());
            choices.add(Frequency.SOMETIMES.getValue());
            choices.add(Frequency.OFTEN.getValue());
            choices.add(Frequency.ALWAYS.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //6
            uid = insertQuestion(new Question("How often do you feel unmotivated and disengaged in coursework?", -1, 6));
            choices.clear();
            choices.add(Frequency.NEVER_.getValue());
            choices.add(Frequency.RARELY_.getValue());
            choices.add(Frequency.SOMETIMES_.getValue());
            choices.add(Frequency.OFTEN_.getValue());
            choices.add(Frequency.ALWAYS_.getValue());
            assessmentDao.insertChoices(setFk_Question_uid(choices, uid));

            //6
            uid = insertQuestion(new Question("How often do you have a negative attitude and mindset towards your studies?", -1, 6));
            choices.clear();
            choices.add(Frequency.NEVER_.getValue());
            choices.add(Frequency.RARELY_.getValue());
            choices.add(Frequency.SOMETIMES_.getValue());
            choices.add(Frequency.OFTEN_.getValue());
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
                                Log.d("tag", "onFetchHabitSuccess: " + habits);
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
            //ONBOARDING
            //Triggered when first time adding new habit on reform. This is triggered in analysis fragment when clicking and confirming the habit to be added.
            //1
            Achievement FirstStep = new Achievement("First Step", "Take a leap in reforming a bad habit", 0, 1);
            achievementDao.insertAchievement(FirstStep);

            //HOME
            //Create and add teh first custom habit
            //2
            Achievement CreateCustomHabit = new Achievement("Custom Habit", "Create a custom habit to reform on your liking.", 0, 1);
            achievementDao.insertAchievement(CreateCustomHabit);

            //Update habit title
            //3
            Achievement EditCustomHabitTitle = new Achievement("Edit Custom Habit Title", "Update the title of a custom habit you have created.", 0, 1);
            achievementDao.insertAchievement(EditCustomHabitTitle);

            //Comment
            //4
            Achievement CommentI = new Achievement("First Comment", "Post your first comment on a habit", 0, 1);
            achievementDao.insertAchievement(CommentI);
            //5
            Achievement CommentII = new Achievement("Piling Up", "Comment on 10 habits", 1, achievementDao.getAchievementByTitle(CommentI.getTitle()), 10);
            achievementDao.insertAchievement(CommentII);
            //6
            Achievement CommentIII = new Achievement("Thinker", "Comment on 30 habits", 1, achievementDao.getAchievementByTitle(CommentII.getTitle()), 30);
            achievementDao.insertAchievement(CommentIII);

            //SUBROUTINES
            //7
            Achievement SubroutineI = new Achievement("First Subroutine", "Complete your first subroutine", 0, 1);
            achievementDao.insertAchievement(SubroutineI);
            //8
            Achievement SubroutineII = new Achievement("Subroutine Done?", "Complete 10 subroutines", 1, achievementDao.getAchievementByTitle(SubroutineI.getTitle()), 10);
            achievementDao.insertAchievement(SubroutineII);
            //9
            Achievement SubroutineIII = new Achievement("More Subroutine Completed", "Complete 25 subroutines", 1, achievementDao.getAchievementByTitle(SubroutineII.getTitle()), 25);
            achievementDao.insertAchievement(SubroutineIII);
            //10
            Achievement SubroutineIV = new Achievement("Subroutine Intermediate", "Complete 50 subroutines", 1, achievementDao.getAchievementByTitle(SubroutineIII.getTitle()), 50);
            achievementDao.insertAchievement(SubroutineIV);
            //11
            Achievement SubroutineV = new Achievement("Subroutine Experienced", "Complete 100 subroutines", 1, achievementDao.getAchievementByTitle(SubroutineIV.getTitle()), 100);
            achievementDao.insertAchievement(SubroutineV);
            //12
            Achievement SubroutineVI = new Achievement("Subroutine Advanced", "Complete 150 subroutines", 1, achievementDao.getAchievementByTitle(SubroutineV.getTitle()), 150);
            achievementDao.insertAchievement(SubroutineVI);

            //Modify Subroutines
            //13
            Achievement EditCustomHabitSubroutine = new Achievement("Edit Custom Habit Subroutine", "Update the subroutines of a custom habit you have created.", 0, 1);
            achievementDao.insertAchievement(EditCustomHabitSubroutine);

            //JOURNAL
            //Triggered in adding notes
            //14
            Achievement NoteI = new Achievement("First Note", "Write down your thoughts", 0, 1);
            achievementDao.insertAchievement(NoteI);
            //15
            Achievement NoteII = new Achievement("Piece by Piece", "Journal your progress", 1, achievementDao.getAchievementByTitle(NoteI.getTitle()), 10);
            achievementDao.insertAchievement(NoteII);
            //16
            Achievement NoteIII = new Achievement("Better Understanding", "Lots of notes written", 1, achievementDao.getAchievementByTitle(NoteII.getTitle()), 30);
            achievementDao.insertAchievement(NoteIII);
            //17
            Achievement NoteIV = new Achievement("Chapter", "Keep writing you thoughts", 1, achievementDao.getAchievementByTitle(NoteIII.getTitle()), 60);
            achievementDao.insertAchievement(NoteIV);
            //18
            Achievement NoteV = new Achievement("Journal", "You have written your thoughts", 1, achievementDao.getAchievementByTitle(NoteIV.getTitle()), 100);
            achievementDao.insertAchievement(NoteV);

            //SETTING
            //Set a theme
            //19
            Achievement Theme = new Achievement("Secret", "Hidden", 0, 1);
            achievementDao.insertAchievement(Theme);

            //MISC
            //Triggered by duration of application since installed
            //20
            Achievement WeekLong = new Achievement("Been a week", "The application was installed for 7 days", 0, 1);
            achievementDao.insertAchievement(WeekLong);
            //21
            Achievement MonthLong = new Achievement("You have stayed", "The application was installed for 30 days", 0, 1);
            achievementDao.insertAchievement(MonthLong);
            //22
            Achievement Three_Month = new Achievement("Keep growing", "The application was installed for 90 days", 0, 1);
            achievementDao.insertAchievement(Three_Month);
            //23
            Achievement YearLong = new Achievement("Great Dedication", "The application was installed for 365 days", 0, 1);
            achievementDao.insertAchievement(YearLong);

            //Triggered by swiping in left and right direction in top bar to navigate between menus
            //24
            Achievement RightSwipeNavigation = new Achievement("Secret", "Hidden", 0, 1);
            achievementDao.insertAchievement(RightSwipeNavigation);
            //25
            Achievement LeftSwipeNavigation = new Achievement("Secret", "Hidden", 0, 1);
            achievementDao.insertAchievement(LeftSwipeNavigation);
            //26
            Achievement CloseApplicationPrompt = new Achievement("Secret", "Hidden", 0, 1);
            achievementDao.insertAchievement(CloseApplicationPrompt);
            return null;
        }
    }
}
