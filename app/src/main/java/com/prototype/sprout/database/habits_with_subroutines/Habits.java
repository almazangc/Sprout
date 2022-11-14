package com.prototype.sprout.database.habits_with_subroutines;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Habits")
public class Habits {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_habit_uid")
    private long pk_habit_uid;

    @ColumnInfo(name = "on_reform")
    private boolean onReform;

    @ColumnInfo(name = "habit")
    private String habit;

    @ColumnInfo(name = "desc")
    private String description;

    @ColumnInfo(name = "abstinence")
    private int abstinence;

    @ColumnInfo(name = "relapse")
    private int relapse;

    @ColumnInfo(name = "date_started")
    private String date_started;

    @ColumnInfo(name = "total_subroutine")
    private int total_subroutine;

    public Habits(String habit, String description) {
        this.onReform = false;
        this.habit = habit;
        this.description = description;
        this.abstinence = 0;
        this.relapse = 0;
        this.date_started = "todo:fix";
        this.total_subroutine = 0;
    }

    public long getPk_habit_uid() {
        return pk_habit_uid;
    }

    public void setPk_habit_uid(long pk_habit_uid) {
        this.pk_habit_uid = pk_habit_uid;
    }

    public boolean isOnReform() {
        return onReform;
    }

    public void setOnReform(boolean onReform) {
        this.onReform = onReform;
    }

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAbstinence() {
        return abstinence;
    }

    public void setAbstinence(int abstinence) {
        this.abstinence = abstinence;
    }

    public int getRelapse() {
        return relapse;
    }

    public void setRelapse(int relapse) {
        this.relapse = relapse;
    }

    public String getDate_started() {
        return date_started;
    }

    public void setDate_started(String date_started) {
        this.date_started = date_started;
    }

    public int getTotal_subroutine() {
        return total_subroutine;
    }

    public void setTotal_subroutine(int total_subroutine) {
        this.total_subroutine = total_subroutine;
    }
}

