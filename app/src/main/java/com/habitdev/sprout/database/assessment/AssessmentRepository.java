package com.habitdev.sprout.database.assessment;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.AppDatabase;
import com.habitdev.sprout.database.assessment.model.Answer;
import com.habitdev.sprout.database.assessment.model.Assessment;
import com.habitdev.sprout.database.assessment.model.AssessmentRecord;
import com.habitdev.sprout.database.assessment.model.Choices;
import com.habitdev.sprout.database.assessment.model.Question;

import java.util.List;

public class AssessmentRepository {
    private final AssessmentDao assessmentDao;
    private final LiveData<List<Assessment>> getAllAssessmentsListLiveData;
    private final LiveData<List<AssessmentRecord>> getAssessmentRecordLiveData;

    public AssessmentRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        this.assessmentDao = appDatabase.assessmentDao();
        getAllAssessmentsListLiveData = assessmentDao.getAssessmentsListLiveData();
        getAssessmentRecordLiveData = assessmentDao.getAssessmentRecordLiveData();
    }

    public void insertAnswer(Answer answer) {
        assessmentDao.insertAnswer(answer);
    }

    public long insertAssessmentRecord(AssessmentRecord assessmentRecords){
        return assessmentDao.insertAssessmentRecord(assessmentRecords);
    }

    public void updateQuestion(Question question) {
        new AssessmentRepository.UpdateQuestionAsyncTask(assessmentDao).execute(question);
    }

    public void updateChoice(Choices choices) {
        new AssessmentRepository.UpdateChoicesAsyncTask(assessmentDao).execute(choices);
    }

    public void updateAnswer(Answer answer) {
//        new AssessmentRepository.UpdateAnswerAsyncTask(assessmentDao).execute(answer);
        assessmentDao.updateAnswer(answer);
    }

    public void updateAssessmentRecord(AssessmentRecord assessmentRecord){
        assessmentDao.updateAssessmentRecord(assessmentRecord);
    }

    public void deleteQuestion(Question question) {
        new AssessmentRepository.DeleteQuestionAsyncTask(assessmentDao).execute(question);
    }

    public void deleteChoice(Choices choices) {
        new AssessmentRepository.DeleteChoiceAsyncTask(assessmentDao).execute(choices);
    }

    public void deleteAssessmentRecord(AssessmentRecord assessmentRecord){
        new AssessmentRepository.DeleteAssessmentRecordAsyncTask(assessmentDao).execute(assessmentRecord);
    }

    public static class UpdateQuestionAsyncTask extends AsyncTask<Question, Void, Void> {

        private final AssessmentDao assessmentDao;

        public UpdateQuestionAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Question... questions) {
            assessmentDao.updateQuestion(questions[0]);
            return null;
        }
    }

    public static class UpdateAnswerAsyncTask extends AsyncTask<Answer, Void, Void> {

        private final AssessmentDao assessmentDao;

        public UpdateAnswerAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Answer... answers) {
            assessmentDao.updateAnswer(answers[0]);
            return null;
        }
    }

    public static class UpdateChoicesAsyncTask extends AsyncTask<Choices, Void, Void> {

        private final AssessmentDao assessmentDao;

        public UpdateChoicesAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Choices... choices) {
            assessmentDao.updateChoice(choices[0]);
            return null;
        }
    }

    public static class DeleteQuestionAsyncTask extends AsyncTask<Question, Void, Void> {

        private final AssessmentDao assessmentDao;

        public DeleteQuestionAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Question... questions) {
            assessmentDao.deleteQuestion(questions[0]);
            return null;
        }
    }

    public static class DeleteChoiceAsyncTask extends AsyncTask<Choices, Void, Void> {

        private final AssessmentDao assessmentDao;

        public DeleteChoiceAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Choices... choices) {
            assessmentDao.deleteChoice(choices[0]);
            return null;
        }
    }

    public static class DeleteAssessmentRecordAsyncTask extends AsyncTask<AssessmentRecord, Void, Void> {

        private final AssessmentDao assessmentDao;

        public DeleteAssessmentRecordAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(AssessmentRecord... assessmentRecords) {
            assessmentDao.deleteAssessmentRecord(assessmentRecords[0]);
            return null;
        }
    }

    public LiveData<List<Assessment>> getGetAllAssessmentsListLiveData() {
        return getAllAssessmentsListLiveData;
    }

    public List<Assessment> getAllAssessmentList() {
        return assessmentDao.getAllAssessmentsList();
    }

    public List<Question> getQuestionList() {
        return assessmentDao.getAllQuestion();
    }

    public List<Question> getShuffledQuestions(){
        return assessmentDao.getShuffledQuestions();
    }

    public LiveData<List<Answer>> getGetAllAnswerListLiveData(long uid) {
        return assessmentDao.getAllAnswerListLiveData(uid);
    }

    public List<Answer> getAllAnswerList(long uid) {
        return assessmentDao.getAllAnswerList(uid);
    }

    public List<Choices> getAllChoicesByUID(long uid) {
        return assessmentDao.getAllChoicesByUID(uid);
    }

    public long doesAnswerExist(long record_uid, long question_uid){
        return assessmentDao.doesAnswerExist(record_uid, question_uid);
    }

    public Answer getAnswerByFkQuestionUID(long record_uid, long question_uid){
        return assessmentDao.getAnswerByFkQuestionUID(record_uid, question_uid);
    }

    public LiveData<List<AssessmentRecord>> getGetAssessmentRecordLiveData() {
        return getAssessmentRecordLiveData;
    }

    public  List<AssessmentRecord> getAssessmentRecord() {
        return assessmentDao.getAssessmentRecord();
    }

    public AssessmentRecord getAssessmentRecordByUID(long uid) {
        return assessmentDao.getAssessmentRecordByUID(uid);
    }

    public long getUncompletedAssessmentRecordUID() {
        return assessmentDao.getUncompletedAssessmentRecordUID();
    }

    public int getUncompletedAssessmentRecordCount() {
        return assessmentDao.getUncompletedAssessmentRecordCount();
    }

    public AssessmentRecord getLatestCompletedAssessmentRecord() {
        return assessmentDao.getLatestCompletedAssessmentRecord();
    }

    public void deleteAnswersByAssessmentRecordUid(long uid){
        assessmentDao.deleteAnswersByAssessmentRecordUid(uid);
    }
}
