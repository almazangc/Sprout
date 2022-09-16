package com.example.sprout.database.Assestment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    private AssessmentRepository repository;
    private LiveData<List<Assessment>> allAssessmentListLiveData;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        allAssessmentListLiveData = repository.getAllAssessment();
    }

    public void insert(Assessment assessment){
        repository.insert(assessment);
    }

    public void update(Assessment assessment){
        repository.update(assessment);
    }

    public void delete(Assessment assessment){
        repository.delete(assessment);
    }

    public void deleteAllAssessment(){
        repository.deleteAll();
    }

    public LiveData<List<Assessment>> getAllAssessmentListLiveData() {
        return allAssessmentListLiveData;
    }

}
