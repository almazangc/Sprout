package com.habitdev.sprout.database.habit.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Habits")
public class Habits implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_habit_uid")
    private long pk_habit_uid;

    @ColumnInfo(name = "habit")
    private String habit;

    @ColumnInfo(name = "desc")
    private String description;

    @ColumnInfo(name = "on_reform")
    private boolean onReform;

    @ColumnInfo(name = "modifiable")
    private boolean modifiable;

    @ColumnInfo(name = "abstinence")
    private int abstinence;

    @ColumnInfo(name = "relapse")
    private int relapse;

    @ColumnInfo(name = "date_started")
    private String date_started;

    @ColumnInfo(name = "total_subroutine")
    private int total_subroutine;

    public Habits(String habit, String description) {
        this.habit = habit;
        this.description = description;
        this.onReform = false;
        this.modifiable = false;
        this.abstinence = 0;
        this.relapse = 0;
        this.date_started = null;
        this.total_subroutine = 0;
    }

    @Ignore
    public Habits(long pk_habit_uid, String habit, String description, boolean onReform, boolean modifiable, int abstinence, int relapse, String date_started, int total_subroutine) {
        this.pk_habit_uid = pk_habit_uid;
        this.habit = habit;
        this.description = description;
        this.onReform = onReform;
        this.modifiable = modifiable;
        this.abstinence = abstinence;
        this.relapse = relapse;
        this.date_started = date_started;
        this.total_subroutine = total_subroutine;
    }

    @NonNull
    @Override
    public String toString() {
        return "Habits{" +
                "pk_habit_uid=" + pk_habit_uid +
                ", habit='" + habit + '\'' +
                ", description='" + description + '\'' +
                ", onReform=" + onReform +
                ", modifiable=" + modifiable +
                ", abstinence=" + abstinence +
                ", relapse=" + relapse +
                ", date_started='" + date_started + '\'' +
                ", total_subroutine=" + total_subroutine +
                '}';
    }

    public long getPk_habit_uid() {
        return pk_habit_uid;
    }

    public void setPk_habit_uid(long pk_habit_uid) {
        this.pk_habit_uid = pk_habit_uid;
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

    public boolean isOnReform() {
        return onReform;
    }

    public void setOnReform(boolean onReform) {
        this.onReform = onReform;
    }

    public boolean isModifiable() {
        return modifiable;
    }

    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
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

