package com.habitdev.sprout.ui.habit_assessment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.ui.habit_assessment.adapter.Model.Result;

import java.util.List;

public class AnalysisParentItemDropDownAdapter extends ArrayAdapter<Result> {

    List<Result> results;
    HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;

    public void setHabitWithSubroutinesViewModel(HabitWithSubroutinesViewModel habitWithSubroutinesViewModel) {
        this.habitWithSubroutinesViewModel = habitWithSubroutinesViewModel;
    }

    public AnalysisParentItemDropDownAdapter(@NonNull Context context, @NonNull List<Result> results) {
        super(context, 0, results);
        this.results = results;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.adapter_analysis_parent_habit_item, parent, false);
        }

        Result result = results.get(position);
        TextView percentage = itemView.findViewById(R.id.analysis_suggestion_percent_drop_item);
        TextView habit = itemView.findViewById(R.id.analysis_habit_title_drop_item);

        if (result != null) {
            if (percentage.getVisibility() == View.GONE) {
                percentage.setVisibility(View.VISIBLE);
            }
            percentage.setText(result.getFormattedConfidenceScore());
            String habit_title = habitWithSubroutinesViewModel.getHabitByUID(result.getHabit_uid()).getHabit();
            habit.setText(habit_title);
        } else {
            percentage.setVisibility(View.GONE);
        }
        return itemView;
    }

    @Override
    public int getCount() {
        return results.size();
    }
}
