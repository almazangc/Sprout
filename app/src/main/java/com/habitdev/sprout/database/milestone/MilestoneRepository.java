package com.habitdev.sprout.database.milestone;

import android.app.Application;
import android.os.AsyncTask;

import com.habitdev.sprout.database.AppDatabase;
import com.habitdev.sprout.database.milestone.model.Milestone;

public class MilestoneRepository {

    private final MilestoneDao milestoneDao;

    public MilestoneRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        milestoneDao = appDatabase.milestoneAchievementDao();
    }



    public void insert(Milestone milestone) {
        new InsertMilestoneAsyncTask(milestoneDao).execute(milestone);
    }

    public void update(Milestone milestone) {
        new UpdateMilestoneAsyncTask(milestoneDao).execute(milestone);
    }

    public void delete(Milestone milestone) {
        new DeleteMilestoneAsyncTask(milestoneDao).execute(milestone);
    }

    public static class InsertMilestoneAsyncTask extends AsyncTask<Milestone, Void, Void> {

        private final MilestoneDao milestoneDao;

        public InsertMilestoneAsyncTask(MilestoneDao milestoneDao) {
            this.milestoneDao = milestoneDao;
        }

        @Override
        protected Void doInBackground(Milestone... milestones) {
            milestoneDao.insert(milestones[0]);
            return null;
        }
    }

    public static class UpdateMilestoneAsyncTask extends AsyncTask<Milestone, Void, Void> {

        private final MilestoneDao milestoneDao;

        public UpdateMilestoneAsyncTask(MilestoneDao milestoneDao) {
            this.milestoneDao = milestoneDao;
        }

        @Override
        protected Void doInBackground(Milestone... milestones) {
            milestoneDao.update(milestones[0]);
            return null;
        }
    }

    public static class DeleteMilestoneAsyncTask extends AsyncTask<Milestone, Void, Void> {

        private final MilestoneDao milestoneDao;

        public DeleteMilestoneAsyncTask(MilestoneDao milestoneDao) {
            this.milestoneDao = milestoneDao;
        }

        @Override
        protected Void doInBackground(Milestone... milestones) {
            milestoneDao.delete(milestones[0]);
            return null;
        }
    }
}
