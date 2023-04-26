package com.habitdev.sprout.ui.dialog;

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

public class CompletedAchievementDialogFragment extends DialogFragment {

    private DialogFragmentCompletedAchievementBinding binding;
    private String message;
    private String achievementTitle;

    public CompletedAchievementDialogFragment() {}

    public CompletedAchievementDialogFragment(String message, String achievementTitle) {
        this.message = message;
        this.achievementTitle = achievementTitle;
    }

    public CompletedAchievementDialogFragment(String achievementTitle) {
        this.achievementTitle = achievementTitle;
    }

    public interface onClick{
        void onClickOkay();
    }

    private onClick mOnClick;

    public void setmOnClick(onClick mOnClick) {
        this.mOnClick = mOnClick;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DialogFragmentCompletedAchievementBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.background_color_transparent);
        getDialog().setCanceledOnTouchOutside(false);
        setMessage();
        setOnclickListener();
        return binding.getRoot();
    }

    private void setOnclickListener() {
        binding.completedAchievementOkButton.setOnClickListener(view -> {
            if(mOnClick != null){
                mOnClick.onClickOkay();
            } else {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
    }

    public void setMessage() {
        if (message != null) {
            binding.completedAchievementOkButton.setText(message);
        } else {
            final String message = "Congratulations, you have achieved\n[ " + achievementTitle +" ]\n\nYou should be proud of yourself for unlocking achievement. Keep up the good work and continue to strive for excellence.";
            binding.completedAchievementMessage.setText(message);
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