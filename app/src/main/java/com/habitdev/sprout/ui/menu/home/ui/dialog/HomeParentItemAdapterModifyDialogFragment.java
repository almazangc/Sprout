package com.habitdev.sprout.ui.menu.home.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.achievement.AchievementViewModel;
import com.habitdev.sprout.database.achievement.model.Achievement;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.databinding.DialogFragmentHomeParentItemAdapterModifyBinding;
import com.habitdev.sprout.enums.HomeConfigurationKeys;
import com.habitdev.sprout.ui.menu.home.adapter.HomeParentItemAdapter;
import com.habitdev.sprout.ui.dialog.CompletedAchievementDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class HomeParentItemAdapterModifyDialogFragment extends DialogFragment {
    private DialogFragmentHomeParentItemAdapterModifyBinding binding;
    private static Habits habitOnModify;
    private static int position;
    private static String habit_title_snapshot;
    private static String habit_description_snapshot;

    private HomeParentItemAdapter adapter_ref;

    private static SharedPreferences sharedPreferences;
    private static Bundle savedInstanceState;

    public void setAdapter_ref(HomeParentItemAdapter adapter_ref) {
        this.adapter_ref = adapter_ref;
    }

    public interface OnHabitModifyListener {
        void onDialogDismiss();
    }

    private OnHabitModifyListener mOnHabitModifyListener;

    public void setmOnHabitModifyListener(OnHabitModifyListener mOnHabitModifyListener) {
        this.mOnHabitModifyListener = mOnHabitModifyListener;
    }

    public HomeParentItemAdapterModifyDialogFragment() {}

    public HomeParentItemAdapterModifyDialogFragment(Habits habitOnModify, int position) {
        HomeParentItemAdapterModifyDialogFragment.habitOnModify = habitOnModify;
        habit_title_snapshot = habitOnModify.getHabit();
        habit_description_snapshot = habitOnModify.getDescription();
        HomeParentItemAdapterModifyDialogFragment.position = position;
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

            habitOnModify = (Habits) savedInstanceState.getSerializable(HomeConfigurationKeys.HABIT.getKey());
            position = savedInstanceState.getInt(HomeConfigurationKeys.POSITION.getKey());

            sharedPreferences = requireActivity().getSharedPreferences(HomeConfigurationKeys.HOME_HABIT_ON_MODIFY_SHARED_PREF.getKey(), Context.MODE_PRIVATE);

            if (sharedPreferences.contains(HomeConfigurationKeys.TITLE.getKey())
                    && sharedPreferences.contains(HomeConfigurationKeys.DESCRIPTION.getKey())) {

                habitOnModify.setHabit(sharedPreferences.getString(HomeConfigurationKeys.TITLE.getKey(), ""));
                habitOnModify.setDescription(sharedPreferences.getString(HomeConfigurationKeys.DESCRIPTION.getKey(), ""));
                binding.homeParentItemAdapterModifyHint.setText(sharedPreferences.getString(HomeConfigurationKeys.HINT_TEXT.getKey(), ""));
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
                //show dialog confirmation update method
                showConfirmUpdateDialog();
            }
        });
    }

    public void showConfirmUpdateDialog() {
        new AlertDialog.Builder(requireContext())
                .setMessage("Apply change's?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    habitOnModify.setHabit(binding.homeParentItemAdapterModifyTitle.getText().toString().trim());
                    habitOnModify.setDescription(binding.homeParentItemAdapterModifyDescription.getText().toString().trim());
                    HabitWithSubroutinesViewModel habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
                    habitWithSubroutinesViewModel.updateHabit(habitOnModify);
                    adapter_ref.notifyItemChanged(position);

                    //Show snackbar notif
                    Snackbar.make(binding.getRoot(), Html.fromHtml("<b> " + habit_title_snapshot + " has been updated!"), Snackbar.LENGTH_LONG)
                            .setAction("Dismiss", view -> {
                                //Dismiss snack bar
                            })
                            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER))
                            .setTextColor(ContextCompat.getColor(requireContext(), R.color.NIGHT))
                            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.CLOUDS))
                            .show();

                    clearSharedPref();

                    //TODO: UPDATE UID WHEN APPDATABASE CHANGE
                    AchievementViewModel achievementViewModel = new ViewModelProvider(requireActivity()).get(AchievementViewModel.class);
                    Achievement EditCustomHabitTitle = achievementViewModel.getAchievementByUID(3);
                    if (!EditCustomHabitTitle.is_completed() ) {
                        EditCustomHabitTitle.setIs_completed(true);
                        EditCustomHabitTitle.setCurrent_progress(EditCustomHabitTitle.getCurrent_progress() + 1);
                        EditCustomHabitTitle.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                        achievementViewModel.updateAchievement(EditCustomHabitTitle);
                        CompletedAchievementDialogFragment dialog = new CompletedAchievementDialogFragment(EditCustomHabitTitle.getTitle());
                        dialog.setTargetFragment(getChildFragmentManager()
                                .findFragmentById(HomeParentItemAdapterModifyDialogFragment.this.getId()), 1);
                        dialog.show(getChildFragmentManager(), "CompletedAchievementDialog");
                        dialog.setmOnClick(new CompletedAchievementDialogFragment.onClick() {
                            @Override
                            public void onClickOkay() {
                                if (mOnHabitModifyListener != null) mOnHabitModifyListener.onDialogDismiss();
                                Objects.requireNonNull(getDialog()).dismiss();
                            }
                        });
                    } else {
                        if (mOnHabitModifyListener != null) mOnHabitModifyListener.onDialogDismiss();
                        Objects.requireNonNull(getDialog()).dismiss();
                    }
                })
                .setNegativeButton("No", null)
                .show();
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
        outState.putSerializable(HomeConfigurationKeys.HABIT.getKey(), habitOnModify);
        outState.putInt(HomeConfigurationKeys.POSITION.getKey(), position);

        sharedPreferences = requireActivity().getSharedPreferences(HomeConfigurationKeys.HOME_HABIT_ON_MODIFY_SHARED_PREF.getKey(), Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(HomeConfigurationKeys.TITLE.getKey(), binding.homeParentItemAdapterModifyTitle.getText().toString().trim())
                .putString(HomeConfigurationKeys.DESCRIPTION.getKey(), binding.homeParentItemAdapterModifyDescription.getText().toString().trim())
                .putString(HomeConfigurationKeys.HINT_TEXT.getKey(), binding.homeParentItemAdapterModifyHint.getText().toString().trim())
                .apply();
    }

    private void clearSharedPref() {
        sharedPreferences = requireActivity().getSharedPreferences(HomeConfigurationKeys.HOME_HABIT_ON_MODIFY_SHARED_PREF.getKey(), Context.MODE_PRIVATE);
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