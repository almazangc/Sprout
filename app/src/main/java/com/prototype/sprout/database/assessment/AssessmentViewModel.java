package com.prototype.sprout.database.assessment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    private AssessmentRepository repository;
    private LiveData<List<Assessment>> assessmentListLiveData;
    private List<Assessment> assessmentList;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        assessmentListLiveData = repository.getAssessmentListLivedata();
        assessmentList = repository.getAssessmentList();
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

    public LiveData<List<Assessment>> getAssessmentListLiveData() {
        return assessmentListLiveData;
    }

    public List<Assessment> getAssessmentList() {
        return assessmentList;
    }

    public String getAssessmentSelected(int uid){
        return repository.getAssessmentSelected(uid);
    }

    public void updateSelectedUID(int uid, String value){
        repository.updateSelectedUID(uid, value);
    }
}
