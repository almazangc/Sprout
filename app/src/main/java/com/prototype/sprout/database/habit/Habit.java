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
    private String dateStarted;

    @ColumnInfo(name = "completed_subroutine")
    private int completedSubroutine;

    @ColumnInfo(name = "subRoutineUID")
    @TypeConverters(ArrayListConverter.class)
    private ArrayList<Integer> subRoutineUID;

    @Ignore
    public Habit() {

    }

    @Ignore
    public Habit(String habit, String description, ArrayList<Integer> subRoutineUID) {
        this.habit = habit;
        this.description = description;
        this.subRoutineUID = subRoutineUID;
    }

    public Habit(boolean onReform, String habit, String description, ArrayList<Integer> subRoutineUID) {
        this.onReform = onReform;
        this.habit = habit;
        this.description = description;
        this.subRoutineUID = subRoutineUID;
    }

    @Ignore
    public Habit(boolean onReform, String habit, String description, int abstinence, int relapse, String dateStarted, int completedSubroutine, ArrayList<Integer> subRoutineUID) {
        this.onReform = onReform;
        this.habit = habit;
        this.description = description;
        this.abstinence = abstinence;
        this.relapse = relapse;
        this.dateStarted = dateStarted;
        this.completedSubroutine = completedSubroutine;
        this.subRoutineUID = subRoutineUID;
    }

    @Override
    public String toString() {
        return "onReform: " + onReform + ",\thabit: " + habit + "}\n";
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public int getCompletedSubroutine() {
        return completedSubroutine;
    }

    public void setCompletedSubroutine(int completedSubroutine) {
        this.completedSubroutine = completedSubroutine;
    }

    public ArrayList<Integer> getSubRoutineUID() {
        return subRoutineUID;
    }

    public void setSubRoutineUID(ArrayList<Integer> subRoutineUID) {
        this.subRoutineUID = subRoutineUID;
    }
}
