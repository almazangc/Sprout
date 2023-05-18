package com.habitdev.sprout.ui.habit_self_assessment.adapter;

import android.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.assessment.AssessmentViewModel;
import com.habitdev.sprout.database.assessment.model.AssessmentRecord;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.utill.diffutils.AssessmentRecordDiffUtil;
import com.habitdev.sprout.utill.recommender.KnowledgeBased;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssessmentRecordParentItemAdapter extends RecyclerView.Adapter<AssessmentRecordParentItemAdapter.AssessmentRecordViewHolder> {

    private List<AssessmentRecord> oldAssessmentRecords;
    private List<Habits> predefinedHabitsList;
    private AssessmentViewModel assessmentViewModel;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private LifecycleOwner lifecycleOwner;
    private RelativeLayout bindingView;

    public void setOldAssessmentRecords(List<AssessmentRecord> oldAssessmentRecords) {
        this.oldAssessmentRecords = oldAssessmentRecords;
    }

    public void setPredefinedHabitsList(List<Habits> predefinedHabitsList) {
        this.predefinedHabitsList = predefinedHabitsList;
    }

    public void setAssessmentViewModel(AssessmentViewModel assessmentViewModel) {
        this.assessmentViewModel = assessmentViewModel;
    }

    public void setHabitWithSubroutinesViewModel(HabitWithSubroutinesViewModel habitWithSubroutinesViewModel) {
        this.habitWithSubroutinesViewModel = habitWithSubroutinesViewModel;
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setBindingView(RelativeLayout bindingView) {
        this.bindingView = bindingView;
    }

    public interface OnDropAssessmentRecord {
        void onClickDrop(AssessmentRecord assessmentRecord);
    }

    private OnDropAssessmentRecord mOnDropAssessmentRecord;

    public void setmOnDropAssessmentRecord(OnDropAssessmentRecord mOnDropAssessmentRecord) {
        this.mOnDropAssessmentRecord = mOnDropAssessmentRecord;
    }

    @NonNull
    @Override
    public AssessmentRecordParentItemAdapter.AssessmentRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssessmentRecordParentItemAdapter.AssessmentRecordViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_setting_profile_assessment_record_parent_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentRecordParentItemAdapter.AssessmentRecordViewHolder holder, int position) {
        holder.bindHabit(oldAssessmentRecords.get(position), mOnDropAssessmentRecord, predefinedHabitsList,
                assessmentViewModel, habitWithSubroutinesViewModel, lifecycleOwner, bindingView);
    }


    @Override
    public int getItemCount() {
        return oldAssessmentRecords.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void seNewAssessmentRecords(List<AssessmentRecord> newAssessmentRecords) {
        DiffUtil.Callback DIFF_CALLBACK = new AssessmentRecordDiffUtil(oldAssessmentRecords, newAssessmentRecords);
        DiffUtil.DiffResult DIFF_CALLBACK_RESULT = DiffUtil.calculateDiff(DIFF_CALLBACK);
        oldAssessmentRecords.clear();
        oldAssessmentRecords.addAll(newAssessmentRecords);
        DIFF_CALLBACK_RESULT.dispatchUpdatesTo(this);
    }

    public static class AssessmentRecordViewHolder extends RecyclerView.ViewHolder {

        final LinearLayout dateCompletedLayout, assessmentStatusLayout;
        final View divider;
        final TextView assessmentID, dateTaken, assessmentStatus, dateCompleted, analysisResultLBL;
        final Button dropAssessmentRecord;
        final RecyclerView childRV;
        final AssessmentRecordChildItemAdapter assessmentRecordChildItemAdapter;

        public AssessmentRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            dateCompletedLayout = itemView.findViewById(R.id.profile_assessment_date_completed);
            assessmentStatusLayout = itemView.findViewById(R.id.profile_assessment_status);
            divider = itemView.findViewById(R.id.profile_assessment_record_divider);

            assessmentID = itemView.findViewById(R.id.profile_assessment_record_no_lbl);
            dateTaken = itemView.findViewById(R.id.profile_assessment_record_date_taken_lbl);
            assessmentStatus = itemView.findViewById(R.id.profile_assessment_status_lbl);
            dateCompleted = itemView.findViewById(R.id.profile_assessment_date_completed_lbl);
            analysisResultLBL = itemView.findViewById(R.id.profile_assessment_record_habit_score_lbl);

            dropAssessmentRecord = itemView.findViewById(R.id.profile_drop_assessment_record);

            childRV = itemView.findViewById(R.id.profile_assessment_record_result_recycler_view);

            assessmentRecordChildItemAdapter = new AssessmentRecordChildItemAdapter();
        }

        void bindHabit(AssessmentRecord assessmentRecord, OnDropAssessmentRecord mOnDropAssessmentRecord, List<Habits> predefinedHabitsList,
                       AssessmentViewModel assessmentViewModel, HabitWithSubroutinesViewModel habitWithSubroutinesViewModel, LifecycleOwner lifecycleOwner,
                       RelativeLayout bindingView) {

            assessmentID.setText((String.format(Locale.getDefault(), "%d", assessmentRecord.getPk_assessment_record_uid())));
            dateTaken.setText(assessmentRecord.getDate_taken());

            if (assessmentRecord.isCompleted()) {
                assessmentStatusLayout.setVisibility(View.GONE);
                dateCompletedLayout.setVisibility(View.VISIBLE);
                divider.setVisibility(View.VISIBLE);
                dateCompleted.setText(assessmentRecord.getDate_completed());
                analysisResultLBL.setVisibility(View.VISIBLE);
                childRV.setVisibility(View.VISIBLE);
                dropAssessmentRecord.setVisibility(View.GONE);
            } else {
                assessmentStatusLayout.setVisibility(View.VISIBLE);
                divider.setVisibility(View.GONE);
                assessmentStatus.setText(String.format(Locale.getDefault(), "%s", "ONGOING"));
                dateCompletedLayout.setVisibility(View.GONE);
                analysisResultLBL.setVisibility(View.GONE);
                childRV.setVisibility(View.GONE);
                dropAssessmentRecord.setVisibility(View.VISIBLE);
            }

            dropAssessmentRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnDropAssessmentRecord != null) {
                        mOnDropAssessmentRecord.onClickDrop(assessmentRecord);
                    }
                }
            });

            KnowledgeBased knowledgeBased = new KnowledgeBased(assessmentRecord);
            knowledgeBased.setAssessmentViewModel(assessmentViewModel);
            knowledgeBased.setHabitWithSubroutinesViewModel(habitWithSubroutinesViewModel);
            knowledgeBased.calculateHabitScores();
            knowledgeBased.getRecommendedHabitsScore();

            assessmentRecordChildItemAdapter.setOldHabitList(new ArrayList<>(predefinedHabitsList));
            habitWithSubroutinesViewModel.getAllPredefinedHabitListLiveData().observe(lifecycleOwner, habitsList -> {
                assessmentRecordChildItemAdapter.setNewHabitList(new ArrayList<>(habitsList));
            });

            assessmentRecordChildItemAdapter.setHabitScoreResult(knowledgeBased.getConvertedToResultList());
            childRV.setAdapter(assessmentRecordChildItemAdapter);

            final long[] predefinedHabitsOnReformCount = {0};
            final int MAXIMUM_PREDEFINED_HABITS_CURRENTLY_ON_REFORM_STATUS = 2;

            habitWithSubroutinesViewModel.getGetPredefinedHabitOnReformCount().observe(lifecycleOwner, new Observer<Long>() {
                @Override
                public void onChanged(Long aLong) {
                    predefinedHabitsOnReformCount[0] = aLong;
                }
            });

            assessmentRecordChildItemAdapter.setmOnAddHabitOnReform(new AssessmentRecordChildItemAdapter.OnAddHabitOnReform() {
                @Override
                public void onClickReform(Habits habit) {

                    if (habit.isOnReform()) {
                        showSnackBarMessage(bindingView, habit.getHabit() + " is already on reform", "Dismiss");
                    } else {
                        if (predefinedHabitsOnReformCount[0] < MAXIMUM_PREDEFINED_HABITS_CURRENTLY_ON_REFORM_STATUS) {
                            new AlertDialog.Builder(itemView.getContext())
                                    .setMessage("Would you want to start [" + habit.getHabit() + "] on reform?")
                                    .setCancelable(false)
                                    .setPositiveButton("YES", (dialogInterface, i) -> {
                                        habit.setOnReform(true);
                                        habit.setDate_started(new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault()).format(new Date()));
                                        habitWithSubroutinesViewModel.updateHabit(habit);
                                        showSnackBarMessage(bindingView, habit.getHabit() + " has been added on reform status", "Okay");
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        } else {
                            showSnackBarMessage(bindingView, "Can only add 2 predefined habits on reform at the same time", "Dismiss");
                            Snackbar.make(bindingView, Html.fromHtml("Can only add 2 predefined habits on reform at the same time"), Snackbar.LENGTH_SHORT)
                                    .setAction("Dismiss", view -> {
                                        //Dismiss snack bar
                                    })
                                    .setActionTextColor(ContextCompat.getColor(itemView.getContext(), R.color.PETER_RIVER))
                                    .setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.NIGHT))
                                    .setBackgroundTint(ContextCompat.getColor(itemView.getContext(), R.color.CLOUDS))
                                    .setDuration(3000) //to seconds duration
                                    .show();
                        }
                    }
                }

                private void showSnackBarMessage(RelativeLayout bindingView, String message, String actionLbl) {
                    Snackbar.make(bindingView, Html.fromHtml(message), Snackbar.LENGTH_SHORT)
                            .setAction(actionLbl, view -> {
                                //Dismiss snack bar
                            })
                            .setActionTextColor(ContextCompat.getColor(itemView.getContext(), R.color.PETER_RIVER))
                            .setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.NIGHT))
                            .setBackgroundTint(ContextCompat.getColor(itemView.getContext(), R.color.CLOUDS))
                            .setDuration(3000) //to seconds duration
                            .show();
                }
            });
        }
    }
}
