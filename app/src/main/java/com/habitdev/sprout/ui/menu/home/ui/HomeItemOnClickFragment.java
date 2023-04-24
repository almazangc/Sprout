package com.habitdev.sprout.ui.menu.home.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.achievement.AchievementViewModel;
import com.habitdev.sprout.database.achievement.model.Achievement;
import com.habitdev.sprout.database.assessment.AssessmentViewModel;
import com.habitdev.sprout.database.comment.CommentViewModel;
import com.habitdev.sprout.database.comment.model.Comment;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.databinding.FragmentHomeItemOnClickBinding;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.enums.HomeConfigurationKeys;
import com.habitdev.sprout.ui.habit_self_assessment.adapter.Model.Result;
import com.habitdev.sprout.ui.menu.home.adapter.HomeItemOnClickParentCommentItemAdapter;
import com.habitdev.sprout.ui.menu.home.adapter.HomeParentItemAdapter;
import com.habitdev.sprout.utill.dialog.CompletedAchievementDialogFragment;
import com.habitdev.sprout.utill.diffutils.DateTimeElapsedUtil;
import com.habitdev.sprout.utill.recommender.KnowledgeBased;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeItemOnClickFragment extends Fragment {

    private FragmentHomeItemOnClickBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private AchievementViewModel achievementViewModel;
    private CommentViewModel commentViewModel;
    private Habits habit;
    private int position;
    private HomeParentItemAdapter adapter_ref;

    private static int current_selected_color;
    private static int old_selected_color;
    private static String color = AppColor.CLOUDS.getColor();

    private static Timer timer;

    public interface OnItemOnClickReturnHome {
        void onHomeItemOnClickReturnHome();
    }

    private OnItemOnClickReturnHome mOnItemOnClickReturnHome;

    public void setmOnItemOnClickReturnHome(OnItemOnClickReturnHome mOnItemOnClickReturnHome) {
        this.mOnItemOnClickReturnHome = mOnItemOnClickReturnHome;
    }

    public HomeItemOnClickFragment() {}

    public void setHabit(Habits habit) {
        this.habit = habit;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setAdapter_ref(HomeParentItemAdapter adapter_ref) {
        this.adapter_ref = adapter_ref;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeItemOnClickBinding.inflate(inflater, container, false);
        commentViewModel = new ViewModelProvider(requireActivity()).get(CommentViewModel.class);
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);

        if (savedInstanceState != null) {
            habit = (Habits) savedInstanceState.getSerializable(HomeConfigurationKeys.HABIT.getKey());
        }

        setHabit();
        if (!habit.isModifiable()) {
            setRecommendedPercent();
        }
        colorSelect();
        insertComment();
        setCommentRecyclerView();
        onBackPress();
        return binding.getRoot();
    }

    private void setRecommendedPercent() {
        AssessmentViewModel assessmentViewModel = new ViewModelProvider(requireActivity()).get(AssessmentViewModel.class);
        KnowledgeBased knowledgeBased = new KnowledgeBased();
        knowledgeBased.setAssessmentViewModel(assessmentViewModel);
        knowledgeBased.setHabitWithSubroutinesViewModel(habitWithSubroutinesViewModel);
        knowledgeBased.calculateHabitScores();
        knowledgeBased.getRecommendedHabitsScore();
        List<Result> habitScoreResult = knowledgeBased.getConvertedToResultList();
        Result result = habitScoreResult.get((int) (habit.getPk_habit_uid()-1));
        Double score = result.getScore();
        DecimalFormat decimalFormat = new DecimalFormat("##%");
        String formattedScore = decimalFormat.format(score);
        binding.recommendedPercent.setText(formattedScore);

        if (score == 1) {
            binding.recommendedPercent.setTextColor(ContextCompat.getColor(requireContext(), R.color.RUSTIC_RED));
        } else if (score > .75) {
            binding.recommendedPercent.setTextColor(ContextCompat.getColor(requireContext(), R.color.CORAL_RED));
        } else if (score > .5) {
            binding.recommendedPercent.setTextColor(ContextCompat.getColor(requireContext(), R.color.WISTERIA));
        } else if (score > .25) {
            binding.recommendedPercent.setTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER));
        } else {
            binding.recommendedPercent.setTextColor(ContextCompat.getColor(requireContext(), R.color.EMERALD));
        }
    }

    private void setHabit() {
        setHabitColor();
        binding.homeItemOnClickHabitTitle.setText(habit.getHabit());
        binding.homeItemOnClickHabitDescription.setText(habit.getDescription());
        binding.homeItemOnClickStatus.setText(habit.isOnReform() ? "ON REFORM" : "ARCHIVED");
        binding.homeItemOnClickHabitDateStartedOnReform.setText(habit.getDate_started());

        DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(habit.getDate_started());
        dateTimeElapsedUtil.calculateElapsedDateTime();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (binding != null) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dateTimeElapsedUtil.calculateElapsedDateTime();
                            /*
                              Why double check nullable binding, it is because of configuration changes that makes
                              binding null before the timer is cancelled
                             */
                            if (binding != null) {
                                binding.homeItemOnClickHabitTotalDaysOfAbstinence.setText(dateTimeElapsedUtil.getResult());
                            }
                        }
                    });
                } else {
                    timer.cancel();
                    timer.purge();
                }
            }
        }, 0, 1000);

        binding.homeItemOnClickHabitRelapse.setText((String.format(Locale.getDefault(), "%d", habit.getRelapse())));
        binding.homeItemOnClickHabitTotalSubroutine.setText((String.format(Locale.getDefault(), "%d", habit.getCompleted_subroutine())));
    }

    private void colorSelect() {
        binding.homeItemOnClickColorSelector.setOnClickListener(v -> {
            if (binding.homeItemOnClickMiscellaneous.getVisibility() == View.GONE) {
                binding.homeItemOnClickMiscellaneous.setVisibility(View.VISIBLE);
            } else {
                binding.homeItemOnClickMiscellaneous.setVisibility(View.GONE);
            }
        });

        binding.cloudMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(0);
            setSelected_color();
            update_habit_color();
        });

        binding.alzarinMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(1);
            setSelected_color();
            update_habit_color();
        });

        binding.amethystMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(2);
            setSelected_color();
            update_habit_color();
        });

        binding.brightskyBlueMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(3);
            setSelected_color();
            update_habit_color();
        });

        binding.nephritisMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(4);
            setSelected_color();
            update_habit_color();
        });

        binding.sunflowerMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(5);
            setSelected_color();
            update_habit_color();
        });
    }

    private void setHabitColor() {
        if (habit != null) {
            if (habit.getColor().equals(AppColor.ALZARIN.getColor())) {
                current_selected_color = 1;
            } else if (habit.getColor().equals(AppColor.AMETHYST.getColor())) {
                current_selected_color = 2;
            } else if (habit.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                current_selected_color = 3;
            } else if (habit.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                current_selected_color = 4;
            } else if (habit.getColor().equals(AppColor.SUNFLOWER.getColor())) {
                current_selected_color = 5;
            } else {
                old_selected_color = 1;
            }
        } else {
            old_selected_color = 1;
        }
        setSelected_color();
    }

    private void setSelected_color() {
        if (old_selected_color != current_selected_color) {

            final int ic_check = R.drawable.ic_check;

            switch (current_selected_color) {
                case 1:
                    binding.alzarinSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_alzarin));
                    color = AppColor.ALZARIN.getColor();
                    break;
                case 2:
                    binding.amethystSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_amethyst));
                    color = AppColor.AMETHYST.getColor();
                    break;
                case 3:
                    binding.brightskyBlueSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_brightsky_blue));
                    color = AppColor.BRIGHT_SKY_BLUE.getColor();
                    break;
                case 4:
                    binding.nephritisSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_nephritis));
                    color = AppColor.NEPHRITIS.getColor();
                    break;
                case 5:
                    binding.sunflowerSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_sunflower));
                    color = AppColor.SUNFLOWER.getColor();
                    break;
                default:
                    binding.cloudSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_clouds));
                    color = AppColor.CLOUDS.getColor();
                    break;
            }

            switch (old_selected_color) {
                case 1:
                    binding.alzarinSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 2:
                    binding.amethystSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 3:
                    binding.brightskyBlueSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 4:
                    binding.nephritisSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 5:
                    binding.sunflowerSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                default:
                    binding.cloudSelected.setImageResource(R.color.TRANSPARENT);
                    break;
            }
        }
    }

    private void updateSelectedColorIndex(int newSelected) {
        old_selected_color = current_selected_color;
        current_selected_color = newSelected;
    }

    private void setBackgroundColorIndicator(Drawable backgroundNoteIndicator) {
        binding.homeItemOnClickColorSelector.setBackground(backgroundNoteIndicator);
    }

    private void update_habit_color() {
        habit.setColor(color);
        habitWithSubroutinesViewModel.updateHabit(habit);
        adapter_ref.notifyItemChanged(position);
    }

    private void insertComment() {
        binding.addCommentBtn.setOnClickListener(view -> {
            if (!binding.addCommentInputText.getText().toString().trim().isEmpty()) {
                commentViewModel.insertComment(
                        new Comment(
                                habit.getPk_habit_uid(),
                                binding.addCommentInputText.getText().toString().trim(),
                                new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm a", Locale.getDefault())
                                        .format(new Date())
                        )
                );
                binding.addCommentInputText.setText("");

                //TODO: UPDATE UID WHEN APPDATABASE CHANGE
                achievementViewModel = new ViewModelProvider(requireActivity()).get(AchievementViewModel.class);

                Achievement CommentIII = achievementViewModel.getAchievementByUID(6);
                Achievement CommentII = achievementViewModel.getAchievementByUID(CommentIII.getPrerequisite_uid());
                Achievement CommentI = achievementViewModel.getAchievementByUID(CommentII.getPrerequisite_uid());

                if (!CommentI.is_completed()) {
                    updateUnlockedAchievement(CommentI);
                } else if (!CommentII.is_completed() && CommentI.is_completed() && CommentII.getGoal_progress() -2 >= CommentII.getCurrent_progress()) {
                    incrementprogress(CommentII);
                } else if (!CommentII.is_completed() && CommentI.is_completed() && CommentII.getGoal_progress() -1 == CommentII.getCurrent_progress()) {
                    updateUnlockedAchievement(CommentII);
                } else if (!CommentIII.is_completed() && CommentII.is_completed() && CommentIII.getGoal_progress() -2 >=  CommentIII.getCurrent_progress()){
                    incrementprogress(CommentIII);
                } else if (!CommentIII.is_completed() && CommentII.is_completed() && CommentIII.getGoal_progress() -1 ==  CommentIII.getCurrent_progress()) {
                    updateUnlockedAchievement(CommentIII);
                }
            }
        });
    }

    private void updateUnlockedAchievement(Achievement achievement) {
        final String SDF_PATTERN = "MMMM d, yyyy";
        achievement.setIs_completed(true);
        achievement.setDate_achieved(new SimpleDateFormat(SDF_PATTERN, Locale.getDefault()).format(new Date()));
        incrementprogress(achievement);
        showCompletedAchievementDialog(achievement);
    }

    private void incrementprogress(Achievement achievement) {
        achievement.setCurrent_progress(achievement.getCurrent_progress() + 1);
        achievementViewModel.updateAchievement(achievement);
    }

    private void showCompletedAchievementDialog(Achievement achievement) {
        CompletedAchievementDialogFragment dialog = new CompletedAchievementDialogFragment(achievement.getTitle());
        dialog.setTargetFragment(getChildFragmentManager()
                .findFragmentById(HomeItemOnClickFragment.this.getId()), 1);
        dialog.show(getChildFragmentManager(), "CompletedAchievementDialog");
    }

    private void setCommentRecyclerView() {
        if (binding.homeCommentRecyclerView.getAdapter() == null) {
            HomeItemOnClickParentCommentItemAdapter homeParentItemAdapter = new HomeItemOnClickParentCommentItemAdapter(commentViewModel);
            binding.homeCommentRecyclerView.setAdapter(homeParentItemAdapter);
            commentViewModel.getAllHabitCommentByUID(habit.getPk_habit_uid()).observe(getViewLifecycleOwner(), comments -> {
                if (comments != null) homeParentItemAdapter.setNewCommentList(new ArrayList<>(comments));
            });
        }
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnItemOnClickReturnHome != null)
                    mOnItemOnClickReturnHome.onHomeItemOnClickReturnHome();
                if (timer != null) {
                    timer.cancel();
                    timer.purge();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(HomeConfigurationKeys.HABIT.getKey(), habit);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnItemOnClickReturnHome = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        commentViewModel.getAllHabitCommentByUID(habit.getPk_habit_uid()).removeObservers(getViewLifecycleOwner());
        habit = null;
        commentViewModel = null;
        habitWithSubroutinesViewModel = null;
        adapter_ref = null;
        binding = null;
    }
}