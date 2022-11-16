package com.prototype.sprout.ui.menu.subroutine.ui;

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
import com.prototype.sprout.database.habits_with_subroutines.Subroutines;
import com.prototype.sprout.databinding.FragmentAddNewSubroutineBinding;
import com.prototype.sprout.ui.menu.setting.SettingFragment;
import com.prototype.sprout.ui.menu.subroutine.SubroutineFragment;

public class AddNewSubroutineFragment extends Fragment {

   private FragmentAddNewSubroutineBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddNewSubroutineBinding.inflate(inflater, container, false);

        onBackPress();

        return binding.getRoot();
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SubroutineFragment subroutineFragment = new SubroutineFragment();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(binding.addNewSubroutineFrameLayout.getId(), subroutineFragment)
                        .commit();
                binding.addNewSubroutineContainer.setVisibility(View.GONE);
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