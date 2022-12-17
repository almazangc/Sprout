package com.habitdev.sprout.ui.menu.home.ui.dialog;

import android.annotation.SuppressLint;
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

    public interface onClickListener {
        void onPredefinedClick();
        void onUserDefineClick();
        void onDismissDialog();
    }

    private onClickListener onClickListener;

    public void setOnClickListener(onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFragmentHomeOnFabClickDialogBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.background_color_transparent);
        getDialog().setCanceledOnTouchOutside(false);
        setOnclickListener();;
//        setPadding();
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setPadding(){
        binding.homeOnFabClickDialogPredefinedHabit.setOnTouchListener((view, motionEvent) -> {
//            Log.d("tag", "onTouch: " + motionEvent.getAction());
            return false;
        });
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
            if (this.onClickListener != null) this.onClickListener.onUserDefineClick();
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