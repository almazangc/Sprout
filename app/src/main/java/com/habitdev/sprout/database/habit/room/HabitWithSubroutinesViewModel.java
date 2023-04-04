package com.habitdev.sprout.database.habit.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.model.room.Subroutines;

import java.util.List;

public class HabitWithSubroutinesViewModel extends AndroidViewModel {

    private final HabitWithSubroutinesRepository repository;
    private final List<Habits> allHabitOnReform;
    private final LiveData<List<Habits>> allHabitOnReformLiveData;
    private final List<HabitWithSubroutines> allHabitOnReformWithSubroutines;
    private final LiveData<List<HabitWithSubroutines>> allHabitOnReformWithSubroutinesLiveData;
    private final List<Long> allHabitOnReformUID;
    private final LiveData<List<Habits>> allHabitListLiveData;
    private final List<Habits> allHabits;
    private final LiveData<Long> getHabitOnReformCount;
    private final LiveData<Long> getPredefinedHabitOnReformCount;
    private final LiveData<List<Habits>> allUserDefinedHabitListLiveData;

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
        getHabitOnReformCount = repository.getHabitOnReformCount();
        getPredefinedHabitOnReformCount = repository.getPredefinedHabitOnReformCount();
        allUserDefinedHabitListLiveData = repository.getAllUserDefinedHabitListLiveData();
    }

    public void updateHabit(Habits habit) {
        repository.updateHabit(habit);
    }

    public void updateSubroutine(Subroutines subroutine) {repository.updateSubroutine(subroutine);}

    public void deleteHabit(Habits habit) {
        repository.deleteHabit(habit);
    }

    public void deleteSubroutine(Subroutines subroutine) {repository.deleteSubroutine(subroutine);}

    public void deleteSubroutineList(List<Subroutines>subroutines) {
        repository.deleteSubroutineList(subroutines);
    }

    public long insertHabit(Habits habits){
        return repository.insertHabit(habits);
    }

    public void insertSubroutine(Subroutines subroutine) {repository.insertSubroutine(subroutine);}

    public void insertSubroutines(List<Subroutines> subroutinesList){
        repository.insertSubroutines(subroutinesList);
    }

    public Habits getHabitByUID(long uid){
        return repository.getHabitByUID(uid);
    }

    public List<Habits> getAllHabitOnReform() {
        return allHabitOnReform;
    }

    public LiveData<List<Habits>> getAllHabitOnReformLiveData() {
        return allHabitOnReformLiveData;
    }

    public long getAllHabitOnReformCount() {
        return repository.getAllHabitOnReformCount();
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

    public LiveData<Long> getGetPredefinedHabitOnReformCount() {
        return getPredefinedHabitOnReformCount;
    }

    public LiveData<List<Habits>> getAllUserDefinedHabitListLiveData() {
        return allUserDefinedHabitListLiveData;
    }
}
