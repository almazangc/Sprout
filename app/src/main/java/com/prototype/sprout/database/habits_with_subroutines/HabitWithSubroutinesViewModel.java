package com.prototype.sprout.database.habits_with_subroutines;

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

    public HabitWithSubroutinesViewModel(@NonNull Application application) {
        super(application);
        repository = new HabitWithSubroutinesRepository(application);
        allHabitOnReform = repository.getAllHabitOnReform();
        allHabitOnReformLiveData = repository.getAllHabitOnReformLiveData();
        allHabitOnReformWithSubroutines = repository.getAllHabitOnReformWithSubroutines();
        allHabitOnReformWithSubroutinesLiveData = repository.getAllHabitOnReformWithSubroutinesLiveData();
        this.allHabitOnReformUID = repository.getAllHabitOnReformUID();
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
}
