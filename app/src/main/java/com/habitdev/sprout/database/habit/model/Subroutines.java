package com.habitdev.sprout.database.habit.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
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

    @ColumnInfo(name = "isMarkedDone")
    private boolean is_marked_done;

    @ColumnInfo(name = "streak")
    private int streak;

    @ColumnInfo(name = "total_streak")
    private int total_streak;

    @ColumnInfo(name = "skips")
    private int skips;

    public Subroutines(String subroutine, String description) {
        this.subroutine = subroutine;
        this.description = description;
        this.is_marked_done = false;
        this.streak = 0;
        this.total_streak = 0;
        this.skips = 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "Subroutines{" +
                "pk_subroutine_uid=" + pk_subroutine_uid +
                ", fk_habit_uid=" + fk_habit_uid +
                ", subroutine='" + subroutine + '\'' +
                ", description='" + description + '\'' +
                ", is_marked_done=" + is_marked_done +
                ", streak=" + streak +
                ", total_streak=" + total_streak +
                ", skips=" + skips +
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

    public boolean isIs_marked_done() {
        return is_marked_done;
    }

    public void setIs_marked_done(boolean is_marked_done) {
        this.is_marked_done = is_marked_done;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public int getTotal_streak() {
        return total_streak;
    }

    public void setTotal_streak(int total_streak) {
        this.total_streak = total_streak;
    }

    public int getSkips() {
        return skips;
    }

    public void setSkips(int skips) {
        this.skips = skips;
    }
}

