package com.prototype.sprout.database.sub_routine;

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
    private int streak;

    @ColumnInfo(name = "total")
    private int total;

    @ColumnInfo(name = "skips")
    private int skips;

    @ColumnInfo(name = "alarm")
    private int alarm;

    @Ignore
    public Routine() {

    }

    @Ignore
    public Routine(String routine, String desc) {
        this.routine = routine;
        this.desc = desc;
    }

    public Routine(String routine, String desc, int streak, int total, int skips, int alarm) {
        this.routine = routine;
        this.desc = desc;
        this.streak = streak;
        this.total = total;
        this.skips = skips;
        this.alarm = alarm;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSkips() {
        return skips;
    }

    public void setSkips(int skips) {
        this.skips = skips;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }
}
