package com.habitdev.sprout.database.habit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;

import java.util.List;

public class HabitWithSubroutinesViewModel extends AndroidViewModel {

    private HabitWithSubroutinesRepository repository;
    private List<Habits> allHabitOnReform;
    private LiveData<List<Habits>> allHabitOnReformLiveData;
    private List<HabitWithSubroutines> allHabitOnReformWithSubroutines;
    private LiveData<List<HabitWithSubroutines>> allHabitOnReformWithSubroutinesLiveData;
    private List<Long> allHabitOnReformUID;
    private LiveData<List<Habits>> allHabitListLiveData;
    private List<Habits> allHabits;
    private LiveData<Long> getHabitOnReformCount;

    public HabitWithSubroutinesViewModel(@NonNull Application application) {
        super(application);
        repository = new HabitWithSubroutinesRepository(application);
        allHabitOnReform = repository.getAllHabitOnReform();
        allHabitOnReformLiveData = repository.getAllHabitOnReformLiveData();
        allHabitOnReformWithSubroutines = repository.getAllHabitOnReformWithSubroutines();
        allHabitOnReformWithSubroutinesLiveData = repository.getAllHabitOnReformWithSubroutinesLiveData();
        allHabitOnReformUID = repository.getAllHabitOnReformUID();
        allHabitListLiveData = repository.getAllHabitListLiveData();
        allHabits = repository.getAllHabits();
        getHabitOnReformCount = repository.getGetHabitOnReformCount();
    }

    public void update(Habits habit) {
        repository.updateHabit(habit);
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
        return repository.getAllSubroutinesOfHabit(uid);
    }

    public LiveData<List<Subroutines>> getAllSubroutinesOnReformHabitLiveData(long uid){
        return repository.getAllSubroutinesOnReformHabitLiveData(uid);
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
