package com.prototype.sprout.database.habit;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prototype.sprout.database.AppDatabase;

import java.util.List;

public class HabitRepository {

    private HabitDao habitDao;
    private List<Habit> allhabitsList;
    private List<String> getHabits;
    private LiveData<List<Habit>> allhabitsListLiveData;

    public HabitRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        habitDao = appDatabase.habitDao();
        allhabitsList = habitDao.getAllHabits();
        getHabits = habitDao.getHabits();
        allhabitsListLiveData = habitDao.getAllHabitsLiveData();
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

    public List<Habit> getAllhabitsList() {
        return allhabitsList;
    }

    public List<String> getHabits() {
        return getHabits;
    }

    public LiveData<List<Habit>> getAllhabitsListLiveData() {
        return allhabitsListLiveData;
    }

    public List<Habit> getHabitList(int uid) {
        return habitDao.getHabit(uid);
    }
}

