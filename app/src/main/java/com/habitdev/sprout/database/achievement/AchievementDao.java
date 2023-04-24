package com.habitdev.sprout.database.achievement;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.habitdev.sprout.database.achievement.model.Achievement;

import java.util.List;

@Dao
public interface AchievementDao {

    @Query("SELECT * FROM Achievement")
    LiveData<List<Achievement>> getAllAchievementLiveDataList();

    @Query("SELECT * FROM Achievement")
    List<Achievement> getAllAchievementList();

    @Query("SELECT * FROM Achievement WHERE pk_achievement_uid = :pk_achievement_uid")
    Achievement getAchievementByUID(long pk_achievement_uid);

    @Query("SELECT COUNT(*) FROM achievement WHERE is_completed = 1")
    LiveData<Integer> getCompletedAchievementsCount();

    @Query("SELECT COUNT(*) FROM achievement")
    LiveData<Integer> getLiveDataTotalAchievementsCount();

    @Query("SELECT COUNT(*) FROM achievement")
    int getTotalAchievementsCount();

    @Query("SELECT pk_achievement_uid FROM Achievement WHERE title = :achievementTitle")
    long getAchievementByTitle(String achievementTitle);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAchievement(Achievement achievement);

    @Update
    void updateAchievement(Achievement achievement);

    @Delete
    void deleteAchievement(Achievement achievement);
}
