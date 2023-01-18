package com.habitdev.sprout.utill;

import androidx.recyclerview.widget.DiffUtil;

import com.habitdev.sprout.database.habit.model.room.Subroutines;

import java.util.List;

public class SubroutineDiffUtil extends DiffUtil.Callback {

    private final List<Subroutines> oldSubroutineList;
    private final List<Subroutines> newSubroutineList;

    public SubroutineDiffUtil(List<Subroutines> oldSubroutineList, List<Subroutines> newSubroutineList) {
        this.oldSubroutineList = oldSubroutineList;
        this.newSubroutineList = newSubroutineList;
    }

    @Override
    public int getOldListSize() {
        return oldSubroutineList == null ? 0 : oldSubroutineList.size();
    }

    @Override
    public int getNewListSize() {
        return newSubroutineList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldSubroutineList.get(oldItemPosition).getPk_subroutine_uid() == newSubroutineList.get(newItemPosition).getPk_subroutine_uid();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Subroutines oldSubroutine = oldSubroutineList.get(oldItemPosition);
        Subroutines newSubroutine = newSubroutineList.get(newItemPosition);
        return oldSubroutine.getSubroutine().equals(newSubroutine.getSubroutine()) &&
                oldSubroutine.getDescription().equals(newSubroutine.getDescription()) &&
                oldSubroutine.getColor().equals(newSubroutine.getColor());
    }
}
