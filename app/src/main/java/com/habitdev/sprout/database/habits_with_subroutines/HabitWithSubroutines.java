package com.habitdev.sprout.database.habits_with_subroutines;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.List;

@Entity()
public class HabitWithSubroutines {
    @Embedded
    public Habits habit;

    @Relation(parentColumn = "pk_habit_uid", entityColumn = "pk_subroutine_uid")
    public List<Subroutines> subroutines;

    public HabitWithSubroutines(Habits habit, List<Subroutines> subroutines) {
        this.habit = habit;
        this.subroutines = subroutines;
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
