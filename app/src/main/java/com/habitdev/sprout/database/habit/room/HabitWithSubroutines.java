package com.habitdev.sprout.database.habit.room;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.model.room.Subroutines;

import java.io.Serializable;
import java.util.List;

@Entity
public class HabitWithSubroutines implements Serializable{
    @Embedded
    public Habits habit;

    @Relation(parentColumn = "pk_habit_uid", entityColumn = "pk_subroutine_uid")
    public List<Subroutines> subroutines;

    public HabitWithSubroutines(Habits habit, List<Subroutines> subroutines) {
        this.habit = habit;
        this.subroutines = subroutines;
    }

    @NonNull
    @Override
    public String toString() {
        return "HabitWithSubroutines{" +
                "habit=" + habit +
                ", subroutines=" + subroutines +
                '}';
    }

    public Habits getHabit() {
        return habit;
    }

    public void setHabit(Habits habit) {
        this.habit = habit;
    }

    public List<Subroutines> getSubroutines() {
        return subroutines;
    }

    public void setSubroutines(List<Subroutines> subroutines) {
        this.subroutines = subroutines;
    }
}
