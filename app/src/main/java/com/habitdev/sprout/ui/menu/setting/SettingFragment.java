package com.habitdev.sprout.ui.menu.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
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
import com.habitdev.sprout.database.user.model.User;
import com.habitdev.sprout.databinding.FragmentSettingBinding;
import com.habitdev.sprout.enums.SettingConfigurationKeys;
import com.habitdev.sprout.ui.menu.setting.ui.AboutUsFragment;
import com.habitdev.sprout.ui.menu.setting.ui.LearnMoreFragment;
import com.habitdev.sprout.ui.menu.setting.ui.ProfileFragment;
import com.habitdev.sprout.ui.menu.setting.ui.TechStackInfoFragment;
import com.habitdev.sprout.ui.menu.setting.ui.TerminalFragment;
import com.habitdev.sprout.ui.menu.setting.ui.ThemeFragment;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

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

    private static boolean isOnProfileTab, isOnThemeTab, isOnStackInfoTab, isOnLearnMoreInfoTab, isOnAboutUsTab, isOnTerminalTab;

    private static User user;

    public SettingFragment() {}

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

        user = userViewModel.getUserByUID(1);

        updateProfile(user);

        binding.editProfile.setOnClickListener(view -> {
            isOnProfileTab = true;
            changeFragment(profileFragment);
        });

        binding.selectThemeBtn.setOnClickListener(view -> {
            isOnThemeTab = true;
            changeFragment(themeFragment);
        });

        binding.aboutUsBtn.setOnClickListener(view -> {
            isOnAboutUsTab = true;
            changeFragment(aboutUsFragment);
        });

        binding.learnMoreBtn.setOnClickListener(view -> {
            isOnLearnMoreInfoTab = true;
            changeFragment(learnMoreFragment);

        });

        binding.techStackInfoBtn.setOnClickListener(view -> {
            isOnStackInfoTab = true;
            changeFragment(techStackInfoFragment);
        });

        binding.terminalBtn.setOnClickListener(view -> {
            isOnTerminalTab = true;
            changeFragment(terminalFragment);
        });

        onBackPress();
        return binding.getRoot();
    }

    private void updateProfile(User user) {
        final String[] default_male_profiles = {
                "default_user_profile_male-avatar.json",
                "default_user_profile_male-avatar-v1.json",
                "default_user_profile_male-avatar-v2.json",
                "default_user_profile_male-avatar-v3.json",
                "default_user_profile_male-avatar-v4.json"
        };

        final String[] default_female_profiles = {
                "default_user_profile_female-avatar.json",
                "default_user_profile_female-avatar-v1.json",
                "default_user_profile_female-avatar-v2.json",
                "default_user_profile_female-avatar-v3.json",
                "default_user_profile_female-avatar-v4.json",
                "default_user_profile_female-avatar-v5.json"
        };

        final String[] default_non_binary_profiles = Stream.concat(Arrays.stream(default_male_profiles), Arrays.stream(default_female_profiles)).toArray(String[]::new);

        String identity = user.getIdentity();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SettingConfigurationKeys.SETTING_SHAREDPRED.getKey(), Context.MODE_PRIVATE);

        if (!sharedPreferences.getAll().isEmpty()) {
            boolean onCustomProfile = sharedPreferences.getBoolean(SettingConfigurationKeys.IS_CUSTOM_PROFILE.getKey(), false);
            if (onCustomProfile) {

                String selectedProfilePath = sharedPreferences.getString(SettingConfigurationKeys.CUSTOM_PROFILE_PATH.getKey(), null);
                if (selectedProfilePath != null) {
                    binding.settingImgView.setVisibility(View.VISIBLE);
                    binding.settingLottieAvatar.setVisibility(View.GONE);
                    binding.settingImgView.setImageBitmap(BitmapFactory.decodeFile(selectedProfilePath));
                } else {
                    binding.settingLottieAvatar.setVisibility(View.VISIBLE);
                    binding.settingImgView.setVisibility(View.GONE);
                    switch (identity != null ? identity: "Default") {
                        case "Male":
                            binding.settingLottieAvatar.setAnimation(default_male_profiles[new Random().nextInt(default_male_profiles.length)]);
                            break;
                        case "Female":
                            binding.settingLottieAvatar.setAnimation(default_female_profiles[new Random().nextInt(default_female_profiles.length)]);
                            break;
                        default:
                            binding.settingLottieAvatar.setAnimation(default_non_binary_profiles[new Random().nextInt(default_non_binary_profiles.length)]);
                            break;
                    }
                }
            }
        } else {
            binding.settingLottieAvatar.setVisibility(View.VISIBLE);
            binding.settingImgView.setVisibility(View.GONE);
            switch (identity != null ? identity: "Default") {
                case "Male":
                    binding.settingLottieAvatar.setAnimation(default_male_profiles[new Random().nextInt(default_male_profiles.length)]);
                    break;
                case "Female":
                    binding.settingLottieAvatar.setAnimation(default_female_profiles[new Random().nextInt(default_female_profiles.length)]);
                    break;
                default:
                    binding.settingLottieAvatar.setAnimation(default_non_binary_profiles[new Random().nextInt(default_non_binary_profiles.length)]);
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //restore which tab was last selected
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
        updateProfile(user);
        isOnProfileTab = false;
    }

    @Override
    public void returnFromThemeToSetting() {
        returnFromFragmentToSetting(themeFragment);
        isOnThemeTab = false;
    }

    @Override
    public void returnFromTechStackInfoToSetting() {
        returnFromFragmentToSetting(techStackInfoFragment);
        isOnStackInfoTab = false;
    }

    @Override
    public void returnFromLearnMoreToSetting() {
        returnFromFragmentToSetting(learnMoreFragment);
        isOnLearnMoreInfoTab = false;
    }

    @Override
    public void returnFromAboutUsToSetting() {
        returnFromFragmentToSetting(aboutUsFragment);
        isOnAboutUsTab = false;
    }


    @Override
    public void returnFromTerminalToSetting() {
        returnFromFragmentToSetting(terminalFragment);
        isOnTerminalTab = false;
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
                    public void onTick(long l) {
                    }

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
    public void onResume() {
        super.onResume();
        //restore which tab
        if (isOnProfileTab) {
            if(!profileFragment.isAdded()) {
                changeFragment(profileFragment);
            }
        }

        if (isOnThemeTab) {
            if (!themeFragment.isAdded()) {
                changeFragment(themeFragment);
            }
        }

        if (isOnStackInfoTab) {
            if (!techStackInfoFragment.isAdded()) {
                changeFragment(techStackInfoFragment);
            }
        }

        if (isOnLearnMoreInfoTab) {
            if (!learnMoreFragment.isAdded()) {
                changeFragment(learnMoreFragment);
            }
        }

        if (isOnAboutUsTab) {
            if (!aboutUsFragment.isAdded()) {
                changeFragment(aboutUsFragment);
            }
        }

        if (isOnTerminalTab) {
            if (!terminalFragment.isAdded()) {
                changeFragment(terminalFragment);
            }
        }
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