package com.prototype.sprout.ui.menu.setting.ui;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prototype.sprout.R;
import com.prototype.sprout.databinding.FragmentAboutUsBinding;
import com.prototype.sprout.databinding.FragmentSettingBinding;
import com.prototype.sprout.ui.menu.setting.SettingFragment;

public class AboutUsFragment extends Fragment {

    private FragmentAboutUsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false);
        onBackPress();
        return binding.getRoot();
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SettingFragment fragment = new SettingFragment();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().replace(binding.aboutUsFrameLayout.getId(), fragment)
                        .commit();
                binding.aboutUsContainer.setVisibility(View.GONE);
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