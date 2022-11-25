package com.habitdev.sprout.ui.menu.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.habitdev.sprout.databinding.FragmentHomeItemOnClickViewBinding;
import com.habitdev.sprout.ui.menu.home.HomeFragment;

public class HomeItemOnClickFragment extends Fragment {

    private FragmentHomeItemOnClickViewBinding binding;

    public HomeItemOnClickFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeItemOnClickViewBinding.inflate(inflater, container, false);
        onBackPress();
        return binding.getRoot();
    }


    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Returns to Home
                HomeFragment homeFragment = new HomeFragment();
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(binding.homeItemOnClickFrameLayout.getId(), homeFragment)
                        .commit();
                binding.homeItemOnClickContainer.setVisibility(View.GONE);
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