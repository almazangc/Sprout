package com.habitdev.sprout.ui.menu.analytic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.database.user.model.User;
import com.habitdev.sprout.databinding.FragmentAnalyticBinding;
import com.habitdev.sprout.ui.menu.analytic.adapter.AnalyticParentItemAdapter;
import com.habitdev.sprout.utill.DateTimeElapsedUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AnalyticFragment extends Fragment {

    private FragmentAnalyticBinding binding;
    private UserViewModel userViewModel;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private AnalyticParentItemAdapter analyticParentItemAdapter = new AnalyticParentItemAdapter();

    public AnalyticFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalyticBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        setDateSinceInstalledElapsedTime();
        setRecyclerViewAdapter();

        onBackPress();
        return binding.getRoot();
    }

    private void setDateSinceInstalledElapsedTime(){
        User user = userViewModel.getUserByUID(1);
        String date = user.getDateInstalled();
        DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(date);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (binding != null){
                        dateTimeElapsedUtil.calculateElapsedDateTime();
                        binding.dateSinceInstalled.setText(dateTimeElapsedUtil.getResult());
                    } else {
                        timer.cancel();
                        timer.purge();
                    }
                });
            }
        }, 0, 1000);
    }

    private void setRecyclerViewAdapter(){
        List<Habits> habitsList = habitWithSubroutinesViewModel.getAllHabitOnReform();
        analyticParentItemAdapter.setOldHabitsList(habitsList);
        analyticParentItemAdapter.setHabitWithSubroutinesViewModel(habitWithSubroutinesViewModel);
        binding.analyticHabitOnReformRecyclerView.setAdapter(analyticParentItemAdapter);
        setRecyclerViewAdapterObserver();
    }

    private void setRecyclerViewAdapterObserver(){
        habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), new Observer<List<Habits>>() {
            @Override
            public void onChanged(List<Habits> habits) {
                analyticParentItemAdapter.setNewHabitList(habits);
            }
        });
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().moveTaskToBack(true);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        userViewModel = null;
        habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().removeObservers(getViewLifecycleOwner());
        habitWithSubroutinesViewModel = null;
        binding = null;
    }
}