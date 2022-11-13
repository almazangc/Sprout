package com.prototype.sprout.database.habit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class HabitViewModel extends AndroidViewModel {

    private HabitRepository repository;
    private List<Habit> allHabitList;
    private List<Habit> allHabitOnReform;
    private List<String> allHabitTitle;
    private LiveData<List<Habit>> allHabitListLiveData;
    private LiveData<List<Habit>> allHabitOnReformLiveData;

    public HabitViewModel(@NonNull Application application) {
        super(application);
        repository = new HabitRepository(application);
        allHabitList = repository.getAllHabitList();
        allHabitOnReform = repository.getAllHabitOnReform();
        allHabitTitle = repository.getHabits();
        allHabitListLiveData = repository.getAllHabitListLiveData();
        allHabitOnReformLiveData = repository.getAllHabitOnReformLiveData();
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

    public List<Habit> getAllHabitList() {
        return allHabitList;
    }

    public List<Habit> getAllHabitOnReform() {
        return allHabitOnReform;
    }

    public List<String> getAllHabitTitle() {
        return allHabitTitle;
    }

    public LiveData<List<Habit>> getAllHabitListLiveData() {
        return allHabitListLiveData;
    }

    public List<Habit> getHabitList(int uid) {
        return repository.getHabitList(uid);
    }

    public LiveData<List<Habit>> getAllHabitOnReformLiveData() {
        return allHabitOnReformLiveData;
    }
}
