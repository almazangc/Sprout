package com.habitdev.sprout.ui.menu.analytic.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentAnalyticItemOnClickBinding;
import com.habitdev.sprout.enums.AnalyticConfigurationKeys;
import com.habitdev.sprout.ui.menu.analytic.adapter.AnalyticItemOnClickParentItemAdapter;

import java.util.List;

public class AnalyticItemOnClickFragment extends Fragment {

    private FragmentAnalyticItemOnClickBinding binding;
    private Habits habit;
    private static final AnalyticItemOnClickParentItemAdapter analyticItemOnClickParentItemAdapter = new AnalyticItemOnClickParentItemAdapter();

    public interface OnclickListener{
        void returnOnAnalytic();
    }

    private OnclickListener mOclickListener;

    public void setmOclickListener(OnclickListener mOclickListener) {
        this.mOclickListener = mOclickListener;
    }

    public void setHabit(Habits habit) {
        this.habit = habit;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOclickListener = (OnclickListener) getParentFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAnalyticItemOnClickBinding.inflate(inflater, container, false);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            habit = (Habits) savedInstanceState.getSerializable(AnalyticConfigurationKeys.HABIT.getKey());
        }
        binding.analyticItemOnClickHabitTitle.setText(habit.getHabit());
        setRecyclerViewAdapter();
        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setRecyclerViewAdapter() {
        if (binding.analyticItemOnClickRecyclerView.getAdapter() == null) {
            HabitWithSubroutinesViewModel habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
            List<Subroutines> subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());
            analyticItemOnClickParentItemAdapter.setOldSubroutinesList(subroutinesList);
            binding.analyticItemOnClickRecyclerView.setAdapter(analyticItemOnClickParentItemAdapter);

            habitWithSubroutinesViewModel.getAllSubroutinesOnReformHabitLiveData(habit.getPk_habit_uid()).observe(getViewLifecycleOwner(), subroutines -> {
                analyticItemOnClickParentItemAdapter.setNewSubroutineList(subroutinesList);
            });
        }
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOclickListener != null) mOclickListener.returnOnAnalytic();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(AnalyticConfigurationKeys.HABIT.getKey(), habit);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOclickListener != null) mOclickListener = null;
    }
}