package com.prototype.sprout.database.habit.sub_routine;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Routine {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "routine")
    private String routine;

    @ColumnInfo(name = "desc")
    private String desc;

    @ColumnInfo(name = "streak")
    private String streak;

    @ColumnInfo(name = "total")
    private String total;

    @ColumnInfo(name = "skips")
    private String skips;

    @ColumnInfo(name = "alarm")
    private int alarm;

    @Ignore
    public Routine() {

    }

    public Routine(String routine, String desc, String streak, String total, String skips, int alarm) {
        this.routine = routine;
        this.desc = desc;
        this.streak = streak;
        this.total = total;
        this.skips = skips;
        this.alarm = alarm;
    }

    public String getRoutine() {
        return routine;
    }

    public void setRoutine(String routine) {
        this.routine = routine;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStreak() {
        return streak;
    }

    public void setStreak(String streak) {
        this.streak = streak;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSkips() {
        return skips;
    }

    public void setSkips(String skips) {
        this.skips = skips;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }
}
