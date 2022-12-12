package com.habitdev.sprout.ui.menu.analytic.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.databinding.FragmentAnalyticItemOnClickBinding;

public class AnalyticItemOnClickFragment extends Fragment {

    private FragmentAnalyticItemOnClickBinding binding;
    private Habits habit;
    private int position;

    public void setHabit(Habits habit) {
        this.habit = habit;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAnalyticItemOnClickBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}