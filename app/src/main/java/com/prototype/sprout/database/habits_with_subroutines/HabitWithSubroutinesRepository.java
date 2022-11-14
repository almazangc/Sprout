package com.prototype.sprout.database.habits_with_subroutines;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.airbnb.lottie.L;
import com.prototype.sprout.database.AppDatabase;

import java.util.List;

public class HabitWithSubroutinesRepository {
    private HabitsDao habitsDao;
    private List<Habits> allHabitOnReform;
    private LiveData<List<Habits>> allHabitOnReformLiveData;
    private List<HabitWithSubroutines> allHabitOnReformWithSubroutines;
    private LiveData<List<HabitWithSubroutines>> allHabitOnReformWithSubroutinesLiveData;
    private List<Long> allHabitOnReformUID;

    public HabitWithSubroutinesRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        this.habitsDao = appDatabase.habitsDao();
        this.allHabitOnReform = habitsDao.getAllHabitOnReform();
        this.allHabitOnReformLiveData = habitsDao.getAllHabitOnReformLiveData();
        this.allHabitOnReformWithSubroutines = habitsDao.getAllHabitsOnReformWithSubroutines();
        this.allHabitOnReformWithSubroutinesLiveData = habitsDao.getAllHabitsOnReformWithSubroutinesLiveData();
        this.allHabitOnReformUID = habitsDao.getAllHabitsOnReformUID();
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
        return habitsDao.getAllSubroutinesOfHabit(uid);
    }

    public LiveData<List<Subroutines>> getAllSubroutinesOnReformHabitLiveData(long uid) {
        return habitsDao.getAllSubroutinesOfHabitLiveData(uid);
    }
}
