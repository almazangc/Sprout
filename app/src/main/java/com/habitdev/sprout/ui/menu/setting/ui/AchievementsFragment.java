package com.habitdev.sprout.ui.menu.setting.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.database.achievement.AchievementViewModel;
import com.habitdev.sprout.database.achievement.model.Achievement;
import com.habitdev.sprout.database.habit.model.room.Subroutines;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.note.NoteViewModel;
import com.habitdev.sprout.databinding.FragmentAchievementsBinding;
import com.habitdev.sprout.ui.menu.analytic.adapter.AnalyticItemOnClickParentItemAdapter;
import com.habitdev.sprout.ui.menu.setting.adapter.SettingAchievementParentItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class AchievementsFragment extends Fragment {

    private FragmentAchievementsBinding binding;
    private AchievementViewModel achievementViewModel;
    private static final SettingAchievementParentItemAdapter settingAchievementParentItemAdapter = new SettingAchievementParentItemAdapter();

    public interface OnReturnSetting {
        void returnFromTerminalToSetting();
    }

    private OnReturnSetting mOnReturnSetting;

    public void setmOnReturnSetting(OnReturnSetting mOnReturnSetting) {
        this.mOnReturnSetting = mOnReturnSetting;
    }

    public AchievementsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAchievementsBinding.inflate(inflater, container, false);
        achievementViewModel = new ViewModelProvider(requireActivity()).get(AchievementViewModel.class);
        setAchievementGeneralInfo();
        setRecyclerViewAdapter();
        onBackPress();
        return binding.getRoot();
    }

    private void setAchievementGeneralInfo() {
        int totalAchievementCount = achievementViewModel.getTotalAchievementsCount();
        binding.achievementGeneralOverviewPieGraph.setMax(totalAchievementCount);
        binding.achievementGeneralTotalAchievement.setText(String.valueOf(totalAchievementCount));

        achievementViewModel.getCompletedAchievementsCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer completedAchievementCount) {
                binding.achievementGeneralTotalCompletedAchievement.setText(String.valueOf(completedAchievementCount));
                binding.achievementGeneralOverviewPieGraph.setProgress(completedAchievementCount, true);
            }
        });
    }


    private void setRecyclerViewAdapter() {
        if (binding.achievementRecyclerview.getAdapter() == null) {

            settingAchievementParentItemAdapter.setOldAchievementList(achievementViewModel.getAllAchievementList());
            binding.achievementRecyclerview.setAdapter(settingAchievementParentItemAdapter);

            achievementViewModel.getAllAcheivementLiveDataList().observe(getViewLifecycleOwner(), new Observer<List<Achievement>>() {
                @Override
                public void onChanged(List<Achievement> achievements) {
                    settingAchievementParentItemAdapter.setNewAchievementList(achievements);
                }
            });
        }
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnReturnSetting != null) {
                    mOnReturnSetting.returnFromTerminalToSetting();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}