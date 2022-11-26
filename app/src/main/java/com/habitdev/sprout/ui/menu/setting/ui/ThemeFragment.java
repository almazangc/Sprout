package com.habitdev.sprout.ui.menu.setting.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.habitdev.sprout.databinding.FragmentThemeBinding;
import com.habitdev.sprout.ui.menu.setting.SettingFragment;

public class ThemeFragment extends Fragment {

    private FragmentThemeBinding binding;

    public ThemeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentThemeBinding.inflate(inflater, container, false);
        onBackPress();
        return binding.getRoot();
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getChildFragmentManager();
                SettingFragment settingFragment = new SettingFragment();
                fragmentManager.beginTransaction().replace(binding.themeFrameLayout.getId(), settingFragment)
                        .commit();
                binding.themeContainer.setVisibility(View.GONE);
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