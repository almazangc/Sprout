package com.habitdev.sprout.database.habit;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.AppDatabase;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;

import java.util.List;

public class HabitWithSubroutinesRepository {
    private final HabitWithSubroutinesDao habitWithSubroutinesDao;
    private final List<Habits> allHabitOnReform;
    private final LiveData<List<Habits>> allHabitOnReformLiveData;
    private final List<HabitWithSubroutines> allHabitOnReformWithSubroutines;
    private final LiveData<List<HabitWithSubroutines>> allHabitOnReformWithSubroutinesLiveData;
    private final List<Long> allHabitOnReformUID;
    private final LiveData<List<Habits>> allHabitListLiveData;
    private final List<Habits> allHabits;
    private final LiveData<Long> habitOnReformCount;
    private final LiveData<List<Habits>> allUserDefinedHabitListLiveData;

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
        this.habitOnReformCount = habitWithSubroutinesDao.getHabitOnReformCount();
        this.allUserDefinedHabitListLiveData = habitWithSubroutinesDao.getAllUserDefinedHabitListLiveData();
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

    public void updateSubroutine(Subroutines subroutine) {
        new UpdateSubroutineAsyncTask(habitWithSubroutinesDao).execute(subroutine);
    }

    public static class UpdateSubroutineAsyncTask extends AsyncTask<Subroutines, Void, Void> {

        private final HabitWithSubroutinesDao habitWithSubroutinesDao;

        public UpdateSubroutineAsyncTask(HabitWithSubroutinesDao habitWithSubroutinesDao) {
            this.habitWithSubroutinesDao = habitWithSubroutinesDao;
        }

        @Override
        protected Void doInBackground(Subroutines... subroutine) {
            habitWithSubroutinesDao.updateSubroutine(subroutine[0]);
            return null;
        }
    }

//    public void updateSubroutine(Subroutines subroutine) {
//        habitWithSubroutinesDao.updateSubroutine(subroutine);
//    }

    public void deleteHabit(Habits habit) {
        new DeleteHabitAsyncTask(habitWithSubroutinesDao).execute(habit);
    }

    public static class DeleteHabitAsyncTask extends AsyncTask<Habits, Void, Void> {

        private final HabitWithSubroutinesDao habitWithSubroutinesDao;

        public DeleteHabitAsyncTask(HabitWithSubroutinesDao habitWithSubroutinesDao) {
            this.habitWithSubroutinesDao = habitWithSubroutinesDao;
        }

        @Override
        protected Void doInBackground(Habits... habit) {
            habitWithSubroutinesDao.deleteHabit(habit[0]);
            return null;
        }
    }

    public void deleteSubroutine(Subroutines subroutine) {
        new DeleteSubroutineAsyncTask(habitWithSubroutinesDao).execute(subroutine);
    }

    public static class DeleteSubroutineAsyncTask extends AsyncTask<Subroutines, Void, Void> {

        private final HabitWithSubroutinesDao habitWithSubroutinesDao;

        public DeleteSubroutineAsyncTask(HabitWithSubroutinesDao habitWithSubroutinesDao) {
            this.habitWithSubroutinesDao = habitWithSubroutinesDao;
        }

        @Override
        protected Void doInBackground(Subroutines... subroutine) {
            habitWithSubroutinesDao.deleteSubroutine(subroutine[0]);
            return null;
        }
    }

    public void deleteSubroutineList(List<Subroutines> subroutines) {
        new DeleteSubroutineListAsyncTask(habitWithSubroutinesDao).execute(subroutines);
    }

    public static class DeleteSubroutineListAsyncTask extends AsyncTask<List<Subroutines>, Void, Void> {

        private final HabitWithSubroutinesDao habitWithSubroutinesDao;

        public DeleteSubroutineListAsyncTask(HabitWithSubroutinesDao habitWithSubroutinesDao) {
            this.habitWithSubroutinesDao = habitWithSubroutinesDao;
        }

        @Override
        protected Void doInBackground(List<Subroutines>... lists) {
            habitWithSubroutinesDao.deleteSubroutineList(lists[0]);
            return null;
        }
    }

    public long insertHabit(Habits habits){
        return habitWithSubroutinesDao.insertHabit(habits);
    }

    public void insertSubroutines(List<Subroutines> subroutinesList){
        habitWithSubroutinesDao.insertSubroutines(subroutinesList);
    }

    public void insertSubroutine(Subroutines subroutine){
        habitWithSubroutinesDao.insertSubroutine(subroutine);
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

    public LiveData<Long> getHabitOnReformCount() {
        return habitOnReformCount;
    }

    public LiveData<List<Habits>> getAllUserDefinedHabitListLiveData() {
        return allUserDefinedHabitListLiveData;
    }
}
