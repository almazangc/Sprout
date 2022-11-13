package com.prototype.sprout.database.habit;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prototype.sprout.database.AppDatabase;

import java.util.List;

public class HabitRepository {

    private HabitDao habitDao;
    private List<Habit> allHabitList;
    private List<Habit> allHabitOnReform;
    private List<String> allHabitTitle;
    private LiveData<List<Habit>> allHabitListLiveData;
    private LiveData<List<Habit>> allHabitOnReformLiveData;

    public HabitRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        habitDao = appDatabase.habitDao();
        allHabitList = habitDao.getAllHabit();
        allHabitOnReform = habitDao.getAllHabitOnReform();
        allHabitTitle = habitDao.getAllHabitTitle();
        allHabitListLiveData = habitDao.getAllHabitLiveData();
        allHabitOnReformLiveData = habitDao.getAllHabitOnReformLiveData();
    }

    public void insert(Habit habit) {
        new HabitRepository.InsertHabitsAsyncTask(habitDao).execute(habit);
    }

    public void update(Habit habit) {
        new HabitRepository.UpdateHabitsAsyncTask(habitDao).execute(habit);
    }

    public void delete(Habit habit) {
        new HabitRepository.DeleteHabitsAsyncTask(habitDao).execute(habit);
    }

    public void deleteAll() {
        new HabitRepository.DeleteAllHabitsAsyncTask(habitDao).execute();
    }

    private static class InsertHabitsAsyncTask extends AsyncTask<Habit, Void, Void> {

        private HabitDao habitDao;

        private InsertHabitsAsyncTask(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected Void doInBackground(Habit... habits) {
            habitDao.insert(habits[0]);
            return null;
        }
    }

    private static class UpdateHabitsAsyncTask extends AsyncTask<Habit, Void, Void> {

        private HabitDao habitDao;

        private UpdateHabitsAsyncTask(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected Void doInBackground(Habit... habits) {
            habitDao.update(habits[0]);
            return null;
        }
    }

    private static class DeleteHabitsAsyncTask extends AsyncTask<Habit, Void, Void> {

        private HabitDao habitDao;

        private DeleteHabitsAsyncTask(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected Void doInBackground(Habit... habits) {
            habitDao.delete(habits[0]);
            return null;
        }
    }

    private static class DeleteAllHabitsAsyncTask extends AsyncTask<Void, Void, Void> {

        private HabitDao habitDao;

        private DeleteAllHabitsAsyncTask(HabitDao habitDao) {
            this.habitDao = habitDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            habitDao.deleteAllHabits();
            return null;
        }
    }

    public List<Habit> getAllHabitList() {
        return allHabitList;
    }

    public List<Habit> getAllHabitOnReform() {
        return allHabitOnReform;
    }

    public List<String> getHabits() {
        return allHabitTitle;
    }

    public LiveData<List<Habit>> getAllHabitListLiveData() {
        return allHabitListLiveData;
    }

    public List<Habit> getHabitList(int uid) {
        return habitDao.getAllHabitTitle(uid);
    }

    public LiveData<List<Habit>> getAllHabitOnReformLiveData() {
        return allHabitOnReformLiveData;
    }
}

