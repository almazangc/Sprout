package com.habitdev.sprout.database.habit.model.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity()
public class Habits implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_habit_uid")
    private long pk_habit_uid;

    @ColumnInfo(name = "habit")
    private String habit;

    @ColumnInfo(name = "desc")
    private String description;

    @ColumnInfo(name = "color")
    private String color;

    @ColumnInfo(name = "on_reform")
    private boolean onReform;

    @ColumnInfo(name = "modifiable")
    private final boolean modifiable;

    @ColumnInfo(name = "relapse")
    private int relapse;

    @ColumnInfo(name = "date_started")
    private String date_started;

    @ColumnInfo(name = "total_subroutine")
    private int total_subroutine;

    @ColumnInfo(name = "completed_subroutines")
    private int completed_subroutine;

    @ColumnInfo(name = "upvote")
    private int upvote;

    @ColumnInfo(name = "downvote")
    private int downvote;

    @ColumnInfo(name = "vote_status")
    private int vote_status;

    @Ignore
    public Habits(String habit, String description, String color, Boolean onReform, Boolean modifiable) {
        this.habit = habit;
        this.description = description;
        this.color = color;
        this.onReform = onReform;
        this.modifiable = modifiable;
        this.relapse = 0;
        this.date_started = null;
        this.total_subroutine = 0;
        this.completed_subroutine = 0;
        this.upvote = 0;
        this.downvote = 0;
        this.vote_status = 0;
    }

    public Habits(long pk_habit_uid, String habit, String description, String color, boolean onReform, boolean modifiable, int relapse, String date_started, int total_subroutine, int completed_subroutine, int upvote, int downvote, int vote_status) {
        this.pk_habit_uid = pk_habit_uid;
        this.habit = habit;
        this.description = description;
        this.color = color;
        this.onReform = onReform;
        this.modifiable = modifiable;
        this.relapse = relapse;
        this.date_started = date_started;
        this.total_subroutine = total_subroutine;
        this.completed_subroutine = completed_subroutine;
        this.upvote = upvote;
        this.downvote = downvote;
        this.vote_status = vote_status;
    }

    @Ignore
    public Habits(long pk_habit_uid, String habit, String description, String color, boolean onReform, boolean modifiable, int relapse, String date_started, int total_subroutine, int completed_subroutine) {
        this.pk_habit_uid = pk_habit_uid;
        this.habit = habit;
        this.description = description;
        this.color = color;
        this.onReform = onReform;
        this.modifiable = modifiable;
        this.relapse = relapse;
        this.date_started = date_started;
        this.total_subroutine = total_subroutine;
        this.completed_subroutine = completed_subroutine;
        this.upvote = 0;
        this.downvote = 0;
        this.vote_status = 0;
    }

    @Ignore
    public Habits(long pk_habit_uid, String habit, String description, String color, boolean onReform, boolean modifiable, String date_started, int total_subroutine) {
        this.pk_habit_uid = pk_habit_uid;
        this.habit = habit;
        this.description = description;
        this.color = color;
        this.onReform = onReform;
        this.modifiable = modifiable;
        this.relapse = 0;
        this.date_started = date_started;
        this.total_subroutine = total_subroutine;
        this.completed_subroutine = 0;
        this.upvote = 0;
        this.downvote = 0;
        this.vote_status = 0;
    }


    /**
     * Create new Habit obj so it wont hold reference from the passed originalHabit
     * @param originalHabit the reference obj to create a copy
     */
    @Ignore
    public Habits(Habits originalHabit) {
        this.pk_habit_uid = originalHabit.pk_habit_uid;
        this.habit = originalHabit.habit;
        this.description = originalHabit.description;
        this.color = originalHabit.color;
        this.onReform = originalHabit.onReform;
        this.modifiable = originalHabit.modifiable;
        this.relapse = originalHabit.relapse;
        this.date_started = originalHabit.date_started;
        this.total_subroutine = originalHabit.total_subroutine;
        this.completed_subroutine = originalHabit.completed_subroutine;
        this.upvote = originalHabit.upvote;
        this.downvote = originalHabit.downvote;
        this.vote_status = originalHabit.vote_status;
    }

    @NonNull
    @Override
    public String toString() {
        return "Habits{" +
                "pk_habit_uid=" + pk_habit_uid +
                ", habit='" + habit + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", onReform=" + onReform +
                ", modifiable=" + modifiable +
                ", relapse=" + relapse +
                ", date_started='" + date_started + '\'' +
                ", total_subroutine=" + total_subroutine +
                ", completed_subroutine=" + completed_subroutine +
                ", upvote=" + upvote +
                ", downvote=" + downvote +
                ", vote_status=" + vote_status +
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public void setCompleted_subroutine(int completed_subroutine) {
        this.completed_subroutine = completed_subroutine;
    }

    public int getCompleted_subroutine() {
        return completed_subroutine;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }

    public int getVote_status() {
        return vote_status;
    }

    public void setVote_status(int vote_status) {
        this.vote_status = vote_status;
    }
}

