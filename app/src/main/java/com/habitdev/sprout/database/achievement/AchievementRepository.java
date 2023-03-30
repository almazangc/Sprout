package com.habitdev.sprout.database.achievement;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.AppDatabase;
import com.habitdev.sprout.database.achievement.model.Achievement;

import java.util.List;

public class AchievementRepository {

    private final AchievementDao achievementDao;

    public AchievementRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDbInstance(application);
        achievementDao = appDatabase.achievementDao();
    }

    public  LiveData<List<Achievement>> getAllAcheivementLiveDataList(){
        return achievementDao.getAllAcheivementLiveDataList();
    }

    public List<Achievement> getAllAchievementList(){
        return achievementDao.getAllAchievementList();
    }

    public Achievement getAchievementByUID(long pk_achievement_uid) {
        return achievementDao.getAchievementByUID(pk_achievement_uid);
    }

    public LiveData<Integer> getLiveDataTotalAchievementsCount(){
        return achievementDao.getLiveDataTotalAchievementsCount();
    }

    public LiveData<Integer> getCompletedAchievementsCount(){
        return achievementDao.getCompletedAchievementsCount();
    }

    public int getTotalAchievementsCount(){
        return achievementDao.getTotalAchievementsCount();
    }

    public void insertAchievement(Achievement achievement) {
        new InsertMilestoneAsyncTask(achievementDao).execute(achievement);
    }

    public void updateAchievement(Achievement achievement) {
        new UpdateMilestoneAsyncTask(achievementDao).execute(achievement);
    }

    public void deleteAchievement(Achievement achievement) {
        new DeleteMilestoneAsyncTask(achievementDao).execute(achievement);
    }

    public static class InsertMilestoneAsyncTask extends AsyncTask<Achievement, Void, Void> {

        private final AchievementDao achievementDao;

        public InsertMilestoneAsyncTask(AchievementDao achievementDao) {
            this.achievementDao = achievementDao;
        }

        @Override
        protected Void doInBackground(Achievement... achievements) {
            achievementDao.insertAchievement(achievements[0]);
            return null;
        }
    }

    public static class UpdateMilestoneAsyncTask extends AsyncTask<Achievement, Void, Void> {

        private final AchievementDao achievementDao;

        public UpdateMilestoneAsyncTask(AchievementDao achievementDao) {
            this.achievementDao = achievementDao;
        }

        @Override
        protected Void doInBackground(Achievement... achievements) {
            achievementDao.updateAchievement(achievements[0]);
            return null;
        }
    }

    public static class DeleteMilestoneAsyncTask extends AsyncTask<Achievement, Void, Void> {

        private final AchievementDao achievementDao;

        public DeleteMilestoneAsyncTask(AchievementDao achievementDao) {
            this.achievementDao = achievementDao;
        }

        @Override
        protected Void doInBackground(Achievement... achievements) {
            achievementDao.deleteAchievement(achievements[0]);
            return null;
        }
    }
}
