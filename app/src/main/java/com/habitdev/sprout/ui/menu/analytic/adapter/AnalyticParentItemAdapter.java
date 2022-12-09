package com.habitdev.sprout.ui.menu.analytic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.utill.HabitDiffUtil;

import java.util.List;

public class AnalyticParentItemAdapter extends RecyclerView.Adapter<AnalyticParentItemAdapter.AnalyticParentViewHolder> {

    private List<Habits> oldHabitsList;

    public AnalyticParentItemAdapter() {
    }

    public void setOldHabitsList(List<Habits> oldHabitsList) {
        this.oldHabitsList = oldHabitsList;
    }

    @NonNull
    @Override
    public AnalyticParentItemAdapter.AnalyticParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnalyticParentViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_analytic_parent_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyticParentItemAdapter.AnalyticParentViewHolder holder, int position) {
        holder.bindHabit(oldHabitsList.get(position));
    }

    @Override
    public int getItemCount() {
        return oldHabitsList.size();
    }

    public void setNewHabitList(List<Habits> newHabitList) {
        DiffUtil.Callback DIFF_CALLBACK = new HabitDiffUtil(oldHabitsList, newHabitList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        this.oldHabitsList = newHabitList;
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class AnalyticParentViewHolder extends RecyclerView.ViewHolder {


        public AnalyticParentViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bindHabit(Habits habit) {

        }
    }
}
