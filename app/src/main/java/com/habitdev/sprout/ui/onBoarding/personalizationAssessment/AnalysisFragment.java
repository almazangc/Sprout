package com.habitdev.sprout.ui.onBoarding.personalizationAssessment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.assessment.AssessmentViewModel;
import com.habitdev.sprout.ui.onBoarding.personalizationAssessment.adapter.Model.Result;
import com.habitdev.sprout.utill.Recommender.HabitRecommender;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.model.room.Subroutines;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentAnalysisBinding;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.ui.menu.OnBackPressDialogFragment;
import com.habitdev.sprout.ui.onBoarding.personalizationAssessment.adapter.AnalysisParentItemAdapter;
import com.habitdev.sprout.ui.onBoarding.personalizationAssessment.adapter.AnalytisParentItemDropDownAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AnalysisFragment extends Fragment {

    private FragmentAnalysisBinding binding;
    private static HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private static List<Habits> habitsList;
    private static Habits habit;
    private static AnalysisParentItemAdapter analysisParentItemAdapter;
    private static List<Subroutines> subroutinesList;

    public AnalysisFragment() {
        habit = new Habits(
                null,
                null,
                AppColor.CLOUDS.getColor(),
                false,
                false
        );
        subroutinesList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalysisBinding.inflate(inflater, container, false);
        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        toggleHabitDescriptionVisibility(false);
        setContinueListener();
        calculateRecommendedHabit();
        setRecyclerViewAdapter();
    }

    /**
     * A knowledge-based recommender which follower a rule-base algorithm using assessment tool for determining what habit will likely be recommended.
     */
    private void calculateRecommendedHabit(){
        AssessmentViewModel assessmentViewModel = new ViewModelProvider(requireActivity()).get(AssessmentViewModel.class);
        HabitRecommender habitRecommender = new HabitRecommender();
        habitRecommender.setAssessmentViewModel(assessmentViewModel);
        habitRecommender.setHabitWithSubroutinesViewModel(habitWithSubroutinesViewModel);
        habitRecommender.calculateHabitScores();
        habitRecommender.getRecommendedHabitsScore();
        List<Result> habitScoreResult = habitRecommender.getConvertedToResultList();
        setDropDownItems(habitScoreResult); //gets the result to be displayed right.
    }

    private void setDropDownItems(List<Result> habitScore){
        AnalytisParentItemDropDownAdapter analytisParentItemDropDownAdapter = new AnalytisParentItemDropDownAdapter(requireContext(), habitScore);
        analytisParentItemDropDownAdapter.setHabitWithSubroutinesViewModel(habitWithSubroutinesViewModel);
        binding.analysisDropItem.setAdapter(analytisParentItemDropDownAdapter);
        setDropDownItemListener();
        setHabitObserver();
    }

    private void setHabitObserver() {
        habitWithSubroutinesViewModel.getAllHabitListLiveData().observe(getViewLifecycleOwner(), habits -> {
            habitsList = habits;
        });
    }

    private void toggleHabitDescriptionVisibility(boolean visibility){
        if (!visibility) {
            binding.analysisHabitDescrition.setVisibility(View.GONE);
            binding.analysisHabitDescritionLbl.setVisibility(View.GONE);
        } else {
            binding.analysisHabitDescrition.setVisibility(View.VISIBLE);
            binding.analysisHabitDescritionLbl.setVisibility(View.VISIBLE);
        }
    }

    private void setRecyclerViewAdapter(){
        if (subroutinesList != null){
            if (analysisParentItemAdapter == null) {
                analysisParentItemAdapter = new AnalysisParentItemAdapter();
                analysisParentItemAdapter.setOldSubroutinesList(subroutinesList);
            }

            if (binding.analysisRecyclerView.getAdapter() == null) {
                binding.analysisRecyclerView.setAdapter(analysisParentItemAdapter);
            } else {
                binding.analysisHabitDescrition.setText(habit.getDescription());
                analysisParentItemAdapter.setNewSubroutineList(subroutinesList);
            }
        }
    }

    private void setDropDownItemListener() {

        binding.analysisDropItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                habit = habitsList.get(position);
                subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());
                toggleHabitDescriptionVisibility(true);
                binding.analysisHabitDescrition.setText(habit.getDescription());
                setRecyclerViewAdapter();
            }
        });

        binding.analysisDropItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    subroutinesList.clear();
                    toggleHabitDescriptionVisibility(false);
                    setRecyclerViewAdapter();
                }
            }
        });
    }

    private void setContinueListener() {
        binding.analysisContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                habit.setOnReform(true);
                habit.setDate_started(new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault()).format(new Date()));
                habitWithSubroutinesViewModel.updateHabit(habit);

                setOnBoarding();
                Bundle bundle = new Bundle();
                bundle.putBoolean(BundleKeys.ANALYSIS.getKEY(), true);
                Navigation.findNavController(view).navigate(R.id.action_navigate_from_analysis_to_Home, bundle);
            }
        });
    }

    private void onBackPress() {
        final int[] keypress_count = {0};
        final boolean[] isOnBackPressDialogShowing = {false};

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                keypress_count[0]++;

                new CountDownTimer(200, 200) {
                    @Override
                    public void onTick(long l) {}

                    @Override
                    public void onFinish() {
                        if (keypress_count[0] > 1) {
                            //Dialog is displayed twice
                            OnBackPressDialogFragment dialog = new OnBackPressDialogFragment();
                            if (!isOnBackPressDialogShowing[0]) {
                                dialog.setTargetFragment(getChildFragmentManager().findFragmentById(AnalysisFragment.this.getId()), 1);
                                dialog.show(getChildFragmentManager(), "Menu.onBackPress");
                                dialog.setmOnCancelDialog(() -> {
                                    keypress_count[0] = 0;
                                    isOnBackPressDialogShowing[0] = false;
                                });
                                isOnBackPressDialogShowing[0] = true;
                            }
                        } else {
                            requireActivity().moveTaskToBack(true);
                            keypress_count[0] = 0;
                        }
                        this.cancel();
                    }
                }.start();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void setOnBoarding() {
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setOnBoarding();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        habitWithSubroutinesViewModel.getAllHabitListLiveData().removeObservers(getViewLifecycleOwner());
        habitWithSubroutinesViewModel = null;
        binding = null;
    }
}