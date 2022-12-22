package com.habitdev.sprout.ui.menu.analytic;

import android.content.Context;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.database.user.model.User;
import com.habitdev.sprout.databinding.FragmentAnalyticBinding;
import com.habitdev.sprout.ui.menu.analytic.adapter.AnalyticParentItemAdapter;
import com.habitdev.sprout.ui.menu.analytic.ui.AnalyticItemOnClickFragment;
import com.habitdev.sprout.utill.DateTimeElapsedUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AnalyticFragment extends Fragment
        implements
        AnalyticParentItemAdapter.onItemClick,
        AnalyticItemOnClickFragment.OnclickListener {

    private FragmentAnalyticBinding binding;
    private static UserViewModel userViewModel;
    private static HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private static List<Habits> habitsList = new ArrayList<>();
    private static final AnalyticParentItemAdapter analyticParentItemAdapter = new AnalyticParentItemAdapter();
    private static final AnalyticItemOnClickFragment analyticItemOnClickFragment = new AnalyticItemOnClickFragment();

    public AnalyticFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        analyticParentItemAdapter.setmOnItemClick(this);
        analyticItemOnClickFragment.setmOclickListener(this);
    }

    @Override
    public void analyticOnItemClick(int position) {
//        show(position); Snackbar
        analyticItemOnClickFragment.setHabit(habitsList.get(position));
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(AnalyticFragment.this.getTag())
                .replace(binding.analyticFrameLayout.getId(), analyticItemOnClickFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN)
                .commit();
        binding.analysisContainer.setVisibility(View.GONE);
    }

    @Override
    public void setAnalyticParentItemOnclickListener() {
        try {
            getChildFragmentManager()
                    .beginTransaction()
                    .remove(analyticItemOnClickFragment)
                    .setTransition(FragmentTransaction.TRANSIT_NONE)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.analysisContainer.setVisibility(View.VISIBLE);
    }

    private void show(int position) {
        Snackbar.make(binding.getRoot(), habitsList.get(position).getHabit(), Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //auto dismiss when clicked, single action
                    }
                })
                .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER))
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.NIGHT))
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.CLOUDS))
                .show();
    }

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

    private void setDateSinceInstalledElapsedTime() {
        User user = userViewModel.getUserByUID(1);
        String date = user.getDateInstalled();
        DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(date);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (binding != null) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        dateTimeElapsedUtil.calculateElapsedDateTime();
                        try {
                            binding.dateSinceInstalled.setText(dateTimeElapsedUtil.getResult());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    timer.cancel();
                    timer.purge();
                }
            }
        }, 0, 1000);
    }

    private void setRecyclerViewAdapter() {
        if (binding.analyticHabitOnReformRecyclerView.getAdapter() == null) {
            habitsList = habitWithSubroutinesViewModel.getAllHabitOnReform();
            analyticParentItemAdapter.setOldHabitsList(habitsList);
            analyticParentItemAdapter.setHabitWithSubroutinesViewModel(habitWithSubroutinesViewModel);
            analyticParentItemAdapter.setmOnItemClick(this);
            binding.analyticHabitOnReformRecyclerView.setAdapter(analyticParentItemAdapter);
            habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), analyticParentItemAdapter::setNewHabitList);
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        analyticParentItemAdapter.setmOnItemClick(null);
        analyticItemOnClickFragment.setmOclickListener(null);
    }
}