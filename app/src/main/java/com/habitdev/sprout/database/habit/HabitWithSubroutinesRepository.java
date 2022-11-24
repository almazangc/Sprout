package com.habitdev.sprout.database.habit;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.AppDatabase;
import com.habitdev.sprout.database.assessment.AssessmentDao;
import com.habitdev.sprout.database.assessment.model.Question;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;

import java.util.List;

public class HabitWithSubroutinesRepository {
    private HabitWithSubroutinesDao habitWithSubroutinesDao;
    private List<Habits> allHabitOnReform;
    private LiveData<List<Habits>> allHabitOnReformLiveData;
    private List<HabitWithSubroutines> allHabitOnReformWithSubroutines;
    private LiveData<List<HabitWithSubroutines>> allHabitOnReformWithSubroutinesLiveData;
    private List<Long> allHabitOnReformUID;
    private List<String> allHabitTitle;
    private List<Habits> allHabits;
    private LiveData<Long> getHabitOnReformCount;

    public HabitWithSubroutinesRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        this.habitWithSubroutinesDao = appDatabase.habitsDao();
        this.allHabitOnReform = habitWithSubroutinesDao.getAllHabitOnReform();
        this.allHabitOnReformLiveData = habitWithSubroutinesDao.getAllHabitOnReformLiveData();
        this.allHabitOnReformWithSubroutines = habitWithSubroutinesDao.getAllHabitsOnReformWithSubroutines();
        this.allHabitOnReformWithSubroutinesLiveData = habitWithSubroutinesDao.getAllHabitsOnReformWithSubroutinesLiveData();
        this.allHabitOnReformUID = habitWithSubroutinesDao.getAllHabitsOnReformUID();
        this.allHabitTitle = habitWithSubroutinesDao.getAllHabitTitle();
        this.allHabits = habitWithSubroutinesDao.getAllHabit();
        this.getHabitOnReformCount = habitWithSubroutinesDao.getHabitOnReformCount();
    }

    public void updateHabit(Habits habit) {
        new UpdateHabitAsyncTask(habitWithSubroutinesDao).execute(habit);
    }

    public static class UpdateHabitAsyncTask extends AsyncTask<Habits, Void, Void> {

        private final HabitWithSubroutinesDao habitWithSubroutinesDao;

        public UpdateHabitAsyncTask(HabitWithSubroutinesDao habitWithSubroutinesDao) {
            this.habitWithSubroutinesDao = habitWithSubroutinesDao;
        }

        @Override
        protected Void doInBackground(Habits... habit) {
            habitWithSubroutinesDao.updateHabit(habit[0]);
            return null;
        }
    }

    public List<Habits> getAllHabitOnReform() {
        return allHabitOnReform;
    }

    public LiveData<List<Habits>> getAllHabitOnReformLiveData() {
        return allHabitOnReformLiveData;
    }

    public List<HabitWithSubroutines> getAllHabitOnReformWithSubroutines() {
        return allHabitOnReformWithSubroutines;
    }

    public LiveData<List<HabitWithSubroutines>> getAllHabitOnReformWithSubroutinesLiveData() {
        return allHabitOnReformWithSubroutinesLiveData;
    }

    public List<Long> getAllHabitOnReformUID() {
        return allHabitOnReformUID;
    }

    public List<Subroutines> getAllSubroutinesOnReformHabit(long uid) {
        return habitWithSubroutinesDao.getAllSubroutinesOfHabit(uid);
    }

    public LiveData<List<Subroutines>> getAllSubroutinesOnReformHabitLiveData(long uid) {
        return habitWithSubroutinesDao.getAllSubroutinesOfHabitLiveData(uid);
    }

    public List<String> getAllHabitTitle() {
        return allHabitTitle;
    }

    public List<Habits> getAllHabits() {
        return allHabits;
    }

    public LiveData<Long> getGetHabitOnReformCount() {
        return getHabitOnReformCount;
    }

    public void updateOnReformStatus(boolean bool, long uid){
        habitWithSubroutinesDao.updateOnReformStatus(bool, uid);
    }
}
