package com.habitdev.sprout.utill;

import androidx.recyclerview.widget.DiffUtil;

import com.habitdev.sprout.database.habit.model.room.Habits;

import java.util.List;

public class HabitDiffUtil extends DiffUtil.Callback {

    private final List<Habits> oldHabitsList;
    private final List<Habits> newHabitsList;

    public HabitDiffUtil(List<Habits> oldHabitsList, List<Habits> newHabitsList) {
        this.oldHabitsList = oldHabitsList;
        this.newHabitsList = newHabitsList;
    }

    @Override
    public int getOldListSize() {
        return oldHabitsList.size();
    }

    @Override
    public int getNewListSize() {
        return newHabitsList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldHabitsList.get(oldItemPosition).getPk_habit_uid() == newHabitsList.get(newItemPosition).getPk_habit_uid();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Habits oldHabit = oldHabitsList.get(oldItemPosition);
        Habits newHabit = newHabitsList.get(newItemPosition);
        return oldHabit.getHabit().equals(newHabit.getHabit()) &&
                oldHabit.getDescription().equals(newHabit.getDescription()) &&
                oldHabit.getColor().equals(newHabit.getColor());
    }
}
