package com.habitdev.sprout.ui.habit_self_assessment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.ui.habit_self_assessment.adapter.Model.Result;
import com.habitdev.sprout.utill.diffutils.HabitDiffUtil;

import java.text.DecimalFormat;
import java.util.List;

public class AssessmentRecordChildItemAdapter extends RecyclerView.Adapter<AssessmentRecordChildItemAdapter.AssessmentRecordViewHolder> {

    private List<Habits> oldHabitList;
    private static List<Result> habitScoreResult;

    public void setOldHabitList(List<Habits> oldHabitList) {
        this.oldHabitList = oldHabitList;
    }

    public void setHabitScoreResult(List<Result> habitScoreResult) {
        AssessmentRecordChildItemAdapter.habitScoreResult = habitScoreResult;
    }

    public interface OnAddHabitOnReform {
        void onClickReform(Habits habit);
    }

    private OnAddHabitOnReform mOnAddHabitOnReform;

    public void setmOnAddHabitOnReform(OnAddHabitOnReform mOnAddHabitOnReform) {
        this.mOnAddHabitOnReform = mOnAddHabitOnReform;
    }

    @NonNull
    @Override
    public AssessmentRecordChildItemAdapter.AssessmentRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssessmentRecordChildItemAdapter.AssessmentRecordViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_setting_profile_assessment_record_child_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentRecordChildItemAdapter.AssessmentRecordViewHolder holder, int position) {
        holder.bind(oldHabitList.get(position), mOnAddHabitOnReform, habitScoreResult.get(position));
    }

    @Override
    public int getItemCount() {
        return oldHabitList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setNewHabitList(List<Habits> newHabitList) {
        DiffUtil.Callback DIFF_CALLBACK = new HabitDiffUtil(oldHabitList, newHabitList);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        this.oldHabitList.clear();
        this.oldHabitList.addAll(newHabitList);
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class AssessmentRecordViewHolder extends RecyclerView.ViewHolder {

        TextView habitScore, habitTitle;
        Button addHabitOnReform;

        public AssessmentRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            habitScore = itemView.findViewById(R.id.profile_assessment_record_habit_score);
            habitTitle = itemView.findViewById(R.id.profile_assessment_record_habit_title);
            addHabitOnReform = itemView.findViewById(R.id.profile_reform_habit_record);
        }

        void bind(Habits habit, OnAddHabitOnReform mOnAddHabitOnReform, Result habitScoreResult) {

            Double score = habitScoreResult.getScore();
            DecimalFormat decimalFormat = new DecimalFormat("##%");
            String formattedScore = decimalFormat.format(score);

            habitScore.setText(formattedScore);
            habitTitle.setText(habit.getHabit());

            addHabitOnReform.setOnClickListener(view -> {
                if (mOnAddHabitOnReform != null) {
                    mOnAddHabitOnReform.onClickReform(habit);
                }
            });

            if (score == 1) {
                addHabitOnReform.setText("Highly Recommended");
                habitScore.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.RUSTIC_RED));
            } else if (score > .75) {
                addHabitOnReform.setText("Recommended");
                habitScore.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.CORAL_RED));
            } else if (score > .5) {
                habitScore.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.WISTERIA));
            } else if (score > .25) {
                addHabitOnReform.setVisibility(View.GONE);
                habitScore.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.PETER_RIVER));
            } else {
                addHabitOnReform.setVisibility(View.GONE);
                habitScore.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.EMERALD));
            }
        }
    }
}
