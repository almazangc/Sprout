package com.habitdev.sprout.ui.menu.setting.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.habitdev.sprout.R;
import com.habitdev.sprout.activity.startup.Main;
import com.habitdev.sprout.databinding.FragmentThemeBinding;

import java.util.HashSet;
import java.util.Set;

public class ThemeFragment extends Fragment {

    private FragmentThemeBinding binding;
    private static SharedPreferences sharedPreferences;
    private static final String THEME_SHARED_PREF_KEY = "THEME_SHARED.PREF";
    private static int theme = 0;

    public interface OnReturnSetting {
        void returnFromThemeToSetting();
    }

    private OnReturnSetting mOnReturnSetting;

    public void setmOnReturnSetting(OnReturnSetting mOnReturnSetting) {
        this.mOnReturnSetting = mOnReturnSetting;
    }

    public ThemeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentThemeBinding.inflate(inflater, container, false);

        final String SharedPreferences_KEY = "SP_DB";
        sharedPreferences = requireActivity().getSharedPreferences(SharedPreferences_KEY, Main.MODE_PRIVATE);

        setSelectedTheme();
        setApplyThemeListener();
        onBackPress();
        return binding.getRoot();
    }

    private void setSelectedTheme() {
        if (sharedPreferences.contains(THEME_SHARED_PREF_KEY)) {
            theme = sharedPreferences.getInt(THEME_SHARED_PREF_KEY, 0);
            switch (theme){
                case 1:
                    ((RadioButton) binding.themeRadioGroup.getChildAt(1)).setChecked(true);
                    break;
                case 2:
                    ((RadioButton) binding.themeRadioGroup.getChildAt(2)).setChecked(true);
                    break;
                default:
                    ((RadioButton) binding.themeRadioGroup.getChildAt(0)).setChecked(true);
                    break;
            }
        } else {
            ((RadioButton) binding.themeRadioGroup.getChildAt(0)).setChecked(true);
        }
    }

    private void setApplyThemeListener() {
        binding.setSelectedThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTheme = ((RadioButton)
                        (binding.getRoot().findViewById(binding.themeRadioGroup.getCheckedRadioButtonId())))
                        .getText()
                        .toString();

                switch (selectedTheme) {
                    case "As in the system":
                        if (theme != AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM){
                            new AlertDialog.Builder(requireContext())
                                    .setMessage("Do you want to set theme same as in the system?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                                        sharedPreferences.edit().putInt(THEME_SHARED_PREF_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM).apply();
                                        theme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                        break;
                    case "Light Theme":
                        if (theme != AppCompatDelegate.MODE_NIGHT_NO){
                            new AlertDialog.Builder(requireContext())
                                    .setMessage("Do you want to set to light theme?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                        sharedPreferences.edit().putInt(THEME_SHARED_PREF_KEY, AppCompatDelegate.MODE_NIGHT_NO).apply();
                                        theme = AppCompatDelegate.MODE_NIGHT_NO;
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                        break;
                    case "Dark Theme":
                        if (theme != AppCompatDelegate.MODE_NIGHT_YES){
                            new AlertDialog.Builder(requireContext())
                                    .setMessage("Do you want to set to dark theme?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                        sharedPreferences.edit().putInt(THEME_SHARED_PREF_KEY, AppCompatDelegate.MODE_NIGHT_YES).apply();
                                        theme = AppCompatDelegate.MODE_NIGHT_YES;
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                        break;
                }
            }
        });
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnReturnSetting != null) mOnReturnSetting.returnFromThemeToSetting();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}