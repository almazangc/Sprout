package com.habitdev.sprout.ui.menu.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentSettingBinding;
import com.habitdev.sprout.ui.menu.setting.ui.AboutUsFragment;
import com.habitdev.sprout.ui.menu.setting.ui.LearnMoreFragment;
import com.habitdev.sprout.ui.menu.setting.ui.ProfileFragment;
import com.habitdev.sprout.ui.menu.setting.ui.TechStackInfoFragment;
import com.habitdev.sprout.ui.menu.setting.ui.TerminalFragment;
import com.habitdev.sprout.ui.menu.setting.ui.ThemeFragment;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    private FragmentManager fragmentManager;
    private UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserNickname().observe(getViewLifecycleOwner(), nickname -> {
            binding.nickname.setText(nickname);
        });

        fragmentManager = getChildFragmentManager();

        binding.editProfile.setOnClickListener(v -> {
            changeFragment(new ProfileFragment());
        });

        binding.selectThemeBtn.setOnClickListener(v -> {
            changeFragment(new ThemeFragment());
        });

        binding.aboutUsBtn.setOnClickListener(v -> {
            changeFragment(new AboutUsFragment());
        });

        binding.learnMoreBtn.setOnClickListener(v -> {
            changeFragment(new LearnMoreFragment());

        });

        binding.techStackInfoBtn.setOnClickListener(v -> {
            changeFragment(new TechStackInfoFragment());
        });

        binding.terminalBtn.setOnClickListener(view -> {
            changeFragment(new TerminalFragment());
        });

        onBackPress();
        return binding.getRoot();
    }

    private void changeFragment(Fragment fragment){
        fragmentManager.beginTransaction()
                .replace(binding.settingFrameLayout.getId(), fragment)
                .commit();
        binding.settingContainer.setVisibility(View.GONE);
        userViewModel.getUserNickname().removeObservers(getViewLifecycleOwner());
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
        userViewModel = null;
        binding = null;
    }
}