package com.habitdev.sprout.ui.menu.home.ui.dialog;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.habitdev.sprout.enums.HomeConfigurationKeys;

import java.util.Objects;

public class HomeParentItemAdapterModifyDialogFragment extends DialogFragment {
    private DialogFragmentHomeParentItemAdapterModifyBinding binding;
    private Habits habitOnModify;
    private int position;
    private String habit_title_snapshot;
    private String habit_description_snapshot;

    private HomeParentItemAdapter adapter_ref;

    private static SharedPreferences sharedPreferences;
    private static Bundle savedInstanceState;

    public void setAdapter_ref(HomeParentItemAdapter adapter_ref) {
        this.adapter_ref = adapter_ref;
    }

    public interface onHabitModifyListener{
        void onDialogDismiss();
    }

    private onHabitModifyListener mOnHabitModifyListener;

    public void setmOnHabitModifyListener(onHabitModifyListener mOnHabitModifyListener) {
        this.mOnHabitModifyListener = mOnHabitModifyListener;
    }

    public HomeParentItemAdapterModifyDialogFragment() {}

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
        getDialog().setCanceledOnTouchOutside(false);

        if (savedInstanceState != null) {
            HomeParentItemAdapterModifyDialogFragment.savedInstanceState = savedInstanceState;
        }

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (savedInstanceState != null) {

            habitOnModify = (Habits) savedInstanceState.getSerializable(HomeConfigurationKeys.HABIT.getValue());
            position = savedInstanceState.getInt(HomeConfigurationKeys.POSITION.getValue());

            sharedPreferences = requireActivity().getSharedPreferences(HomeConfigurationKeys.HOME_HABIT_ON_MODIFY_SHARED_PREF.getValue(), Context.MODE_PRIVATE);

            if (sharedPreferences.contains(HomeConfigurationKeys.TITLE.getValue())
                    && sharedPreferences.contains(HomeConfigurationKeys.DESCRIPTION.getValue())) {

                habitOnModify.setHabit(sharedPreferences.getString(HomeConfigurationKeys.TITLE.getValue(), ""));
                habitOnModify.setDescription(sharedPreferences.getString(HomeConfigurationKeys.DESCRIPTION.getValue(), ""));
                binding.homeParentItemAdapterModifyHint.setText(sharedPreferences.getString(HomeConfigurationKeys.HINT_TEXT.getValue(), ""));
            }
        }

        binding.homeParentItemAdapterModifyTitle.setText(habitOnModify.getHabit());
        binding.homeParentItemAdapterModifyDescription.setText(habitOnModify.getDescription());

        setUpdateVisibility();
        validateHabit();
        onUpdate();
        onCancel();
    }

    private void validateHabit() {
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
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
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
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
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
        binding.homeParentItemAdapterModifyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSharedPref();
                if (mOnHabitModifyListener != null) mOnHabitModifyListener.onDialogDismiss();
                dismiss();
            }
        });
    }

    private void onUpdate() {
        binding.homeParentItemAdapterModifyUpdate.setOnClickListener(view -> {
            if (binding.homeParentItemAdapterModifyHint.getText().toString().trim().isEmpty()) {
                habitOnModify.setHabit(binding.homeParentItemAdapterModifyTitle.getText().toString().trim());
                habitOnModify.setDescription(binding.homeParentItemAdapterModifyDescription.getText().toString().trim());
                HabitWithSubroutinesViewModel habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
                habitWithSubroutinesViewModel.updateHabit(habitOnModify);
                adapter_ref.notifyItemChanged(position);

                clearSharedPref();
                if (mOnHabitModifyListener != null) mOnHabitModifyListener.onDialogDismiss();
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
    }

    private void setUpdateVisibility() {
        if (binding.homeParentItemAdapterModifyTitle.getText().toString().trim().equals(habit_title_snapshot) &&
                binding.homeParentItemAdapterModifyDescription.getText().toString().trim().equals(habit_description_snapshot)) {
            if (binding.homeParentItemAdapterModifyUpdate.getVisibility() == View.VISIBLE)
                binding.homeParentItemAdapterModifyUpdate.setVisibility(View.GONE);
        } else {
            if (binding.homeParentItemAdapterModifyUpdate.getVisibility() == View.GONE)
                binding.homeParentItemAdapterModifyUpdate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(HomeConfigurationKeys.HABIT.getValue(), habitOnModify);
        outState.putInt(HomeConfigurationKeys.POSITION.getValue(), position);

        sharedPreferences = requireActivity().getSharedPreferences(HomeConfigurationKeys.HOME_HABIT_ON_MODIFY_SHARED_PREF.getValue(), Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(HomeConfigurationKeys.TITLE.getValue(), binding.homeParentItemAdapterModifyTitle.getText().toString().trim())
                .putString(HomeConfigurationKeys.DESCRIPTION.getValue(), binding.homeParentItemAdapterModifyDescription.getText().toString().trim())
                .putString(HomeConfigurationKeys.HINT_TEXT.getValue(), binding.homeParentItemAdapterModifyHint.getText().toString().trim())
                .apply();
    }

    private void clearSharedPref(){
        sharedPreferences = requireActivity().getSharedPreferences(HomeConfigurationKeys.HOME_HABIT_ON_MODIFY_SHARED_PREF.getValue(), Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mOnHabitModifyListener = null;
        binding = null;
        adapter_ref = null;
    }
}