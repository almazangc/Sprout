package com.habitdev.sprout.utill.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.DialogFragmentCompletedAchievementBinding;
import java.util.Objects;

public class CompletedAchievementDiaglogFragment extends DialogFragment {

    private DialogFragmentCompletedAchievementBinding binding;
    private String message;
    private String achievementTitle;

    public CompletedAchievementDiaglogFragment() {}

    public CompletedAchievementDiaglogFragment(String message, String achievementTitle) {
        this.message = message;
        this.achievementTitle = achievementTitle;
    }

    public CompletedAchievementDiaglogFragment(String achievementTitle) {
        this.achievementTitle = achievementTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DialogFragmentCompletedAchievementBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.background_color_transparent);
        getDialog().setCanceledOnTouchOutside(false);
        setMessage();
        setOnclickListener();
        return binding.getRoot();
    }

    private void setOnclickListener() {
        binding.homeOnFabClickDialogButton.setOnClickListener(view -> Objects.requireNonNull(getDialog()).dismiss());
    }

    public void setMessage() {
        if (message != null) {
            binding.homeOnFabClickDialogMessage.setText(message);
        } else {
            final String message = "Congratulations, you have completed\n[ " + achievementTitle +" ]\n\nYou should be proud of yourself for unlocking achievement. Keep up the good work and continue to strive for excellence.";
            binding.homeOnFabClickDialogMessage.setText(message);
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}