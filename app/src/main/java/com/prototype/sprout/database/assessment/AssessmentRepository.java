package com.prototype.sprout.database.assessment;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prototype.sprout.database.AppDatabase;

import java.util.List;

public class AssessmentRepository {
    private AssessmentDao assessmentDao;
    private LiveData<List<Assessment>> AssessmentListLivedata;
    private List<Assessment> AssessmentList;

    public AssessmentRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        assessmentDao = appDatabase.assessmentDao();
        AssessmentListLivedata = assessmentDao.getALLAssessmentLiveData();
        AssessmentList = assessmentDao.getALLAssessment();
    }

    public void insert(Assessment assessment) {
        new InsertAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void update(Assessment assessment) {
        new UpdateAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void delete(Assessment assessment) {
        new DeleteAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void deleteAll() {
        new DeleteAllAssessmentAsyncTask(assessmentDao).execute();
    }

    public static class InsertAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {

        private AssessmentDao assessmentDao;

        public InsertAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.insert(assessments[0]);
            return null;
        }
    }

    public static class UpdateAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {

        private AssessmentDao assessmentDao;

        public UpdateAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.update(assessments[0]);
            return null;
        }
    }

    public static class DeleteAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {

        private AssessmentDao assessmentDao;

        public DeleteAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.delete(assessments[0]);
            return null;
        }
    }

    public static class DeleteAllAssessmentAsyncTask extends AsyncTask<Void, Void, Void> {

        private AssessmentDao assessmentDao;

        public DeleteAllAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            assessmentDao.deleteAllAssessment();
            return null;
        }
    }

    public LiveData<List<Assessment>> getAssessmentListLivedata() {
        return AssessmentListLivedata;
    }

    public List<Assessment> getAssessmentList() {
        return AssessmentList;
    }

    public void updateSelectedUID(int uid, String value){
        assessmentDao.updateSelectedUID(uid, value);
    }

    public String getAssessmentSelected(int uid){
        return assessmentDao.getAssessmentSelected(uid);
    }
}







