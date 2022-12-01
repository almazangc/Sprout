package com.habitdev.sprout.ui.menu.home.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.databinding.DialogFragmentHomeParentItemAdapterModifyBinding;

import java.util.Objects;

public class HomeParentItemAdapterModifyDialogFragment extends DialogFragment {
    private DialogFragmentHomeParentItemAdapterModifyBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private Habits habitOnModify;

    public HomeParentItemAdapterModifyDialogFragment() {

    }

    public HomeParentItemAdapterModifyDialogFragment(HabitWithSubroutinesViewModel habitWithSubroutinesViewModel, Habits habitOnModify) {
        this.habitWithSubroutinesViewModel = habitWithSubroutinesViewModel;
        this.habitOnModify = habitOnModify;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFragmentHomeParentItemAdapterModifyBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.background_dialog_transparent);
        binding.homeParentItemAdapterModifyTitle.setText(habitOnModify.getHabit());
        binding.homeParentItemAdapterModifyDescription.setText(habitOnModify.getDescription());
        onUpdate();
        onCancel();
        return binding.getRoot();
    }

    private void onCancel() {
        binding.homeParentItemAdapterModifyCancel.setOnClickListener(view -> dismiss());
    }

    private void onUpdate() {
        binding.homeParentItemAdapterModifyUpdate.setOnClickListener(view -> {
            habitOnModify.setHabit(binding.homeParentItemAdapterModifyTitle.getText().toString().trim());
            habitOnModify.setDescription(binding.homeParentItemAdapterModifyDescription.getText().toString().trim());
            habitWithSubroutinesViewModel.update(habitOnModify);
            Objects.requireNonNull(getDialog()).dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}