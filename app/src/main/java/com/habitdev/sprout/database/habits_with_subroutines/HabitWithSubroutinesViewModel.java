package com.habitdev.sprout.database.habits_with_subroutines;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class HabitWithSubroutinesViewModel extends AndroidViewModel {

    private HabitWithSubroutinesRepository repository;
    private List<Habits> allHabitOnReform;
    private LiveData<List<Habits>> allHabitOnReformLiveData;
    private List<HabitWithSubroutines> allHabitOnReformWithSubroutines;
    private LiveData<List<HabitWithSubroutines>> allHabitOnReformWithSubroutinesLiveData;
    private List<Long> allHabitOnReformUID;
    private List<String> allHabitTitle;
    private LiveData<Long> getHabitOnReformCount;

    public HabitWithSubroutinesViewModel(@NonNull Application application) {
        super(application);
        repository = new HabitWithSubroutinesRepository(application);
        allHabitOnReform = repository.getAllHabitOnReform();
        allHabitOnReformLiveData = repository.getAllHabitOnReformLiveData();
        allHabitOnReformWithSubroutines = repository.getAllHabitOnReformWithSubroutines();
        allHabitOnReformWithSubroutinesLiveData = repository.getAllHabitOnReformWithSubroutinesLiveData();
        allHabitOnReformUID = repository.getAllHabitOnReformUID();
        allHabitTitle = repository.getAllHabitTitle();
        getHabitOnReformCount = repository.getGetHabitOnReformCount();
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
        return repository.getAllSubroutinesOnReformHabit(uid);
    }

    public LiveData<List<Subroutines>> getAllSubroutinesOnReformHabitLiveData(long uid){
        return repository.getAllSubroutinesOnReformHabitLiveData(uid);
    }

    public List<String> getAllHabitTitle() {
        return allHabitTitle;
    }

    public LiveData<Long> getGetHabitOnReformCount() {
        return getHabitOnReformCount;
    }

    public void updateOnReformStatus(boolean bool, long uid){
        repository.updateOnReformStatus(bool, uid);
    }
}
