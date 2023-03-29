package com.habitdev.sprout.database.achievement.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Achievement {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_achievement_uid")
    private long pk_achievement_uid;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    //quantity 0, quality 1
    /**
     * 0 - Quantity
     * 1 - Quality
     */
    @ColumnInfo(name = "type")
    private long type;

    @ColumnInfo(name = "current_progress")
    private long current_progress;

    @ColumnInfo(name = "goal_progress")
    private long goal_progress;

    @ColumnInfo(name = "date_achieved")
    private String date_achieved;

    @ColumnInfo(name = "is_completed")
    private boolean is_completed;

    public Achievement() {}

    @Ignore
    public Achievement(String title, String description, long type, long goal_progress) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.current_progress = 0;
        this.goal_progress = goal_progress;
        this.date_achieved = null;
        this.is_completed = false;
    }

    @Ignore
    public Achievement(String title, String description, long type, long current_progress, long goal_progress, String date_achieved, boolean is_completed) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.current_progress = current_progress;
        this.goal_progress = goal_progress;
        this.date_achieved = date_achieved;
        this.is_completed = is_completed;
    }

    public long getPk_achievement_uid() {
        return pk_achievement_uid;
    }

    public void setPk_achievement_uid(long pk_achievement_uid) {
        this.pk_achievement_uid = pk_achievement_uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getCurrent_progress() {
        return current_progress;
    }

    public void setCurrent_progress(long current_progress) {
        this.current_progress = current_progress;
    }

    public long getGoal_progress() {
        return goal_progress;
    }

    public void setGoal_progress(long goal_progress) {
        this.goal_progress = goal_progress;
    }

    public String getDate_achieved() {
        return date_achieved;
    }

    public void setDate_achieved(String date_achieved) {
        this.date_achieved = date_achieved;
    }

    public boolean isIs_completed() {
        return is_completed;
    }

    public void setIs_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }

    @NonNull
    @Override
    public String toString() {
        return "Achievement{" +
                "pk_milestone_uid=" + pk_achievement_uid +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", current_progress=" + current_progress +
                ", goal_progress=" + goal_progress +
                ", date_completed=" + date_achieved +
                ", is_completed=" + is_completed +
                '}';
    }
}
