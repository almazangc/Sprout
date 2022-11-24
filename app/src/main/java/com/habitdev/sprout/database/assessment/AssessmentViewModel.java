package com.habitdev.sprout.database.assessment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.assessment.model.Answer;
import com.habitdev.sprout.database.assessment.model.Choices;
import com.habitdev.sprout.database.assessment.model.Question;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {
    private AssessmentRepository repository;
    private LiveData<List<Assessment>> assessmentListLiveData;
    private final LiveData<List<Answer>> getAllAnswerListLiveData;


    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        assessmentListLiveData = repository.getAssessmentsListLiveData();
        getAllAnswerListLiveData = repository.getGetAllAnswerListLiveData();
    }

    public LiveData<List<Assessment>> getAssessmentListLiveData() {
        return assessmentListLiveData;
    }

    public LiveData<List<Answer>> getGetAllAnswerListLiveData() {
        return getAllAnswerListLiveData;
    }

    public List<Question> getQuestionList() {
        return repository.getQuestionList();
    }

    public List<Choices> getChoicesList(long uid) {
        return repository.getChoicesList(uid);
    }

    public long doesAnswerExist(long uid){
        return repository.doesAnswerExist(uid);
    }

    public Answer getAnswerByFkQuestionUID(long uid){
        return repository.getAnswerByFkQuestionUID(uid);
    }

    public void insert(Answer answer) {
        repository.insertAnswer(answer);
    }

    public void update(Answer answer) {
        repository.updateAnswer(answer);
    }
}
