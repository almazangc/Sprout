package com.habitdev.sprout.ui.menu.setting.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.achievement.model.Achievement;
import com.habitdev.sprout.utill.AchievementDiffUtil;

import java.util.List;

public class SettingAchievementParentItemAdapter extends RecyclerView.Adapter<SettingAchievementParentItemAdapter.AchievementViewHolder> {

    List<Achievement> oldAchievementList;

    public SettingAchievementParentItemAdapter() {
    }

    public void setOldAchievementList(List<Achievement> oldAchievementList) {
        this.oldAchievementList = oldAchievementList;
    }

    @NonNull
    @Override
    public SettingAchievementParentItemAdapter.AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AchievementViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_setting_achievements_parent_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        holder.bindAchievement(oldAchievementList.get(position));
    }

    @Override
    public int getItemCount() {
        return oldAchievementList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setNewAchievementList(List<Achievement> newAchievementList) {
        DiffUtil.Callback DIFF_CALLBACK = new AchievementDiffUtil(oldAchievementList, newAchievementList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        oldAchievementList.clear();
        oldAchievementList.addAll(newAchievementList);
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class AchievementViewHolder extends RecyclerView.ViewHolder {

        final LinearLayout achievementProgressIndicatorContainer;
        final RelativeLayout achievementDateCompletedContainer;
        final CircularProgressIndicator achievementProgressIndicator;
        final TextView achievementCurrentProgress, achievementGoalProgress, achievementTitle, achievementDescription, achievementDateCompletedlbl, achievementDateCompleted;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);

            achievementProgressIndicatorContainer = itemView.findViewById(R.id.achievement_progress_indicator_container);
            achievementProgressIndicator = itemView.findViewById(R.id.achievement_progress_indicator);
            achievementCurrentProgress = itemView.findViewById(R.id.achievement_current_progress);
            achievementGoalProgress = itemView.findViewById(R.id.achievement_goal_progress);
            achievementTitle = itemView.findViewById(R.id.achievement_title);
            achievementDescription = itemView.findViewById(R.id.achievement_description);
            achievementDateCompletedContainer = itemView.findViewById(R.id.achievement_general_date_completed_container);
            achievementDateCompletedlbl = itemView.findViewById(R.id.achievement_date_completeted_lbl);
            achievementDateCompleted = itemView.findViewById(R.id.achievement_date_completeted);
        }

        void bindAchievement(Achievement achievement) {
            if (achievement.getType() == 1) {
                achievementProgressIndicator.setMax((int) achievement.getGoal_progress());
                achievementProgressIndicator.setProgress((int) achievement.getCurrent_progress(), true);
                achievementCurrentProgress.setText(String.valueOf(achievement.getCurrent_progress()));
                achievementGoalProgress.setText(String.valueOf(achievement.getGoal_progress()));
            } else {
                achievementProgressIndicatorContainer.setVisibility(View.GONE);
            }

            achievementTitle.setText(achievement.getTitle());
            achievementDescription.setText(achievement.getDescription());

            if (achievement.is_completed()) {
                achievementDateCompletedlbl.setVisibility(View.VISIBLE);
                achievementDateCompleted.setVisibility(View.VISIBLE);
                achievementDateCompleted.setText(achievement.getDate_achieved());
            } else {
                achievementDateCompletedlbl.setVisibility(View.GONE);
                achievementDateCompleted.setVisibility(View.GONE);
            }
        }
    }
}
