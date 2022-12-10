package com.habitdev.sprout.ui.menu.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentSettingBinding;
import com.habitdev.sprout.ui.menu.setting.ui.AboutUsFragment;
import com.habitdev.sprout.ui.menu.setting.ui.LearnMoreFragment;
import com.habitdev.sprout.ui.menu.setting.ui.ProfileFragment;
import com.habitdev.sprout.ui.menu.setting.ui.TechStackInfoFragment;
import com.habitdev.sprout.ui.menu.setting.ui.TerminalFragment;
import com.habitdev.sprout.ui.menu.setting.ui.ThemeFragment;

public class SettingFragment extends Fragment implements
        ProfileFragment.onReturnSetting,
        ThemeFragment.onReturnSetting,
        AboutUsFragment.onReturnSetting,
        LearnMoreFragment.onReturnSetting,
        TechStackInfoFragment.onReturnSetting,
        TerminalFragment.onReturnSetting{

    private FragmentSettingBinding binding;
    private UserViewModel userViewModel;

    protected ProfileFragment profileFragment = new ProfileFragment();
    protected ThemeFragment themeFragment = new ThemeFragment();
    protected AboutUsFragment aboutUsFragment = new AboutUsFragment();
    protected LearnMoreFragment learnMoreFragment = new LearnMoreFragment();
    protected TechStackInfoFragment techStackInfoFragment = new TechStackInfoFragment();
    protected TerminalFragment terminalFragment = new TerminalFragment();

    public SettingFragment() {
        profileFragment.setmOnReturnSetting(this);
        themeFragment.setmOnReturnSetting(this);
        aboutUsFragment.setmOnReturnSetting(this);
        learnMoreFragment.setmOnReturnSetting(this);
        techStackInfoFragment.setmOnReturnSetting(this);
        terminalFragment.setmOnReturnSetting(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserNickname().observe(getViewLifecycleOwner(), nickname -> {
            binding.nickname.setText(nickname);
        });

        binding.editProfile.setOnClickListener(v -> {
            changeFragment(profileFragment);
        });

        binding.selectThemeBtn.setOnClickListener(v -> {
            changeFragment(themeFragment);
        });

        binding.aboutUsBtn.setOnClickListener(v -> {
            changeFragment(aboutUsFragment);
        });

        binding.learnMoreBtn.setOnClickListener(v -> {
            changeFragment(learnMoreFragment);

        });

        binding.techStackInfoBtn.setOnClickListener(v -> {
            changeFragment(techStackInfoFragment);
        });

        binding.terminalBtn.setOnClickListener(view -> {
            changeFragment(terminalFragment);
        });

        onBackPress();
        return binding.getRoot();
    }

    private void changeFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(SettingFragment.this.getTag())
                .add(binding.settingFrameLayout.getId(), fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN)
                .commit();
        binding.settingContainer.setVisibility(View.GONE);
        userViewModel = null;
    }

    @Override
    public void returnFromProfileToSetting() {
        returnFromFragmentToSetting(profileFragment);
    }

    @Override
    public void returnFromThemeToSetting() {
        returnFromFragmentToSetting(themeFragment);
    }

    @Override
    public void returnFromAboutUsToSetting() {
        returnFromFragmentToSetting(aboutUsFragment);
    }

    @Override
    public void returnFromLearnMoreToSetting() {
        returnFromFragmentToSetting(learnMoreFragment);
    }

    @Override
    public void returnFromTechStackInfoToSetting() {
        returnFromFragmentToSetting(techStackInfoFragment);
    }

    @Override
    public void returnFromTerminalToSetting() {
        returnFromFragmentToSetting(terminalFragment);
    }

    private void returnFromFragmentToSetting(Fragment fragment){
        getChildFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        binding.settingContainer.setVisibility(View.VISIBLE);
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
        if (userViewModel != null) {
            userViewModel.getUserNickname().removeObservers(getViewLifecycleOwner());
            userViewModel = null;
        }
        binding = null;
    }
}