package com.habitdev.sprout.ui.menu.setting.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.habitdev.sprout.activity.startup.Main;
import com.habitdev.sprout.databinding.FragmentThemeBinding;

public class ThemeFragment extends Fragment {

    private FragmentThemeBinding binding;
    private SharedPreferences sharedPreferences;
    private final String THEME_SHARED_PREF_KEY = "THEME";

    public interface onReturnSetting{
        void returnFromThemeToSetting();
    }

    private onReturnSetting mOnReturnSetting;

    public void setmOnReturnSetting(onReturnSetting mOnReturnSetting) {
        this.mOnReturnSetting = mOnReturnSetting;
    }

    public ThemeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentThemeBinding.inflate(inflater, container, false);

        final String SharedPreferences_KEY = "SP_DB";
        sharedPreferences = requireActivity().getSharedPreferences(SharedPreferences_KEY, Main.MODE_PRIVATE);

        if (sharedPreferences.contains(THEME_SHARED_PREF_KEY)) {
            int theme = sharedPreferences.getInt(THEME_SHARED_PREF_KEY, 0);
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
        }

        binding.setSelectedThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedTheme = ((RadioButton)
                        (binding.getRoot().findViewById(binding.themeRadioGroup.getCheckedRadioButtonId())))
                        .getText()
                        .toString();

                switch (selectedTheme) {
                    case "As in the system":
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        sharedPreferences.edit().putInt(THEME_SHARED_PREF_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM).apply();
                        break;
                    case "Light Theme":
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        sharedPreferences.edit().putInt(THEME_SHARED_PREF_KEY, AppCompatDelegate.MODE_NIGHT_NO).apply();
                        break;
                    case "Dark Theme":
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        sharedPreferences.edit().putInt(THEME_SHARED_PREF_KEY, AppCompatDelegate.MODE_NIGHT_YES).apply();
                        break;
                }
            }
        });
        onBackPress();
        return binding.getRoot();
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