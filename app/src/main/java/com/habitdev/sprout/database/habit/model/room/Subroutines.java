package com.habitdev.sprout.database.habit.model.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(foreignKeys = @ForeignKey(
                entity = Habits.class,
                parentColumns = "pk_habit_uid",
                childColumns = "fk_habit_uid",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        ))
public class Subroutines implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk_subroutine_uid")
    private long pk_subroutine_uid;

    @ColumnInfo(name = "fk_habit_uid", index = true)
    private long fk_habit_uid;

    @ColumnInfo(name = "routine")
    private String subroutine;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "color")
    private String color;

    @ColumnInfo(name = "isModifiable")
    private Boolean isModifiable;

    @ColumnInfo(name = "isMarkedDone")
    private boolean isMarkDone;

    @ColumnInfo(name = "total_completed")
    private int total_completed;

    @ColumnInfo(name = "max_streak")
    private int max_streak;

    @ColumnInfo(name = "longest_streak")
    private int longest_streak;

    @ColumnInfo(name = "skips")
    private int total_skips;

    @ColumnInfo(name = "upvote")
    private int upvote;

    @ColumnInfo(name = "downvote")
    private int downvote;

    @Ignore
    public Subroutines(String subroutine, String description, String color, boolean isModifiable) {
        this.subroutine = subroutine;
        this.description = description;
        this.color = color;
        this.isModifiable = isModifiable;
        this.isMarkDone = false;
        this.total_completed = 0;
        this.max_streak = 0;
        this.longest_streak = 0;
        this.total_skips = 0;
        this.upvote = 0;
        this.downvote  = 0;
    }

    public Subroutines(long pk_subroutine_uid, long fk_habit_uid, String subroutine, String description, String color, Boolean isModifiable, boolean isMarkDone, int total_completed, int max_streak, int total_skips, int
                       longest_streak) {
        this.pk_subroutine_uid = pk_subroutine_uid;
        this.fk_habit_uid = fk_habit_uid;
        this.subroutine = subroutine;
        this.description = description;
        this.color = color;
        this.isModifiable = isModifiable;
        this.isMarkDone = isMarkDone;
        this.total_completed = total_completed;
        this.max_streak = max_streak;
        this.longest_streak = longest_streak;
        this.total_skips = total_skips;
        this.upvote = 0;
        this.downvote  = 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "Subroutines{" +
                "pk_subroutine_uid=" + pk_subroutine_uid +
                ", fk_habit_uid=" + fk_habit_uid +
                ", subroutine='" + subroutine + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", isModifiable=" + isModifiable +
                ", isMarkDone=" + isMarkDone +
                ", total_completed=" + total_completed +
                ", max_streak=" + max_streak +
                ", longest_streak=" + longest_streak +
                ", total_skips=" + total_skips +
                ", upvote=" + upvote +
                ", downvote=" + downvote +
                '}';
    }

    public long getPk_subroutine_uid() {
        return pk_subroutine_uid;
    }

    public void setPk_subroutine_uid(long pk_subroutine_uid) {
        this.pk_subroutine_uid = pk_subroutine_uid;
    }

    public long getFk_habit_uid() {
        return fk_habit_uid;
    }

    public void setFk_habit_uid(long fk_habit_uid) {
        this.fk_habit_uid = fk_habit_uid;
    }

    public String getSubroutine() {
        return subroutine;
    }

    public void setSubroutine(String subroutine) {
        this.subroutine = subroutine;
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

    public Boolean getModifiable() {
        return isModifiable;
    }

    public void setModifiable(Boolean modifiable) {
        isModifiable = modifiable;
    }

    public boolean isMarkDone() {
        return isMarkDone;
    }

    public void setMarkDone(boolean markDone) {
        isMarkDone = markDone;
    }

    public int getTotal_completed() {
        return total_completed;
    }

    public void setTotal_completed(int total_completed) {
        this.total_completed = total_completed;
    }

    public int getMax_streak() {
        return max_streak;
    }

    public void setMax_streak(int max_streak) {
        this.max_streak = max_streak;
    }

    public int getLongest_streak() {
        return longest_streak;
    }

    public void setLongest_streak(int longest_streak) {
        this.longest_streak = longest_streak;
    }

    public int getTotal_skips() {
        return total_skips;
    }

    public void setTotal_skips(int total_skips) {
        this.total_skips = total_skips;
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
}

