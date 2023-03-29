package com.habitdev.sprout.database.assessment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.assessment.model.Answer;
import com.habitdev.sprout.database.assessment.model.Assessment;
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
        return repository.getGetAllAssessmentsListLiveData();
    }

    public List<Assessment> getAllAssessmentList() {
        return  repository.getAllAssessmentList();
    }

    public List<Question> getAllQuestionList() {
        return repository.getQuestionList();
    }

    public List<Question> getShuffledQuestions(){
        return repository.getShuffledQuestions();
    }

    public LiveData<List<Answer>> getGetAllAnswerListLiveData() {
        return repository.getGetAllAnswerListLiveData();
    }

    public List<Answer> getAllAnswerList() {
        return repository.getAllAnswerList();
    }

    public List<Choices> getAllChoicesByUID(long uid) {
        return repository.getAllChoicesByUID(uid);
    }

    public long doesAnswerExist(long uid){
        return repository.doesAnswerExist(uid);
    }

    public Answer getAnswerByFkQuestionUID(long uid){
        return repository.getAnswerByFkQuestionUID(uid);
    }

    public void insertAnswer(Answer answer) {
        repository.insertAnswer(answer);
    }

    public void updateAnswer(Answer answer) {
        repository.updateAnswer(answer);
    }
}
