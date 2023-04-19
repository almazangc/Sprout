package com.habitdev.sprout.ui.habit_assessment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.assessment.AssessmentViewModel;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.model.room.Subroutines;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentAnalysisBinding;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.ui.habit_assessment.adapter.AnalysisParentItemAdapter;
import com.habitdev.sprout.ui.habit_assessment.adapter.AnalysisParentItemDropDownAdapter;
import com.habitdev.sprout.ui.habit_assessment.adapter.Model.Result;
import com.habitdev.sprout.ui.menu.OnBackPressDialogFragment;
import com.habitdev.sprout.ui.menu.setting.ui.ProfileFragment;
import com.habitdev.sprout.utill.recommender.KnowledgeBased;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AnalysisFragment extends Fragment {

    private FragmentAnalysisBinding binding;
    private static HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private static List<Habits> habitsList;
    private static Habits habit;
    private static AnalysisParentItemAdapter analysisParentItemAdapter;
    private static List<Subroutines> subroutinesList;
    private static boolean isOnRetakeAssessment;
    private static List<Result> habitScoreResult;
    private static ProfileFragment profileFragment = new ProfileFragment();

    public AnalysisFragment() {
        habit = new Habits(
                null,
                null,
                AppColor.CLOUDS.getColor(),
                false,
                false
        );
        subroutinesList = new ArrayList<>();
        isOnRetakeAssessment = false;
    }

    public AnalysisFragment(boolean isOnRetakeAssessment) {
        habit = new Habits(
                null,
                null,
                AppColor.CLOUDS.getColor(),
                false,
                false
        );
        subroutinesList = new ArrayList<>();
        AnalysisFragment.isOnRetakeAssessment = isOnRetakeAssessment;
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
        setContinueListener();
        calculateRecommendedHabit();
        setRecyclerViewAdapter();
    }

    /**
     * A knowledge-based recommender which follower a rule-base algorithm using assessment tool for determining what habit will likely be recommended.
     */
    private void calculateRecommendedHabit() {
        AssessmentViewModel assessmentViewModel = new ViewModelProvider(requireActivity()).get(AssessmentViewModel.class);
        KnowledgeBased knowledgeBased = new KnowledgeBased();
        knowledgeBased.setAssessmentViewModel(assessmentViewModel);
        knowledgeBased.setHabitWithSubroutinesViewModel(habitWithSubroutinesViewModel);
        knowledgeBased.calculateHabitScores();
        knowledgeBased.getRecommendedHabitsScore();
        habitScoreResult = knowledgeBased.getConvertedToResultList();
        setDropDownItems();
        setInitialRecommendationMessage();
    }

    private void setDropDownItems() {
        AnalysisParentItemDropDownAdapter analysisParentItemDropDownAdapter = new AnalysisParentItemDropDownAdapter(requireContext(), new ArrayList<>(habitScoreResult));
        analysisParentItemDropDownAdapter.setHabitWithSubroutinesViewModel(habitWithSubroutinesViewModel);
        binding.analysisDropItem.setAdapter(analysisParentItemDropDownAdapter);
        setDropDownItemListener();
        setHabitObserver();
    }

    private void setInitialRecommendationMessage() {
        Result result = new Result().getHighestConfidenceScore(habitScoreResult);
        Habits habit = habitWithSubroutinesViewModel.getHabitByUID(result.getHabit_uid());
        setAnalysisMessage(result, habit);
        displayHabitInformation(habit);
    }

    @SuppressLint("SetTextI18n")
    private void setAnalysisMessage(Result result, Habits habit) {
        String habitAssessmentResult =
                "Based on your habit personalization assessment.<br/>The result shows that you have a " +
                "<b>%s</b> likelihood of having the habit: <b><font color='%s'>%s</font></b>.<br/><br/>";
        String[] recommendationMessage = {
                "We recommend you focus on reforming this habit to improve well-being.",
                "Reforming this habit can positively impact your overall health and wellness.",
        };
        String[] considerReformingMessages = {
                "While this habit may not be a top priority for improvement, it is still worth considering as you work towards building healthier habits.",
                "This habit may not be at the top of the list, but it's still worth considering as you aim to develop healthier habits.",
                "Although there may be more significant habits to work on first, improving this one can still have a positive impact on your health and well-being."
        };

        String focusMessage = recommendationMessage[new Random().nextInt(recommendationMessage.length)];
        String considerMessage = considerReformingMessages[new Random().nextInt(considerReformingMessages.length)];

        String hexColorValue = String.format("#%06X", (0xFFFFFF & ContextCompat.getColor(requireContext(), R.color.CORAL_RED)));
        String finalMessage = String.format(habitAssessmentResult, result.getFormattedConfidenceScore(), hexColorValue, habit.getHabit().toUpperCase(Locale.ROOT)) +
                (result.getScore() >= 0.7 ? focusMessage : considerMessage);

        Spanned formattedMessage = Html.fromHtml(finalMessage);
        binding.analysisResult.setText(formattedMessage);
    }

    private void setHabitObserver() {
        habitWithSubroutinesViewModel.getAllHabitListLiveData().observe(getViewLifecycleOwner(), habits -> {
            habitsList = new ArrayList<>(habits);
        });
    }

    private void setRecyclerViewAdapter() {
        if (subroutinesList != null) {
            if (analysisParentItemAdapter == null) {
                analysisParentItemAdapter = new AnalysisParentItemAdapter();
                analysisParentItemAdapter.setOldSubroutinesList(new ArrayList<>(subroutinesList));
            }

            if (binding.analysisRecyclerView.getAdapter() == null) {
                binding.analysisRecyclerView.setAdapter(analysisParentItemAdapter);
            } else {
                analysisParentItemAdapter.setNewSubroutineList(new ArrayList<>(subroutinesList));
            }
        }
    }

    private void setDropDownItemListener() {
        binding.analysisDropItem.setOnItemClickListener((adapterView, view, position, id) -> {
            //updates selected habit
            habit = habitsList.get(position);
            displayHabitInformation(habit);

            Result result = habitScoreResult.get(position);
            setAnalysisMessage(result, habit);

            setRecyclerViewAdapter();
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
                    setRecyclerViewAdapter();
                }
            }
        });
    }

    private void displayHabitInformation(Habits habit) {
        binding.analysisDropItem.setText("");
        binding.analysisHabitTitle.setText(habit.getHabit());
        binding.analysisHabitDescription.setText(habit.getDescription());
        subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());
        setRecyclerViewAdapter();
    }

    private void setContinueListener() {

        final long[] predefinedHabitsOnReformCount = {0};
        final int MAXIMUM_PREDEFINED_HABITS_CURRENTLY_ON_REFORM_STATUS = 2;

        habitWithSubroutinesViewModel.getGetPredefinedHabitOnReformCount().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                predefinedHabitsOnReformCount[0] = aLong;
            }
        });

        binding.analysisContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (habit.getHabit() != null) {
                    if (!habit.isOnReform()) {
                        if (predefinedHabitsOnReformCount[0] < MAXIMUM_PREDEFINED_HABITS_CURRENTLY_ON_REFORM_STATUS) {
                            new AlertDialog.Builder(requireContext())
                                    .setMessage("Do you want to start [" + habit.getHabit() + "] on reform?")
                                    .setCancelable(false)
                                    .setPositiveButton("YES", (dialogInterface, i) -> {
                                        habit.setOnReform(true);
                                        habit.setDate_started(new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault()).format(new Date()));
                                        habitWithSubroutinesViewModel.updateHabit(habit);

                                        if (isOnRetakeAssessment) {
                                            returnToProfileTab();
                                        } else {
                                            setOnBoarding();
                                            Bundle bundle = new Bundle();
                                            bundle.putBoolean(BundleKeys.ANALYSIS.getKEY(), true);
                                            Navigation.findNavController(view).navigate(R.id.action_navigate_from_analysis_to_Home, bundle);
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        } else {
                            Snackbar.make(binding.getRoot(), Html.fromHtml("Can only add 2 predefined habits on reform at the same time"), Snackbar.LENGTH_SHORT)
                                    .setAction("Return to Profile Tab", view2 -> {
                                        //Dismiss snack bar
                                        returnToProfileTab();
                                    })
                                    .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER))
                                    .setTextColor(getResources().getColor(R.color.NIGHT))
                                    .setBackgroundTint(getResources().getColor(R.color.CLOUDS))
                                    .setDuration(5000) //to seconds duration
                                    .show();
                        }
                    } else {
                        new AlertDialog.Builder(requireContext())
                                .setMessage("The habit [" + habit.getHabit() + "] is currently on reform, do you want to proceed?")
                                .setCancelable(false)
                                .setPositiveButton("YES", (dialogInterface, i) -> {
                                    returnToProfileTab();
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(binding.getRoot(), Html.fromHtml("Please choose a habit you like to reform"), Snackbar.LENGTH_LONG)
                            .setAction("Dismiss", snackbarview -> {
                                //Dismiss snack bar
                            })
                            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER))
                            .setTextColor(getResources().getColor(R.color.NIGHT))
                            .setBackgroundTint(getResources().getColor(R.color.CLOUDS))
                            .setDuration(3000);

                    View snackbarView = snackbar.getView();
                    Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbarView;
                    layout.setPadding(layout.getPaddingLeft(), 30, layout.getPaddingRight(), 30);
                    snackbar.show();

                    snackbarView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View view) {
                            // Snackbar is attached
                        }

                        @Override
                        public void onViewDetachedFromWindow(View view) {
                            // Snackbar is detached
                            snackbar.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void returnToProfileTab() {
        habitWithSubroutinesViewModel.getAllHabitListLiveData().removeObservers(getViewLifecycleOwner());
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(AnalysisFragment.this.getTag())
                .add(binding.analysisRelativeLayout.getId(), profileFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        binding.analysisContainer.setVisibility(View.GONE);
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