package com.habitdev.sprout.ui.menu.home.ui.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.databinding.DialogFragmentHomeParentItemAdapterModifyBinding;
import com.habitdev.sprout.ui.menu.home.adapter.HomeParentItemAdapter;

import java.util.Objects;

public class HomeParentItemAdapterModifyDialogFragment extends DialogFragment {
    private DialogFragmentHomeParentItemAdapterModifyBinding binding;
    private Habits habitOnModify;
    private final int position;
    private final String habit_title_snapshot;
    private final String habit_description_snapshot;

    private HomeParentItemAdapter adapter_ref;

    public void setAdapter_ref(HomeParentItemAdapter adapter_ref) {
        this.adapter_ref = adapter_ref;
    }

    public HomeParentItemAdapterModifyDialogFragment(Habits habitOnModify, int position) {
        this.habitOnModify = habitOnModify;
        this.habit_title_snapshot = habitOnModify.getHabit();
        this.habit_description_snapshot = habitOnModify.getDescription();
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFragmentHomeParentItemAdapterModifyBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.background_color_transparent);
        binding.homeParentItemAdapterModifyTitle.setText(habitOnModify.getHabit());
        binding.homeParentItemAdapterModifyDescription.setText(habitOnModify.getDescription());
        setUpdateVisibility();
        validateHabit();
        onUpdate();
        onCancel();

        return binding.getRoot();
    }

    private void validateHabit(){
        final String REQUIRED = "Required*";
        final String NO_TITLE = "Title is empty*";
        final String NO_DESCRIPTION = "Description is empty*";
        binding.homeParentItemAdapterModifyTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty())
                    binding.homeParentItemAdapterModifyHint.setText(REQUIRED);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()){
                    binding.homeParentItemAdapterModifyHint.setText(NO_TITLE);
                } else {
                    if (binding.homeParentItemAdapterModifyDescription.getText().toString().trim().isEmpty()) {
                        binding.homeParentItemAdapterModifyHint.setText(NO_DESCRIPTION);
                    } else {
                        binding.homeParentItemAdapterModifyHint.setText("");
                    }
                }
                setUpdateVisibility();
            }
        });
        binding.homeParentItemAdapterModifyDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty())
                    binding.homeParentItemAdapterModifyHint.setText(REQUIRED);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()){
                    binding.homeParentItemAdapterModifyHint.setText(NO_DESCRIPTION);
                } else {
                    if (binding.homeParentItemAdapterModifyTitle.getText().toString().trim().isEmpty()) {
                        binding.homeParentItemAdapterModifyHint.setText(NO_TITLE);
                    } else {
                        binding.homeParentItemAdapterModifyHint.setText("");
                    }
                }
                setUpdateVisibility();
            }
        });
    }

    private void onCancel() {
        binding.homeParentItemAdapterModifyCancel.setOnClickListener(view -> dismiss());
    }

    private void onUpdate() {
        binding.homeParentItemAdapterModifyUpdate.setOnClickListener(view -> {
            if (binding.homeParentItemAdapterModifyHint.getText().toString().trim().isEmpty()) {
                habitOnModify.setHabit(binding.homeParentItemAdapterModifyTitle.getText().toString().trim());
                habitOnModify.setDescription(binding.homeParentItemAdapterModifyDescription.getText().toString().trim());
                HabitWithSubroutinesViewModel habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
                habitWithSubroutinesViewModel.updateHabit(habitOnModify);
                adapter_ref.notifyItemChanged(position);
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
    }

    private void setUpdateVisibility() {
        if (binding.homeParentItemAdapterModifyTitle.getText().toString().trim().equals(habit_title_snapshot) &&
            binding.homeParentItemAdapterModifyDescription.getText().toString().trim().equals(habit_description_snapshot)){
            if (binding.homeParentItemAdapterModifyUpdate.getVisibility() == View.VISIBLE)
                binding.homeParentItemAdapterModifyUpdate.setVisibility(View.GONE);
        } else {
            if (binding.homeParentItemAdapterModifyUpdate.getVisibility() == View.GONE)
                binding.homeParentItemAdapterModifyUpdate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}