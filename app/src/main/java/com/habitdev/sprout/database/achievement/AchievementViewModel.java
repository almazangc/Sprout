package com.habitdev.sprout.database.achievement;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.habitdev.sprout.database.achievement.model.Achievement;

import java.util.List;

public class AchievementViewModel extends AndroidViewModel {

    private final AchievementRepository repository;

    public AchievementViewModel(@NonNull Application application) {
        super(application);
        repository = new AchievementRepository(application);
    }

    public LiveData<List<Achievement>> getAllAcheivementLiveDataList() {
        return repository.getAllAcheivementLiveDataList();
    }

    public List<Achievement> getAllAchievementList() {
        return repository.getAllAchievementList();
    }

    public Achievement getAchievementByUID(long pk_achievement_uid) {
        return repository.getAchievementByUID(pk_achievement_uid);
    }

    public LiveData<Integer> getLiveDataTotalAchievementsCount() {
        return repository.getLiveDataTotalAchievementsCount();
    }

    public LiveData<Integer> getCompletedAchievementsCount() {
        return repository.getCompletedAchievementsCount();
    }

    public int getTotalAchievementsCount() {
        return repository.getTotalAchievementsCount();
    }

    public void insertAchievement(Achievement achievement) {
        repository.insertAchievement(achievement);
    }

    public void updateAchievement(Achievement achievement) {
        repository.updateAchievement(achievement);
    }

    public void deleteAchievement(Achievement achievement) {
        repository.deleteAchievement(achievement);
    }
}
