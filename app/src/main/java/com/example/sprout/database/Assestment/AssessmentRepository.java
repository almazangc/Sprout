package com.example.sprout.database.Assestment;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.sprout.database.AppDatabase;

import java.util.List;

public class AssessmentRepository {
    private AssessmentDao assessmentDao;
    private LiveData<List<Assessment>> allAssessment;

    public AssessmentRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        assessmentDao = appDatabase.assessmentDao();
        allAssessment = assessmentDao.getALLAssessmentLiveData();
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
        new DeleteAssessmentAsyncTask(assessmentDao).execute();
    }

    public LiveData<List<Assessment>> getAllAssessment() {
        return allAssessment;
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
}







