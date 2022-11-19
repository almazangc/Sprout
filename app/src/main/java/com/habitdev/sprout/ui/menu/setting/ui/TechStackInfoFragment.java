package com.habitdev.sprout.ui.menu.setting.ui;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.FragmentAboutUsBinding;
import com.habitdev.sprout.databinding.FragmentTechStackInfoBinding;
import com.habitdev.sprout.ui.menu.setting.SettingFragment;

public class TechStackInfoFragment extends Fragment {

    private FragmentTechStackInfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechStackInfoBinding.inflate(inflater, container, false);
        onBackPress();
        return binding.getRoot();
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SettingFragment fragment = new SettingFragment();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().replace(binding.techStackInfoFrameLayout.getId(), fragment)
                        .commit();
                binding.techStackInfoContainer.setVisibility(View.GONE);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}