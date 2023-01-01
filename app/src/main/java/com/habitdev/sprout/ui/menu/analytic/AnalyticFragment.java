package com.habitdev.sprout.ui.menu.analytic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.database.user.model.User;
import com.habitdev.sprout.databinding.FragmentAnalyticBinding;
import com.habitdev.sprout.enums.AnalyticConfigurationKeys;
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
    private static boolean isOnItemClick;
    private static Habits habitOnClick;
    private static Bundle savedInstanceState;

    public AnalyticFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        analyticParentItemAdapter.setmOnItemClick(this);
        analyticItemOnClickFragment.setmOclickListener(this);
    }

    @Override
    public void analyticOnItemClick(int position) {
        isOnItemClick = true;
        habitOnClick = habitsList.get(position);
        analyticItemOnClickFragment.setHabit(habitOnClick);
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(AnalyticFragment.this.getTag())
                .replace(binding.analyticFrameLayout.getId(), analyticItemOnClickFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN)
                .commit();
        binding.analysisContainer.setVisibility(View.GONE);
    }

    @Override
    public void returnOnAnalytic() {
        try {
            getChildFragmentManager()
                    .beginTransaction()
                    .remove(analyticItemOnClickFragment)
                    .setTransition(FragmentTransaction.TRANSIT_NONE)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isOnItemClick = false;
        savedInstanceState = null;
        requireActivity().getSharedPreferences(AnalyticConfigurationKeys.ANALYTIC_SHAREDPREF.getKey(), Context.MODE_PRIVATE).edit().clear().apply();
        binding.analysisContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalyticBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        setDateSinceInstalledElapsedTime();
        setRecyclerViewAdapter();
        onBackPress();
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            AnalyticFragment.savedInstanceState = savedInstanceState;
        }
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            isOnItemClick = savedInstanceState.getBoolean(AnalyticConfigurationKeys.IS_ON_ITEM_CLICK.getKey());
            habitOnClick = (Habits) savedInstanceState.getSerializable(AnalyticConfigurationKeys.HABIT.getKey());

            analyticItemOnClickFragment.setHabit(habitOnClick);
            if (isOnItemClick) {
                getChildFragmentManager()
                        .beginTransaction()
                        .addToBackStack(AnalyticFragment.this.getTag())
                        .replace(binding.analyticFrameLayout.getId(), analyticItemOnClickFragment)
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .commit();
                binding.analysisContainer.setVisibility(View.GONE);
            }
        }
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
            setEmptyRVBackground();

            habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), new Observer<List<Habits>>() {
                @Override
                public void onChanged(List<Habits> habits) {
                    analyticParentItemAdapter.setNewHabitList(habits);
                    setEmptyRVBackground();
                }
            });
        }
    }

    private void setEmptyRVBackground() {
        if (AnalyticFragment.analyticParentItemAdapter.getItemCount() > 0) {
            Log.d("tag", "setEmptyRVBackground: " + AnalyticFragment.analyticParentItemAdapter.getItemCount());
            Log.d("tag", "setEmptyRVBackground: > 0");
            binding.analyticEmptyLottieRecyclerView.setVisibility(View.INVISIBLE);
            binding.analyticEmptyLbl.setVisibility(View.INVISIBLE);
        } else {
            Log.d("tag", "setEmptyRVBackground: empty");
            binding.analyticEmptyLottieRecyclerView.setVisibility(View.VISIBLE);
            binding.analyticEmptyLbl.setVisibility(View.VISIBLE);
        }
    }

    private void onBackPress() {
        final int[] keypress_count = {0};

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                keypress_count[0]++;

                //toast msg double backpress to close app not minimize

                new CountDownTimer(200, 200) {
                    @Override
                    public void onTick(long l) {}

                    @Override
                    public void onFinish() {
                        if (keypress_count[0] > 1) {
                            requireActivity().finishAndRemoveTask();
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("tag", "onSaveInstanceState: " + isOnItemClick);
        outState.putBoolean(AnalyticConfigurationKeys.IS_ON_ITEM_CLICK.getKey(), isOnItemClick);
        outState.putSerializable(AnalyticConfigurationKeys.HABIT.getKey(), habitOnClick);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(AnalyticConfigurationKeys.ANALYTIC_SHAREDPREF.getKey(), Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putBoolean(AnalyticConfigurationKeys.IS_ON_ITEM_CLICK.getKey(), isOnItemClick)
                .putString(AnalyticConfigurationKeys.HABIT.getKey(), new Gson().toJson(habitOnClick))
                .apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(AnalyticConfigurationKeys.ANALYTIC_SHAREDPREF.getKey(), Context.MODE_PRIVATE);
        if (!sharedPreferences.getAll().isEmpty()) {
            isOnItemClick = sharedPreferences.getBoolean(AnalyticConfigurationKeys.IS_ON_ITEM_CLICK.getKey(), false);
            habitOnClick = new Gson().fromJson(sharedPreferences.getString(AnalyticConfigurationKeys.HABIT.getKey(), null), Habits.class);
            analyticItemOnClickFragment.setHabit(habitOnClick);
            if (isOnItemClick) {
                getChildFragmentManager()
                        .beginTransaction()
                        .addToBackStack(AnalyticFragment.this.getTag())
                        .replace(binding.analyticFrameLayout.getId(), analyticItemOnClickFragment)
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .commit();
                binding.analysisContainer.setVisibility(View.GONE);
            } else {
                sharedPreferences.edit().clear().apply();
            }
        }
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