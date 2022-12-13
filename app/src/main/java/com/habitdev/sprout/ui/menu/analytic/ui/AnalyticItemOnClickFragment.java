package com.habitdev.sprout.ui.menu.analytic.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentAnalyticItemOnClickBinding;
import com.habitdev.sprout.ui.menu.analytic.adapter.AnalyticItemOnClickParentItemAdapter;

import java.util.List;

public class AnalyticItemOnClickFragment extends Fragment {

    private FragmentAnalyticItemOnClickBinding binding;
    private Habits habit;
    private static final AnalyticItemOnClickParentItemAdapter analyticItemOnClickParentItemAdapter = new AnalyticItemOnClickParentItemAdapter();

    public interface OnclickListener{
        void setAnalyticParentItemOnclickListener();
    }

    private OnclickListener mOclickListener;

    public void setmOclickListener(OnclickListener mOclickListener) {
        this.mOclickListener = mOclickListener;
    }

    public void setHabit(Habits habit) {
        this.habit = habit;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAnalyticItemOnClickBinding.inflate(inflater, container, false);
        binding.analyticItemOnClickHabitTitle.setText(habit.getHabit());
        setRecyclerViewAdapter();
        onBackPress();
        return binding.getRoot();
    }

    private void setRecyclerViewAdapter() {
        HabitWithSubroutinesViewModel habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        List<Subroutines> subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());
        analyticItemOnClickParentItemAdapter.setOldSubroutinesList(subroutinesList);
        binding.analyticItemOnClickRecyclerView.setAdapter(analyticItemOnClickParentItemAdapter);
        setRecyclerViewAdapterObserver();
    }

    private void setRecyclerViewAdapterObserver() {
        //obeserver
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOclickListener != null) mOclickListener.setAnalyticParentItemOnclickListener();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}