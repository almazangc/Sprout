package com.habitdev.sprout.ui.menu.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.database.comment.CommentViewModel;
import com.habitdev.sprout.database.comment.model.Comment;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.databinding.FragmentHomeItemOnClickViewBinding;
import com.habitdev.sprout.ui.menu.home.HomeFragment;
import com.habitdev.sprout.ui.menu.home.adapter.HomeItemOnClickParentCommentItemAdapter;
import com.habitdev.sprout.utill.DateTimeElapsedUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeItemOnClickFragment extends Fragment {

    private FragmentHomeItemOnClickViewBinding binding;
    private Habits habit;
    private CommentViewModel commentViewModel;
    private List<Comment> habitComments;
    private FragmentActivity homeActivity;

    public HomeItemOnClickFragment() {

    }

    public HomeItemOnClickFragment(Habits habit, FragmentActivity fragmentActivity) {
        this.habit = habit;
        this.homeActivity = fragmentActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeItemOnClickViewBinding.inflate(inflater, container, false);
        commentViewModel = new ViewModelProvider(requireActivity()).get(CommentViewModel.class);
        updateUI();
        insertComment();
        onBackPress();
        return binding.getRoot();
    }

    private void updateUI(){
        binding.homeItemOnClickHabitTitle.setText(habit.getHabit());
        binding.homeItemOnClickHabitDescription.setText(habit.getDescription());

//        binding.homeItemOnClickHabitUpVotes.setText((String.format(Locale.getDefault(), "%d", 0)));
//        binding.homeItemOnClickHabitDownVotes.setText((String.format(Locale.getDefault(), "%d", 0)));

        binding.homeItemOnClickStatus.setText(habit.isOnReform() ? "ON REFORM" : "AVAILABLE");
        binding.homeItemOnClickHabitDateStartedOnReform.setText(habit.getDate_started());

        DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(habit.getDate_started());
        dateTimeElapsedUtil.calculateElapsedDateTime();


        //TODO: Functional But Leaking
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                homeActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dateTimeElapsedUtil.calculateElapsedDateTime();
                        if (binding != null)
                        binding.homeItemOnClickHabitTotalDaysOfAbstinence.setText(dateTimeElapsedUtil.getResult());
                    }
                });
            }
        },0,1000);

        binding.homeItemOnClickHabitRelapse.setText((String.format(Locale.getDefault(), "%d", habit.getRelapse())));
        binding.homeItemOnClickHabitTotalSubroutine.setText((String.format(Locale.getDefault(), "%d", habit.getCompleted_subroutine())));
        setCommentRecyclerView();
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
        habitComments = new ArrayList<>();
        HomeItemOnClickParentCommentItemAdapter homeParentItemAdapter = new HomeItemOnClickParentCommentItemAdapter(habitComments, commentViewModel);
        binding.homeCommentRecyclerView.setAdapter(homeParentItemAdapter);

        commentViewModel.getCommentsFromHabitByUID(habit.getPk_habit_uid()).observe(getViewLifecycleOwner(), comments -> {
            if (!comments.isEmpty()){
                homeParentItemAdapter.setComments(comments);
                habitComments = comments;
            }
        });
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //TODO: Fix how to manage fragments
                HomeFragment homeFragment = new HomeFragment();
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
        binding = null;
    }
}