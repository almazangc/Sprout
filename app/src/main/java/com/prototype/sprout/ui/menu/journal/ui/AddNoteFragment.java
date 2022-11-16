package com.prototype.sprout.ui.menu.journal.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.prototype.sprout.databinding.FragmentAddNewNoteBinding;
import com.prototype.sprout.ui.menu.subroutine.SubroutineFragment;

public class AddNoteFragment extends Fragment {

    private FragmentAddNewNoteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddNewNoteBinding.inflate(inflater, container, false);
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
                        .replace(binding.addNewNoteFrameLayout.getId(), subroutineFragment)
                        .commit();
                binding.addNewNoteContainer.setVisibility(View.GONE);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}