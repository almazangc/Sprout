package com.habitdev.sprout.utill.diffutils;

import androidx.recyclerview.widget.DiffUtil;

import com.habitdev.sprout.database.achievement.model.Achievement;

import java.util.List;

public class AchievementDiffUtil extends DiffUtil.Callback {

    private final List<Achievement> oldAchievementList;
    private final List<Achievement> newAchievementList;

    public AchievementDiffUtil(List<Achievement> oldAchievementList, List<Achievement> newAchievementList) {
        this.oldAchievementList = oldAchievementList;
        this.newAchievementList = newAchievementList;
    }

    @Override
    public int getOldListSize() {
        return oldAchievementList == null ? 0 : oldAchievementList.size();
    }

    @Override
    public int getNewListSize() {
        return newAchievementList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldAchievementList.get(oldItemPosition).getPk_achievement_uid() == newAchievementList.get(newItemPosition).getPk_achievement_uid();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Achievement oldAchievement = oldAchievementList.get(oldItemPosition);
        Achievement newAchievement = newAchievementList.get(newItemPosition);
        return oldAchievement.getTitle().equals(newAchievement.getTitle()) &&
                oldAchievement.getDescription().equals(newAchievement.getDescription()) &&
                oldAchievement.getCurrent_progress() == newAchievement.getCurrent_progress() &&
                (oldAchievement.getDate_achieved() != null && (newAchievement.getDate_achieved() != null && (oldAchievement.getDate_achieved().equals(newAchievement.getDate_achieved())))) &&
                oldAchievement.getDate_achieved().equals(newAchievement.getDate_achieved()) &&
                oldAchievement.is_completed() == newAchievement.is_completed();
    }
}
