package com.habitdev.sprout.ui.menu.home.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.DialogFragmentHomeOnFabClickDialogBinding;

import java.util.Objects;

public class HomeOnFabClickDialogFragment extends DialogFragment implements View.OnClickListener {

    private DialogFragmentHomeOnFabClickDialogBinding binding;

    public interface OnClickListener {
        void onPredefinedClick();
        void onUserNewHabitClick();
        void onDismissDialog();
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFragmentHomeOnFabClickDialogBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.background_color_transparent);
        getDialog().setCanceledOnTouchOutside(false);
        setOnclickListener();
        setPredifinedHabitsClickability();
        return binding.getRoot();
    }

    private void setPredifinedHabitsClickability() {

    }

    private void setOnclickListener() {
        binding.homeOnFabClickDialogCancel.setOnClickListener(this);
        binding.homeOnFabClickDialogPredefinedHabit.setOnClickListener(this);
        binding.homeOnFabClickDialogUserDefinedHabit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.home_on_fab_click_dialog_cancel) {
            if (this.onClickListener != null) this.onClickListener.onDismissDialog();
            dismiss();
        } else if (view.getId() == R.id.home_on_fab_click_dialog_predefined_habit) {
            if (this.onClickListener != null) this.onClickListener.onPredefinedClick();
            Objects.requireNonNull(getDialog()).dismiss();
        }
        if (view.getId() == R.id.home_on_fab_click_dialog_user_defined_habit) {
            if (this.onClickListener != null) this.onClickListener.onUserNewHabitClick();
            Objects.requireNonNull(getDialog()).dismiss();
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        if (this.onClickListener != null) this.onClickListener.onDismissDialog();
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}