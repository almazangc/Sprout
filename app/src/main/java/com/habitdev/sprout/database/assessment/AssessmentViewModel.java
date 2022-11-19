package com.habitdev.sprout.database.assessment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    private AssessmentRepository repository;
    private LiveData<List<Assessment>> allAssessmentListLiveData;
    private List<Assessment> allAssessmentList;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        allAssessmentListLiveData = repository.getAllAssessmentListLivedata();
        allAssessmentList = repository.getAllAssessmentList();
    }

    public void insert(Assessment assessment) {
        repository.insert(assessment);
    }

    public void update(Assessment assessment) {
        repository.update(assessment);
    }

    public void delete(Assessment assessment) {
        repository.delete(assessment);
    }

    public void deleteAllAssessment() {
        repository.deleteAll();
    }

    public LiveData<List<Assessment>> getAllAssessmentListLiveData() {
        return allAssessmentListLiveData;
    }

    public List<Assessment> getAllAssessmentList() {
        return allAssessmentList;
    }

    public String getAssessmentSelected(int uid){
        return repository.getAssessmentSelected(uid);
    }

    public void updateSelectedUID(int uid, String value){
        repository.updateSelectedUID(uid, value);
    }
}
