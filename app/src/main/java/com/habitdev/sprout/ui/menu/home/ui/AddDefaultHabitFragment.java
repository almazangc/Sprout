package com.habitdev.sprout.ui.menu.home.ui;

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
import com.habitdev.sprout.databinding.FragmentAddDefaultHabitBinding;
import com.habitdev.sprout.databinding.FragmentHomeBinding;
import com.habitdev.sprout.ui.menu.home.HomeFragment;

public class AddDefaultHabitFragment extends Fragment {

    private FragmentAddDefaultHabitBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddDefaultHabitBinding.inflate(inflater, container, false);

        onBackPress();

        return binding.getRoot();
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().replace(binding.addFromDefaultHabitFrameLayout.getId(), new HomeFragment())
                        .commit();
                binding.addFromDefaultHabitContainer.setVisibility(View.GONE);
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