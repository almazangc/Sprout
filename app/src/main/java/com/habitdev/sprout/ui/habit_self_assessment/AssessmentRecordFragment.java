package com.habitdev.sprout.ui.habit_self_assessment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.assessment.AssessmentViewModel;
import com.habitdev.sprout.database.assessment.model.AssessmentRecord;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.databinding.FragmentAssessmentRecordBinding;
import com.habitdev.sprout.ui.habit_self_assessment.adapter.AssessmentRecordParentItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class AssessmentRecordFragment extends Fragment {

    private static AssessmentViewModel assessmentViewModel;
    private static HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private final AssessmentRecordParentItemAdapter assessmentRecordParentItemAdapter = new AssessmentRecordParentItemAdapter();
    private static List<AssessmentRecord> assessmentRecords;

    public interface OnReturnSetting {
        void returnFromAssessmentRecordToProfileTab();
    }

    private OnReturnSetting mOnReturnProfileTab;

    public void setmOnReturnProfileTab(OnReturnSetting mOnReturnProfileTab) {
        this.mOnReturnProfileTab = mOnReturnProfileTab;
    }

    private FragmentAssessmentRecordBinding binding;

    public AssessmentRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAssessmentRecordBinding.inflate(inflater, container, false);
        assessmentViewModel = new ViewModelProvider(requireActivity()).get(AssessmentViewModel.class);
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        setRecyclerViewAdapter();
        onBackPress();
        return binding.getRoot();
    }

    private void setRecyclerViewAdapter() {
        assessmentRecords = assessmentViewModel.getAssessmentRecord();
        List<Habits> predefinedHabitsList = habitWithSubroutinesViewModel.getAllPredefinedHabitList();
        assessmentRecordParentItemAdapter.setOldAssessmentRecords(assessmentRecords);
        assessmentRecordParentItemAdapter.setPredefinedHabitsList(predefinedHabitsList);
        assessmentRecordParentItemAdapter.setAssessmentViewModel(assessmentViewModel);
        assessmentRecordParentItemAdapter.setHabitWithSubroutinesViewModel(habitWithSubroutinesViewModel);
        assessmentRecordParentItemAdapter.setLifecycleOwner(getViewLifecycleOwner());
        assessmentRecordParentItemAdapter.setBindingView(binding.getRoot());

        binding.settingProfileAssessmentRecordRecyclerView.setAdapter(assessmentRecordParentItemAdapter);

        assessmentRecordParentItemAdapter.setmOnDropAssessmentRecord(new AssessmentRecordParentItemAdapter.OnDropAssessmentRecord() {
            @Override
            public void onClickDrop(AssessmentRecord assessmentRecord) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Would you like to remove incomplete assessment record?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            assessmentViewModel.deleteAnswersByAssessmentRecordUid(assessmentRecord.getPk_assessment_record_uid());
                            assessmentViewModel.deleteAssessmentRecord(assessmentRecord);

                            Snackbar.make(binding.getRoot(), Html.fromHtml("<b> Assessment " + assessmentRecord.getPk_assessment_record_uid()+ "</b>: Has been removed"), Snackbar.LENGTH_SHORT)
                                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.NIGHT))
                                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.CLOUDS))
                                    .show();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        setRecyclerViewObserver(assessmentRecordParentItemAdapter);
    }

    private void setRecyclerViewObserver(@NonNull AssessmentRecordParentItemAdapter assessmentRecordParentItemAdapter) {
        assessmentViewModel.getAssessmentRecordLiveData().observe(getViewLifecycleOwner(), new Observer<List<AssessmentRecord>>() {
            @Override
            public void onChanged(List<AssessmentRecord> assessmentRecordList) {
                assessmentRecords = new ArrayList<>(assessmentRecordList);
                assessmentRecordParentItemAdapter.seNewAssessmentRecords(assessmentRecords);
            }
        });
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnReturnProfileTab != null) mOnReturnProfileTab.returnFromAssessmentRecordToProfileTab();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}