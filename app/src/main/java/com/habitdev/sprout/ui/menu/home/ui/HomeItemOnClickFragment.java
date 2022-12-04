package com.habitdev.sprout.ui.menu.home.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.comment.CommentViewModel;
import com.habitdev.sprout.database.comment.model.Comment;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.databinding.FragmentHomeItemOnClickViewBinding;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.ui.menu.home.HomeParentItemFragment;
import com.habitdev.sprout.ui.menu.home.adapter.HomeItemOnClickParentCommentItemAdapter;
import com.habitdev.sprout.utill.DateTimeElapsedUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeItemOnClickFragment extends Fragment {

    private FragmentHomeItemOnClickViewBinding binding;
    private Habits habit;
    private CommentViewModel commentViewModel;

    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private final int ic_check;
    private int current_selected_color;
    private int old_selected_color;
    private String color;

    public HomeItemOnClickFragment(Habits habit) {
        this.habit = habit;
        this.ic_check = R.drawable.ic_check;
        this.current_selected_color = 0;
        this.old_selected_color = 0;
        this.color = AppColor.CLOUDS.getColor();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeItemOnClickViewBinding.inflate(inflater, container, false);
        commentViewModel = new ViewModelProvider(requireActivity()).get(CommentViewModel.class);
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        setHabit();
        colorSelect();
        insertComment();
        setCommentRecyclerView();
        onBackPress();
        return binding.getRoot();
    }

    private void setHabit(){
        setHabitColor();
        binding.homeItemOnClickHabitTitle.setText(habit.getHabit());
        binding.homeItemOnClickHabitDescription.setText(habit.getDescription());
        binding.homeItemOnClickStatus.setText(habit.isOnReform() ? "ON REFORM" : "AVAILABLE");
        binding.homeItemOnClickHabitDateStartedOnReform.setText(habit.getDate_started());

        DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(habit.getDate_started());
        dateTimeElapsedUtil.calculateElapsedDateTime();
        //TODO: Functional But Leaking
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                homeActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//        },0,1000);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        dateTimeElapsedUtil.calculateElapsedDateTime();
                        if (binding != null)
                            binding.homeItemOnClickHabitTotalDaysOfAbstinence.setText(dateTimeElapsedUtil.getResult());
                    }
                });
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

    private void setHabitColor(){
        if (habit.getColor().equals(AppColor.ALZARIN.getColor())) {
            current_selected_color = 1;
            setSelected_color();
        } else if (habit.getColor().equals(AppColor.AMETHYST.getColor())) {
            current_selected_color = 2;
            setSelected_color();
        } else if (habit.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
            current_selected_color = 3;
            setSelected_color();
        } else if (habit.getColor().equals(AppColor.NEPHRITIS.getColor())) {
            current_selected_color = 4;
            setSelected_color();
        } else if (habit.getColor().equals(AppColor.SUNFLOWER.getColor())) {
            current_selected_color = 5;
            setSelected_color();
        } else {
            old_selected_color = 1;
            setSelected_color();
        }
    }

    private void setSelected_color() {
        if (old_selected_color != current_selected_color) {
            switch (current_selected_color) {
                case 1:
                    //alzarin
                    binding.alzarinSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_alzarin));
                    color = AppColor.ALZARIN.getColor();
                    break;
                case 2:
                    //amethyst
                    binding.amethystSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_amethyst));
                    color = AppColor.AMETHYST.getColor();
                    break;
                case 3:
                    //bright_sky_blue
                    binding.brightskyBlueSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_brightsky_blue));
                    color = AppColor.BRIGHT_SKY_BLUE.getColor();
                    break;
                case 4:
                    //nephritis
                    binding.nephritisSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_nephritis));
                    color = AppColor.NEPHRITIS.getColor();
                    break;
                case 5:
                    //sunflower
                    binding.sunflowerSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_sunflower));
                    color = AppColor.SUNFLOWER.getColor();
                    break;
                default:
                    //clouds night
                    binding.cloudSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_clouds));
                    color = AppColor.CLOUDS.getColor();
                    break;
            }

            switch (old_selected_color) {
                case 1:
                    //alzarin
                    binding.alzarinSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 2:
                    //amethyst
                    binding.amethystSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 3:
                    //bright_sky_blue
                    binding.brightskyBlueSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 4:
                    //nephritis
                    binding.nephritisSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 5:
                    //sunflower
                    binding.sunflowerSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                default:
                    //clouds night
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

    private void update_habit_color(){
        habit.setColor(color);
        habitWithSubroutinesViewModel.updateHabit(habit);
    }

    private void insertComment(){
        binding.addCommentBtn.setOnClickListener(view -> {
            if (!binding.addCommentInputText.getText().toString().trim().isEmpty()){
                commentViewModel.insertComment(
                        new Comment(
                                habit.getPk_habit_uid(),
                                0,
                                "Habit",
                                binding.addCommentInputText.getText().toString().trim(),
                                new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm a", Locale.getDefault())
                                        .format(new Date())
                        )
                );
            }
        });
    }

    private void setCommentRecyclerView() {
        HomeItemOnClickParentCommentItemAdapter homeParentItemAdapter = new HomeItemOnClickParentCommentItemAdapter(commentViewModel);
        binding.homeCommentRecyclerView.setAdapter(homeParentItemAdapter);

        commentViewModel.getCommentsFromHabitByUID(habit.getPk_habit_uid()).observe(getViewLifecycleOwner(), comments -> {
            if (comments != null){
                homeParentItemAdapter.submitList(comments);
            }
        });
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //TODO: Fix how to manage fragments
                HomeParentItemFragment homeFragment = new HomeParentItemFragment();
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(binding.homeItemOnClickFrameLayout.getId(), homeFragment)
                        .commit();
                binding.homeItemOnClickContainer.setVisibility(View.GONE);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        commentViewModel.getCommentsFromHabitByUID(habit.getPk_habit_uid()).removeObservers(getViewLifecycleOwner());
        habit = null;
        commentViewModel = null;
        habitWithSubroutinesViewModel = null;
        binding = null;
    }
}