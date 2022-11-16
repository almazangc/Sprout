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
import com.prototype.sprout.ui.menu.setting.ui.AboutUsFragment;
import com.prototype.sprout.ui.menu.setting.ui.LearnMoreFragment;
import com.prototype.sprout.ui.menu.setting.ui.TechStackInfoFragment;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        fragmentManager = getChildFragmentManager();

        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUserNickname().observe(getViewLifecycleOwner(), nickname -> {
            binding.nickname.setText(nickname);
        });

        goToAboutUs();
        goToLearnMore();
        goToTechStack();

        onBackPress();

        return binding.getRoot();
    }

    private void goToAboutUs() {
        binding.aboutUsBtn.setOnClickListener(view -> {
            AboutUsFragment fragment = new AboutUsFragment();
            changeFragment(fragment);
        });
    }

    private void goToLearnMore() {
        binding.learnMoreBtn.setOnClickListener(view -> {
            LearnMoreFragment fragment = new LearnMoreFragment();
            changeFragment(fragment);
        });
    }

    private void goToTechStack() {
        binding.techStackInfoBtn.setOnClickListener(view -> {
            TechStackInfoFragment fragment = new TechStackInfoFragment();
            changeFragment(fragment);
        });
    }

    private void changeFragment(Fragment newFragment) {
        fragmentManager.beginTransaction().replace(binding.settingsFrameLayout.getId(), newFragment)
                .commit();
        binding.settingsContainer.setVisibility(View.GONE);
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
        binding = null;
    }
}