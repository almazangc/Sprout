package com.prototype.sprout.database.habit;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.prototype.sprout.database.Converter;

import java.util.ArrayList;

@Entity
public class Habit {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "habits")
    private String habits;

    @ColumnInfo(name = "subRoutineUID")
    @TypeConverters(Converter.class)
    private ArrayList<Integer> subRoutineUID;

    public Habit(String habits, ArrayList<Integer> subRoutineUID) {
        this.habits = habits;
        this.subRoutineUID = subRoutineUID;
    }

    @Ignore
    public Habit() {

    }

    @Override
    public String toString() {
        return "Habit{" +
                "uid=" + uid +
                ", habits='" + habits + '\'' +
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

    public ArrayList<Integer> getSubRoutineUID() {
        return subRoutineUID;
    }

    public void setSubRoutineUID(ArrayList<Integer> subRoutineUID) {
        this.subRoutineUID = subRoutineUID;
    }
}
