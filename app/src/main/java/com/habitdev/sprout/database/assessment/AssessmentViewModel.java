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

    private final AssessmentRepository repository;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
    }

    public LiveData<List<Assessment>> getAssessmentListLiveData() {
        return repository.getAssessmentsListLiveData();
    }

    public LiveData<List<Answer>> getGetAllAnswerListLiveData() {
        return repository.getGetAllAnswerListLiveData();
    }

    public List<Answer> getGetAllAnswerList() {
        return repository.getGetAllAnswerList();
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
