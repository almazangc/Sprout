package com.habitdev.sprout.ui.menu.setting;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
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

/**
 *
 */
public class SettingFragment extends Fragment implements
        ProfileFragment.OnReturnSetting,
        ThemeFragment.OnReturnSetting,
        AboutUsFragment.OnReturnSetting,
        LearnMoreFragment.OnReturnSetting,
        TechStackInfoFragment.OnReturnSetting,
        TerminalFragment.OnReturnSetting {

    private FragmentSettingBinding binding;
    private UserViewModel userViewModel;

    private static final ProfileFragment profileFragment = new ProfileFragment();
    private static final ThemeFragment themeFragment = new ThemeFragment();
    private static final AboutUsFragment aboutUsFragment = new AboutUsFragment();
    private static final LearnMoreFragment learnMoreFragment = new LearnMoreFragment();
    private static final TechStackInfoFragment techStackInfoFragment = new TechStackInfoFragment();
    private static final TerminalFragment terminalFragment = new TerminalFragment();

    public SettingFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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

        binding.editProfile.setOnClickListener(view -> {
            changeFragment(profileFragment);
        });

        binding.selectThemeBtn.setOnClickListener(view -> {
            changeFragment(themeFragment);
        });

        binding.aboutUsBtn.setOnClickListener(view -> {
            changeFragment(aboutUsFragment);
        });

        binding.learnMoreBtn.setOnClickListener(view -> {
            changeFragment(learnMoreFragment);

        });

        binding.techStackInfoBtn.setOnClickListener(view -> {
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

    private void returnFromFragmentToSetting(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        binding.settingContainer.setVisibility(View.VISIBLE);
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
    public void onDestroyView() {
        super.onDestroyView();
        if (userViewModel != null) {
            userViewModel.getUserNickname().removeObservers(getViewLifecycleOwner());
            userViewModel = null;
        }
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        profileFragment.setmOnReturnSetting(null);
        themeFragment.setmOnReturnSetting(null);
        aboutUsFragment.setmOnReturnSetting(null);
        learnMoreFragment.setmOnReturnSetting(null);
        techStackInfoFragment.setmOnReturnSetting(null);
        terminalFragment.setmOnReturnSetting(null);
    }
}