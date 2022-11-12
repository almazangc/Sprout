package com.prototype.sprout.database.sub_routine;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RoutineViewModel extends AndroidViewModel {

    private RoutineRepository repository;
    private List<Routine> allRoutineList;
    private List<String> getRoutines;
    private LiveData<List<Routine>> allRoutineListLiveData;

    public RoutineViewModel(@NonNull Application application) {
        super(application);
        repository = new RoutineRepository(application);
        allRoutineList = repository.getAllRoutineList();
        getRoutines = repository.getGetRoutines();
        allRoutineListLiveData = repository.getAllRoutineListLiveData();
    }

    public void insert(Routine routine) {
        repository.insert(routine);
    }

    public void update(Routine routine) {
        repository.update(routine);
    }

    public void delete(Routine routine) {
        repository.delete(routine);
    }

    public void deleteAllRoutines() {
        repository.deleteAll();
    }

    public List<Routine> getAllRoutineList() {
        return allRoutineList;
    }

    public List<String> getGetRoutines() {
        return getRoutines;
    }

    public LiveData<List<Routine>> getAllRoutineListLiveData() {
        return allRoutineListLiveData;
    }

    public List<Routine> getRoutine(int uid){
        return repository.getRoutine(uid);
    }
}
