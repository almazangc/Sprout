package com.habitdev.sprout.ui.menu.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

import com.habitdev.sprout.database.achievement.AchievementViewModel;
import com.habitdev.sprout.database.achievement.model.Achievement;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.database.user.model.User;
import com.habitdev.sprout.databinding.FragmentSettingBinding;
import com.habitdev.sprout.enums.SettingConfigurationKeys;
import com.habitdev.sprout.ui.menu.OnBackPressDialogFragment;
import com.habitdev.sprout.ui.menu.home.HomeFragment;
import com.habitdev.sprout.ui.menu.journal.JournalFragment;
import com.habitdev.sprout.ui.menu.setting.ui.AboutUsFragment;
import com.habitdev.sprout.ui.menu.setting.ui.LearnMoreFragment;
import com.habitdev.sprout.ui.menu.setting.ui.ProfileFragment;
import com.habitdev.sprout.ui.menu.setting.ui.TechStackInfoFragment;
import com.habitdev.sprout.ui.menu.setting.ui.AchievementsFragment;
import com.habitdev.sprout.ui.menu.setting.ui.ThemeFragment;
import com.habitdev.sprout.utill.dialog.CompletedAchievementDiaglogFragment;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
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
        AchievementsFragment.OnReturnSetting {

    private FragmentSettingBinding binding;
    private UserViewModel userViewModel;

    private static final ProfileFragment PROFILE_FRAGMENT = new ProfileFragment();
    private static final ThemeFragment THEME_FRAGMENT = new ThemeFragment();
    private static final AboutUsFragment ABOUT_US_FRAGMENT = new AboutUsFragment();
    private static final LearnMoreFragment LEARN_MORE_FRAGMENT = new LearnMoreFragment();
    private static final TechStackInfoFragment TECH_STACK_INFO_FRAGMENT = new TechStackInfoFragment();
    private static final AchievementsFragment ACHIEVEMENTS_FRAGMENT = new AchievementsFragment();

    private static boolean isOnProfileTab, isOnThemeTab, isOnStackInfoTab, isOnLearnMoreInfoTab, isOnAboutUsTab, isOnAchievementsTab;

    private static User user;

    public SettingFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        PROFILE_FRAGMENT.setmOnReturnSetting(this);
        THEME_FRAGMENT.setmOnReturnSetting(this);
        ABOUT_US_FRAGMENT.setmOnReturnSetting(this);
        LEARN_MORE_FRAGMENT.setmOnReturnSetting(this);
        TECH_STACK_INFO_FRAGMENT.setmOnReturnSetting(this);
        ACHIEVEMENTS_FRAGMENT.setmOnReturnSetting(this);
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
            changeFragment(PROFILE_FRAGMENT);
        });

        binding.selectThemeBtn.setOnClickListener(view -> {
            isOnThemeTab = true;
            changeFragment(THEME_FRAGMENT);
        });

        binding.aboutUsBtn.setOnClickListener(view -> {
            isOnAboutUsTab = true;
            changeFragment(ABOUT_US_FRAGMENT);
        });

        binding.learnMoreBtn.setOnClickListener(view -> {
            isOnLearnMoreInfoTab = true;
            changeFragment(LEARN_MORE_FRAGMENT);

        });

        binding.techStackInfoBtn.setOnClickListener(view -> {
            isOnStackInfoTab = true;
            changeFragment(TECH_STACK_INFO_FRAGMENT);
        });

        binding.achievementsBtn.setOnClickListener(view -> {
            isOnAchievementsTab = true;
            changeFragment(ACHIEVEMENTS_FRAGMENT);
        });

        onBackPress();
        return binding.getRoot();
    }

    private void updateProfile(User user) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SettingConfigurationKeys.SETTING_SHAREDPRED.getKey(), Context.MODE_PRIVATE);

        if (!sharedPreferences.getAll().isEmpty()) {
            boolean onCustomProfile = sharedPreferences.getBoolean(SettingConfigurationKeys.IS_CUSTOM_PROFILE.getKey(), false);
            if (onCustomProfile) {
                String selectedProfilePath = sharedPreferences.getString(SettingConfigurationKeys.CUSTOM_PROFILE_PATH.getKey(), null);
                if (selectedProfilePath != null) {
                    Bitmap profile = BitmapFactory.decodeFile(selectedProfilePath);
                    if (profile != null) {
                        binding.settingImgView.setVisibility(View.VISIBLE);
                        binding.settingLottieAvatar.setVisibility(View.GONE);
                        binding.settingImgView.setImageBitmap(profile);
                    } else {
                        setDefaultProfile(user);
                    }
                } else {
                    setDefaultProfile(user);
                }
            }
        } else {
            setDefaultProfile(user);
        }
    }

    private void setDefaultProfile(User user) {
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

        binding.settingLottieAvatar.setVisibility(View.VISIBLE);
        binding.settingImgView.setVisibility(View.GONE);
        switch (identity != null ? identity : "Default") {
            case "Male":
                binding.settingLottieAvatar.setAnimation(default_male_profiles[new Random().nextInt(default_male_profiles.length)]);
                break;
            case "Female":
                binding.settingLottieAvatar.setAnimation(default_female_profiles[new Random().nextInt(default_female_profiles.length)]);
                break;
            case "Non-Binary":
            default:
                binding.settingLottieAvatar.setAnimation(default_non_binary_profiles[new Random().nextInt(default_non_binary_profiles.length)]);
                break;
        }
    }

    private void changeFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(SettingFragment.this.getTag())
                .add(binding.settingFrameLayout.getId(), fragment)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();
        binding.settingContainer.setVisibility(View.GONE);
        userViewModel = null;
    }

    @Override
    public void returnFromProfileToSetting() {
        returnFromFragmentToSetting(PROFILE_FRAGMENT);
        updateProfile(user);
        isOnProfileTab = false;
    }

    @Override
    public void returnFromThemeToSetting() {
        returnFromFragmentToSetting(THEME_FRAGMENT);
        isOnThemeTab = false;
    }

    @Override
    public void returnFromTechStackInfoToSetting() {
        returnFromFragmentToSetting(TECH_STACK_INFO_FRAGMENT);
        isOnStackInfoTab = false;
    }

    @Override
    public void returnFromLearnMoreToSetting() {
        returnFromFragmentToSetting(LEARN_MORE_FRAGMENT);
        isOnLearnMoreInfoTab = false;
    }

    @Override
    public void returnFromAboutUsToSetting() {
        returnFromFragmentToSetting(ABOUT_US_FRAGMENT);
        isOnAboutUsTab = false;
    }


    @Override
    public void returnFromTerminalToSetting() {
        returnFromFragmentToSetting(ACHIEVEMENTS_FRAGMENT);
        isOnAchievementsTab = false;
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
        final boolean[] isOnBackPressDialogShowing = {false};
        final boolean[] isAchievementDialogShowing = {false};

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                keypress_count[0]++;

                new CountDownTimer(200, 200) {
                    @Override
                    public void onTick(long l) {}

                    @Override
                    public void onFinish() {
                        if (keypress_count[0] > 1) {
                            //Dialog is displayed twice
                            OnBackPressDialogFragment dialog = new OnBackPressDialogFragment();
                            if (!isOnBackPressDialogShowing[0]) {
                                dialog.setTargetFragment(getChildFragmentManager().findFragmentById(SettingFragment.this.getId()), 2);
                                dialog.show(getChildFragmentManager(), "Menu.onBackPress");
                                dialog.setmOnCancelDialog(new OnBackPressDialogFragment.onCancelDialog() {
                                    @Override
                                    public void cancelDialog() {
                                        keypress_count[0] = 0;
                                        isOnBackPressDialogShowing[0] = false;
                                    }
                                });
                                isOnBackPressDialogShowing[0] = true;
                            }
                            if (!isAchievementDialogShowing[0]) {
                                //TODO: UPDATE UID WHEN APPDATABASE CHANGE
                                AchievementViewModel achievementViewModel = new ViewModelProvider(requireActivity()).get(AchievementViewModel.class);
                                Achievement CLOSEAPPPROMPT = achievementViewModel.getAchievementByUID(13);

                                if (!CLOSEAPPPROMPT.is_completed()) {
                                    CLOSEAPPPROMPT.setIs_completed(true);
                                    CLOSEAPPPROMPT.setCurrent_progress(CLOSEAPPPROMPT.getCurrent_progress()+1);
                                    CLOSEAPPPROMPT.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                                    CLOSEAPPPROMPT.setTitle("Close Application");
                                    CLOSEAPPPROMPT.setDescription("Unlocked by pressing back button twice");
                                    achievementViewModel.updateAchievement(CLOSEAPPPROMPT);
                                    CompletedAchievementDiaglogFragment completedAchievementDiaglogFragment = new CompletedAchievementDiaglogFragment(CLOSEAPPPROMPT.getTitle());
                                    completedAchievementDiaglogFragment.setTargetFragment(getChildFragmentManager()
                                            .findFragmentById(SettingFragment.this.getId()), 1);
                                    completedAchievementDiaglogFragment.show(getChildFragmentManager(), "CompletedAchievementDiaglog");
                                    isAchievementDialogShowing[0] = true;
                                }
                            }
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
            if (!PROFILE_FRAGMENT.isAdded()) {
                changeFragment(PROFILE_FRAGMENT);
            }
        }

        if (isOnThemeTab) {
            if (!THEME_FRAGMENT.isAdded()) {
                changeFragment(THEME_FRAGMENT);
            }
        }

        if (isOnStackInfoTab) {
            if (!TECH_STACK_INFO_FRAGMENT.isAdded()) {
                changeFragment(TECH_STACK_INFO_FRAGMENT);
            }
        }

        if (isOnLearnMoreInfoTab) {
            if (!LEARN_MORE_FRAGMENT.isAdded()) {
                changeFragment(LEARN_MORE_FRAGMENT);
            }
        }

        if (isOnAboutUsTab) {
            if (!ABOUT_US_FRAGMENT.isAdded()) {
                changeFragment(ABOUT_US_FRAGMENT);
            }
        }

        if (isOnAchievementsTab) {
            if (!ACHIEVEMENTS_FRAGMENT.isAdded()) {
                changeFragment(ACHIEVEMENTS_FRAGMENT);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        PROFILE_FRAGMENT.setmOnReturnSetting(null);
        THEME_FRAGMENT.setmOnReturnSetting(null);
        ABOUT_US_FRAGMENT.setmOnReturnSetting(null);
        LEARN_MORE_FRAGMENT.setmOnReturnSetting(null);
        TECH_STACK_INFO_FRAGMENT.setmOnReturnSetting(null);
        ACHIEVEMENTS_FRAGMENT.setmOnReturnSetting(null);
    }
}