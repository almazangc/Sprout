package com.habitdev.sprout.database.assessment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.assessment.model.Answer;
import com.habitdev.sprout.database.assessment.model.Assessment;
import com.habitdev.sprout.database.assessment.model.AssessmentRecord;
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

    public LiveData<List<Answer>> getGetAllAnswerListLiveData(long uid) {
        return repository.getGetAllAnswerListLiveData(uid);
    }

    public List<Answer> getAllAnswerList(long uid) {
        return repository.getAllAnswerList(uid);
    }

    public List<Choices> getAllChoicesByUID(long uid) {
        return repository.getAllChoicesByUID(uid);
    }

    public long doesAnswerExist(long record_uid, long question_uid){
        return repository.doesAnswerExist(record_uid, question_uid);
    }

    public Answer getAnswerByFkQuestionUID(long record_uid, long question_uid){
        return repository.getAnswerByFkQuestionUID(record_uid, question_uid);
    }

    public LiveData<List<AssessmentRecord>> getAssessmentRecordLiveData() {
        return repository.getAssessmentRecordLiveData();
    }

    public  List<AssessmentRecord> getAssessmentRecord() {
        return repository.getAssessmentRecord();
    }

    public AssessmentRecord getAssessmentRecordByUID(long uid) {
        return repository.getAssessmentRecordByUID(uid);
    }

    public long getUncompletedAssessmentRecordUID() {
        return repository.getUncompletedAssessmentRecordUID();
    }

    public int getUncompletedAssessmentRecordCount() {
        return repository.getUncompletedAssessmentRecordCount();
    }

    public AssessmentRecord getLatestCompletedAssessmentRecord() {
        return repository.getLatestCompletedAssessmentRecord();
    }

    public void deleteAnswersByAssessmentRecordUid(long uid){
        repository.deleteAnswersByAssessmentRecordUid(uid);
    }

    public void insertAnswer(Answer answer) {
        repository.insertAnswer(answer);
    }

    public long insertAssessmentRecord(AssessmentRecord assessmentRecords){
        return repository.insertAssessmentRecord(assessmentRecords);
    }


    public void updateQuestion(Question question) {
        repository.updateQuestion(question);
    }

    public void updateChoice(Choices choices) {
        repository.updateChoice(choices);
    }

    public void updateAnswer(Answer answer) {
        repository.updateAnswer(answer);
    }

    public void updateAssessmentRecord(AssessmentRecord assessmentRecord){
        repository.updateAssessmentRecord(assessmentRecord);
    }

    public void deleteQuestion(Question question) {
       repository.deleteQuestion(question);
    }

    public void deleteChoice(Choices choices) {
        repository.deleteChoice(choices);
    }

    public void deleteAssessmentRecord(AssessmentRecord assessmentRecord){
        repository.deleteAssessmentRecord(assessmentRecord);
    }

}
