package com.prototype.sprout.ui.menu.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.prototype.sprout.database.user.UserViewModel;
import com.prototype.sprout.databinding.FragmentSettingBinding;
import com.prototype.sprout.ui.menu.home.ui.AddDefaultHabitFragment;
import com.prototype.sprout.ui.menu.setting.ui.AboutUsFragment;
import com.prototype.sprout.ui.menu.setting.ui.LearnMoreFragment;
import com.prototype.sprout.ui.menu.setting.ui.TechStackInfoFragment;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    private FragmentManager fragmentManager;
    private UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUserNickname().observe(getViewLifecycleOwner(), nickname -> {
            binding.nickname.setText(nickname);
        });

        fragmentManager = getChildFragmentManager();

        binding.aboutUsBtn.setOnClickListener(v -> {
            AboutUsFragment aboutUs = new AboutUsFragment();
            fragmentManager.beginTransaction()
                    .replace(binding.settingFrameLayout.getId(), aboutUs)
                    .commit();
            binding.settingContainer.setVisibility(View.GONE);
        });

        binding.learnMoreBtn.setOnClickListener(v -> {
            LearnMoreFragment learnMore = new LearnMoreFragment();
            fragmentManager.beginTransaction()
                    .replace(binding.settingFrameLayout.getId(), learnMore)
                    .commit();
            binding.settingContainer.setVisibility(View.GONE);

        });

        binding.techStackInfoBtn.setOnClickListener(v -> {
            TechStackInfoFragment techStackInfo = new TechStackInfoFragment();
            fragmentManager.beginTransaction()
                    .replace(binding.settingFrameLayout.getId(), techStackInfo)
                    .commit();
            binding.settingContainer.setVisibility(View.GONE);
        });

        onBackPress();
        return binding.getRoot();
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Do Something
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentManager = null;
        userViewModel = null;
        binding = null;
    }
}