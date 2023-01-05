package com.habitdev.sprout.ui.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.DialogFragmentOnBackPressBinding;

import java.util.Objects;

public class OnBackPressDialogFragment extends DialogFragment {

    private DialogFragmentOnBackPressBinding binding;

    public interface onCancelDialog {
        void cancelDialog();
    }

    public onCancelDialog mOnCancelDialog;

    public void setmOnCancelDialog(onCancelDialog mOnCancelDialog) {
        this.mOnCancelDialog = mOnCancelDialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (mOnCancelDialog == null) {
                mOnCancelDialog = (onCancelDialog) getParentFragment();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFragmentOnBackPressBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.background_color_transparent);


        binding.onBackPressDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnCancelDialog != null) {
                    mOnCancelDialog.cancelDialog();
                    dismiss();
                }
            }
        });

        binding.onBackPressDialogExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().finishAndRemoveTask();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        if (mOnCancelDialog != null) {
            mOnCancelDialog.cancelDialog();
            dismiss();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnCancelDialog = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}