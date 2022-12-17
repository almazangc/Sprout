package com.habitdev.sprout.database.assessment;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.AppDatabase;
import com.habitdev.sprout.database.assessment.model.Answer;
import com.habitdev.sprout.database.assessment.model.Choices;
import com.habitdev.sprout.database.assessment.model.Question;

import org.checkerframework.checker.units.qual.A;

import java.util.List;

public class AssessmentRepository {
    private final AssessmentDao assessmentDao;
    private final LiveData<List<Assessment>> AssessmentsListLiveData;
    private final LiveData<List<Answer>> getAllAnswerListLiveData;
    private final List<Answer> getAllAnswerList;

    public AssessmentRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        this.assessmentDao = appDatabase.assessmentDao();
        AssessmentsListLiveData = assessmentDao.getAssessmentsListLiveData();
        getAllAnswerListLiveData = assessmentDao.getAllAnswerListLiveData();
        getAllAnswerList = assessmentDao.getAllAnswerList();
    }

    public void updateQuestion(Question question) {
        new AssessmentRepository.UpdateQuestionAsyncTask(assessmentDao).execute(question);
    }

    public void updateChoice(Choices choices) {
        new AssessmentRepository.UpdateChoicesAsyncTask(assessmentDao).execute(choices);
    }

    public void insertAnswer(Answer answer) {
        assessmentDao.insertAnswer(answer); //snychrounous
//        new AssessmentRepository.InsertAnswerAsyncTask(assessmentDao).execute(answer);
    }

    public void updateAnswer(Answer answer) {
        assessmentDao.updateAnswer(answer); //snychrounous
//        new AssessmentRepository.UpdateAnswerAsyncTask(assessmentDao).execute(answer);
    }

    public void deleteQuestion(Question question) {
        new AssessmentRepository.DeleteQuestionAsyncTask(assessmentDao).execute(question);
    }

    public void deleteChoice(Choices choices) {
        new AssessmentRepository.DeleteChoiceAsyncTask(assessmentDao).execute(choices);
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

    public static class InsertAnswerAsyncTask extends AsyncTask<Answer, Void, Void> {

        private final AssessmentDao assessmentDao;

        public InsertAnswerAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Answer... answers) {
            assessmentDao.insertAnswer(answers[0]);
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

    public LiveData<List<Assessment>> getAssessmentsListLiveData() {
        return AssessmentsListLiveData;
    }

    public LiveData<List<Answer>> getGetAllAnswerListLiveData() {
        return getAllAnswerListLiveData;
    }

    public List<Answer> getGetAllAnswerList() {
        return getAllAnswerList;
    }

    public List<Question> getQuestionList() {
        return assessmentDao.getAllQuestion();
    }

    public List<Choices> getChoicesList(long uid) {
        return assessmentDao.getAllChoices(uid);
    }

    public long doesAnswerExist(long uid){
        return assessmentDao.doesAnswerExist(uid);
    }

    public Answer getAnswerByFkQuestionUID(long uid){
        return assessmentDao.getAnswerByFkQuestionUID(uid);
    }
}
