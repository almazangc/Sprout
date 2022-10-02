package com.prototype.sprout.database.habit;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.prototype.sprout.database.converters.ArrayListConverter;

import java.util.ArrayList;

@Entity
public class Habit {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "habits")
    private String habits;

    @ColumnInfo(name = "desc")
    private String desc;

    @ColumnInfo(name = "abstinence")
    private int abstinence;

    @ColumnInfo(name = "relapse")
    private int relapse;

    @ColumnInfo(name = "subRoutineUID")
    @TypeConverters(ArrayListConverter.class)
    private ArrayList<Integer> subRoutineUID;

    @Ignore
    public Habit() {

    }

    @Ignore
    public Habit(String habits, String desc, ArrayList<Integer> subRoutineUID) {
        this.habits = habits;
        this.desc = desc;
        this.subRoutineUID = subRoutineUID;
    }

    public Habit(String habits, String desc, int abstinence, int relapse, ArrayList<Integer> subRoutineUID) {
        this.habits = habits;
        this.desc = desc;
        this.abstinence = abstinence;
        this.relapse = relapse;
        this.subRoutineUID = subRoutineUID;
    }

    @Override
    public String toString() {
        return "Habit{" +
                "uid=" + uid +
                ", habits='" + habits + '\'' +
                ", desc='" + desc + '\'' +
                ", abstinence=" + abstinence +
                ", relapse=" + relapse +
                ", subRoutineUID=" + subRoutineUID +
                '}';
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getHabits() {
        return habits;
    }

    public void setHabits(String habits) {
        this.habits = habits;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public ArrayList<Integer> getSubRoutineUID() {
        return subRoutineUID;
    }

    public void setSubRoutineUID(ArrayList<Integer> subRoutineUID) {
        this.subRoutineUID = subRoutineUID;
    }
}
