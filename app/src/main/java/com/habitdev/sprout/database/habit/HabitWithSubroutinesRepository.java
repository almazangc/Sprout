package com.habitdev.sprout.database.habit;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.AppDatabase;
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
    private LiveData<List<Habits>> allHabitListLiveData;
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
        this.allHabitListLiveData = habitWithSubroutinesDao.getAllHabitListLiveData();
        this.allHabits = habitWithSubroutinesDao.getAllHabitList();
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

    public long insertHabit(Habits habits){
        return habitWithSubroutinesDao.insertHabit(habits);
    }

    public void insertSubroutines(List<Subroutines> subroutinesList){
        habitWithSubroutinesDao.insertSubroutines(subroutinesList);
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

    public List<Subroutines> getAllSubroutinesOfHabit(long uid) {
        return habitWithSubroutinesDao.getAllSubroutinesOfHabit(uid);
    }

    public LiveData<List<Subroutines>> getAllSubroutinesOnReformHabitLiveData(long uid) {
        return habitWithSubroutinesDao.getAllSubroutinesOfHabitLiveData(uid);
    }

    public LiveData<List<Habits>> getAllHabitListLiveData() {
        return allHabitListLiveData;
    }

    public List<Habits> getAllHabits() {
        return allHabits;
    }

    public LiveData<Long> getGetHabitOnReformCount() {
        return getHabitOnReformCount;
    }
}
