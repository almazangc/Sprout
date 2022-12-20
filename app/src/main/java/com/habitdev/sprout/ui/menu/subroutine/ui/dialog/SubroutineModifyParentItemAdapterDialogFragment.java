package com.habitdev.sprout.ui.menu.subroutine.ui.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.DialogFragmentSubroutineModifyParentItemAdapterBinding;
import com.habitdev.sprout.enums.AppColor;

import java.util.Objects;

public class SubroutineModifyParentItemAdapterDialogFragment extends DialogFragment implements View.OnClickListener {
    private DialogFragmentSubroutineModifyParentItemAdapterBinding binding;
    private final Subroutines subroutine;

    private static final int ic_check = R.drawable.ic_check;
    private static int current_selected_color;
    private static int old_selected_color;
    private static String color;

    private OnUpdateClickListener mOnUpdateClickListener;

    private OnInsertClickListener mOnInsertClickListener;

    public interface OnUpdateClickListener {
        void onClickUpdate(Subroutines subroutine);
    }

    public interface OnInsertClickListener {
        void onClickInsert(Subroutines subroutines);
    }

    public void setmOnUpdateClickListener(OnUpdateClickListener mOnUpdateClickListener) {
        this.mOnUpdateClickListener = mOnUpdateClickListener;
    }

    public void setmOnInsertClickListener(OnInsertClickListener mOnInsertClickListener) {
        this.mOnInsertClickListener = mOnInsertClickListener;
    }

    public SubroutineModifyParentItemAdapterDialogFragment(Subroutines subroutine) {
        this.subroutine = subroutine;
        current_selected_color = 0;
        old_selected_color = 0;
        color = AppColor.CLOUDS.getColor();
    }

    public SubroutineModifyParentItemAdapterDialogFragment() {
        this.subroutine = new Subroutines("", "", AppColor.CLOUDS.getColor(), true);
        current_selected_color = 0;
        old_selected_color = 0;
        color = AppColor.CLOUDS.getColor();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (mOnUpdateClickListener == null) mOnUpdateClickListener = (OnUpdateClickListener) getParentFragment();
            if (mOnInsertClickListener == null) mOnInsertClickListener = (OnInsertClickListener) getParentFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFragmentSubroutineModifyParentItemAdapterBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.background_color_transparent);
        getDialog().setCanceledOnTouchOutside(false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        setContentView();
    }

    private void setContentView() {
        binding.dialogSubroutineModifyTitle.setText(subroutine.getSubroutine());
        binding.dialogSubroutineModifyDescription.setText(subroutine.getDescription());
        binding.dialogSubroutineModifyLayoutMiscellaneous.setVisibility(View.GONE);
        setHint();
        setSubroutineColor();
        colorSelect();
        setOnclickListener();
    }

    private void setHint() {
        final String REQUIRED = "Required*";
        final String EMPTY_TITLE = "Title is empty*";
        final String EMPTY_DESCRIPTION = "Description is empty*";

        binding.dialogSubroutineModifyHint.setText("");
        binding.dialogSubroutineModifyTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty())
                    binding.dialogSubroutineModifyHint.setText(REQUIRED);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.dialogSubroutineModifyHint.setText(EMPTY_TITLE);
                } else {
                    if (binding.dialogSubroutineModifyDescription.getText().toString().trim().isEmpty()) {
                        binding.dialogSubroutineModifyHint.setText(EMPTY_DESCRIPTION);
                    } else {
                        binding.dialogSubroutineModifyHint.setText("");
                    }
                }
            }
        });
        binding.dialogSubroutineModifyDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty())
                    binding.dialogSubroutineModifyHint.setText(REQUIRED);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.dialogSubroutineModifyHint.setText(EMPTY_DESCRIPTION);
                } else {
                    if (binding.dialogSubroutineModifyTitle.getText().toString().trim().isEmpty()) {
                        binding.dialogSubroutineModifyHint.setText(EMPTY_TITLE);
                    } else {
                        binding.dialogSubroutineModifyHint.setText("");
                    }
                }
            }
        });
    }

    private void colorSelect() {
        binding.dialogSubroutineModifyColorSelector.setOnClickListener(v -> {
            if (binding.dialogSubroutineModifyLayoutMiscellaneous.getVisibility() == View.GONE) {
                binding.dialogSubroutineModifyLayoutMiscellaneous.setVisibility(View.VISIBLE);
            } else {
                binding.dialogSubroutineModifyLayoutMiscellaneous.setVisibility(View.GONE);
            }
        });

        binding.cloudMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(0);
            setSelected_color();
        });

        binding.alzarinMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(1);
            setSelected_color();
        });

        binding.amethystMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(2);
            setSelected_color();
        });

        binding.brightskyBlueMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(3);
            setSelected_color();
        });

        binding.nephritisMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(4);
            setSelected_color();
        });

        binding.sunflowerMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(5);
            setSelected_color();
        });
    }

    private void setSubroutineColor() {
        if (subroutine.getColor().equals(AppColor.ALZARIN.getColor())) {
            current_selected_color = 1;
        } else if (subroutine.getColor().equals(AppColor.AMETHYST.getColor())) {
            current_selected_color = 2;
        } else if (subroutine.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
            current_selected_color = 3;
        } else if (subroutine.getColor().equals(AppColor.NEPHRITIS.getColor())) {
            current_selected_color = 4;
        } else if (subroutine.getColor().equals(AppColor.SUNFLOWER.getColor())) {
            current_selected_color = 5;
        } else {
            old_selected_color = 1;
        }
        setSelected_color();
    }

    private void setSelected_color() {
        if (old_selected_color != current_selected_color) {
            switch (current_selected_color) {
                case 1:
                    binding.alzarinSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_alzarin));
                    color = AppColor.ALZARIN.getColor();
                    break;
                case 2:
                    binding.amethystSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_amethyst));
                    color = AppColor.AMETHYST.getColor();
                    break;
                case 3:
                    binding.brightskyBlueSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_brightsky_blue));
                    color = AppColor.BRIGHT_SKY_BLUE.getColor();
                    break;
                case 4:
                    binding.nephritisSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_nephritis));
                    color = AppColor.NEPHRITIS.getColor();
                    break;
                case 5:
                    binding.sunflowerSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_sunflower));
                    color = AppColor.SUNFLOWER.getColor();
                    break;
                default:
                    binding.cloudSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_clouds));
                    color = AppColor.CLOUDS.getColor();
                    break;
            }

            switch (old_selected_color) {
                case 1:
                    binding.alzarinSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 2:
                    binding.amethystSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 3:
                    binding.brightskyBlueSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 4:
                    binding.nephritisSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 5:
                    binding.sunflowerSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                default:
                    binding.cloudSelected.setImageResource(R.color.TRANSPARENT);
                    break;
            }
        }
    }

    private void updateSelectedColorIndex(int newSelected) {
        old_selected_color = current_selected_color;
        current_selected_color = newSelected;
    }

    private void setBackgroundColorIndicator(Drawable backgroundNoteIndicator) {
        binding.dialogSubroutineModifyColorSelector.setBackground(backgroundNoteIndicator);
    }

    private void setOnclickListener() {
        binding.dialogSubroutineModifyCancel.setOnClickListener(this);
        binding.dialogSubroutineModifyUpdateSubroutineBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dialog_subroutine_modify_cancel) {
            Objects.requireNonNull(getDialog()).dismiss();
        } else if (view.getId() == R.id.dialog_subroutine_modify_update_subroutine_btn) {
            if (binding.dialogSubroutineModifyHint.getText().toString().trim().isEmpty()) {
                subroutine.setSubroutine(binding.dialogSubroutineModifyTitle.getText().toString().trim());
                subroutine.setDescription(binding.dialogSubroutineModifyDescription.getText().toString().trim());
                subroutine.setColor(color);
                if (this.mOnUpdateClickListener != null) this.mOnUpdateClickListener.onClickUpdate(subroutine);
                if (this.mOnInsertClickListener != null) this.mOnInsertClickListener.onClickInsert(subroutine);
                Objects.requireNonNull(getDialog()).dismiss();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOnUpdateClickListener != null) mOnUpdateClickListener = null;
        if (mOnInsertClickListener != null) mOnInsertClickListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}