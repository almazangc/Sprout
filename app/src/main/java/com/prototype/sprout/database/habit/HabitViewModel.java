package com.prototype.sprout.database.habit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class HabitViewModel extends AndroidViewModel {

    private HabitRepository repository;
    private List<Habit> allhabitsList;
    private LiveData<List<Habit>> allhabitsListLiveData;

    public HabitViewModel(@NonNull Application application) {
        super(application);
        repository = new HabitRepository(application);
        allhabitsList = repository.getAllhabitsList();
        allhabitsListLiveData = repository.getAllhabitsListLiveData();
    }

    public void insert(Habit habit) {
        repository.insert(habit);
    }

    public void update(Habit habit) {
        repository.update(habit);
    }

    public void delete(Habit habit) {
        repository.delete(habit);
    }

    public void deleteAllHabits() {
        repository.deleteAll();
    }

    public List<Habit> getAllhabitsList() {
        return allhabitsList;
    }

    public LiveData<List<Habit>> getAllhabitsListLiveData() {
        return allhabitsListLiveData;
    }

    public List<Habit> getHabitList(int uid) {
        return repository.getHabitList(uid);
    }
}
