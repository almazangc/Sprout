package com.habitdev.sprout.database.milestone.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Milestone {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_milestone_uid")
    private long pk_milestone_uid;

    private String name;
    private String description;
    private long date_completed;
    private boolean completed;

    public Milestone() {}

    @Ignore
    public Milestone(long pk_milestone_uid, String name, String description, long date_completed, boolean completed) {
        this.pk_milestone_uid = pk_milestone_uid;
        this.name = name;
        this.description = description;
        this.date_completed = date_completed;
        this.completed = completed;
    }

    public long getPk_milestone_uid() {
        return pk_milestone_uid;
    }

    public void setPk_milestone_uid(long pk_milestone_uid) {
        this.pk_milestone_uid = pk_milestone_uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate_completed() {
        return date_completed;
    }

    public void setDate_completed(long date_completed) {
        this.date_completed = date_completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @NonNull
    @Override
    public String toString() {
        return "Milestone{" +
                "pk_milestone_uid=" + pk_milestone_uid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date_completed=" + date_completed +
                ", completed=" + completed +
                '}';
    }
}
