package com.habitdev.sprout.ui.menu.home.ui.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.DialogFragmentHomeAddNewInsertSubroutineBinding;
import com.habitdev.sprout.enums.AppColor;

import java.util.Objects;

public class HomeAddNewInsertSubroutineDialogFragment extends DialogFragment {

    private DialogFragmentHomeAddNewInsertSubroutineBinding binding;
    private Subroutines subroutine;
    private final boolean onModify;
    private final boolean onRemove;

    private int current_selected_color;
    private int old_selected_color;
    private String color = AppColor.CLOUDS.getColor();

    public interface onDialoagChange {
        void addSubroutine(Subroutines subroutines);

        void modifySubroutine(Subroutines subroutines);

        void removeSubroutine(Subroutines subroutines);
    }

    public onDialoagChange mOnDialoagChange;

    public HomeAddNewInsertSubroutineDialogFragment() {
        this.onModify = false;
        this.onRemove = false;
    }

    public HomeAddNewInsertSubroutineDialogFragment(Subroutines subroutine) {
        this.subroutine = subroutine;
        this.onModify = true;
        this.onRemove = false;
    }

    public HomeAddNewInsertSubroutineDialogFragment(Subroutines subroutines, boolean onRemove) {
        this.subroutine = subroutines;
        this.onModify = false;
        this.onRemove = onRemove;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFragmentHomeAddNewInsertSubroutineBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.background_color_transparent);
        setAddBtnText();
        setHint();
        onRemove();
        setSubroutine();
        onCancel();
        onClickSave();
        return binding.getRoot();
    }

    private void setAddBtnText() {
        if (onModify) {
            binding.addNewHabitSubroutineBtn.setText("Save Update");
        } else {
            binding.addNewHabitSubroutineBtn.setText("Insert New");
        }
    }

    private void setSubroutine() {
        if (subroutine != null) {
            binding.addNewSubroutineTitle.setText(subroutine.getSubroutine());
            binding.addNewSubroutineDescription.setText(subroutine.getDescription());
        }
        setSubroutineColor();
        colorSelect();
    }

    private void onCancel() {
        binding.addNewHabitCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
    }

    private void onClickSave() {
        binding.addNewHabitSubroutineBtn.setOnClickListener(view -> {
           if (binding.addNewSubroutineHint.getText().toString().trim().isEmpty()){
               if (subroutine == null) {
                   onAdd();
               } else {
                   onModify();
               }
               Objects.requireNonNull(getDialog()).dismiss();
           }
        });
    }

    private void onAdd() {
        subroutine = new Subroutines(
                binding.addNewSubroutineTitle.getText().toString(),
                binding.addNewSubroutineDescription.getText().toString(),
                color,
                true
        );
        mOnDialoagChange.addSubroutine(subroutine);
    }

    private void onModify() {
        if (onModify) {
            subroutine.setSubroutine(binding.addNewSubroutineTitle.getText().toString());
            subroutine.setDescription(binding.addNewSubroutineDescription.getText().toString());
            subroutine.setColor(color);
            mOnDialoagChange.modifySubroutine(subroutine);
        }
    }

    private void onRemove() {
        if (onRemove) {
            mOnDialoagChange.removeSubroutine(subroutine);
            dismiss();
        }
    }

    private void setHint(){
        binding.addNewSubroutineTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty()) binding.addNewSubroutineHint.setText("Required*");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()){
                        binding.addNewSubroutineHint.setText("Empty Title*");
                } else {
                    if (binding.addNewSubroutineDescription.getText().toString().trim().isEmpty()){
                        binding.addNewSubroutineHint.setText("Empty Description*");
                    } else {
                        binding.addNewSubroutineHint.setText("");
                    }
                }
            }
        });

        binding.addNewSubroutineDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty()) binding.addNewSubroutineHint.setText("Required*");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()){
                        binding.addNewSubroutineHint.setText("Empty Description*");
                } else {
                    if (binding.addNewSubroutineTitle.getText().toString().trim().isEmpty()){
                        binding.addNewSubroutineHint.setText("Empty Title*");
                    } else {
                        binding.addNewSubroutineHint.setText("");
                    }
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnDialoagChange = (onDialoagChange) getParentFragment();
        } catch (ClassCastException e) {
            Toast.makeText(requireActivity(), "onAttach: ClassException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void colorSelect() {
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
        if (subroutine != null) {
            if (subroutine.getColor().equals(AppColor.ALZARIN.getColor())) {
                current_selected_color = 1;
                setSelected_color();
            } else if (subroutine.getColor().equals(AppColor.AMETHYST.getColor())) {
                current_selected_color = 2;
                setSelected_color();
            } else if (subroutine.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
                current_selected_color = 3;
                setSelected_color();
            } else if (subroutine.getColor().equals(AppColor.NEPHRITIS.getColor())) {
                current_selected_color = 4;
                setSelected_color();
            } else if (subroutine.getColor().equals(AppColor.SUNFLOWER.getColor())) {
                current_selected_color = 5;
                setSelected_color();
            } else {
                old_selected_color = 1;
                setSelected_color();
            }
        } else {
            old_selected_color = 1;
            setSelected_color();
        }
    }

    private void setSelected_color() {
        if (old_selected_color != current_selected_color) {
            int ic_check = R.drawable.ic_check;
            switch (current_selected_color) {
                case 1:
                    //alzarin
                    binding.alzarinSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_alzarin));
                    color = AppColor.ALZARIN.getColor();
                    break;
                case 2:
                    //amethyst
                    binding.amethystSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_amethyst));
                    color = AppColor.AMETHYST.getColor();
                    break;
                case 3:
                    //bright_sky_blue
                    binding.brightskyBlueSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_brightsky_blue));
                    color = AppColor.BRIGHT_SKY_BLUE.getColor();
                    break;
                case 4:
                    //nephritis
                    binding.nephritisSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_nephritis));
                    color = AppColor.NEPHRITIS.getColor();
                    break;
                case 5:
                    //sunflower
                    binding.sunflowerSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_sunflower));
                    color = AppColor.SUNFLOWER.getColor();
                    break;
                default:
                    //clouds night
                    binding.cloudSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_clouds));
                    color = AppColor.CLOUDS.getColor();
                    break;
            }

            switch (old_selected_color) {
                case 1:
                    //alzarin
                    binding.alzarinSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 2:
                    //amethyst
                    binding.amethystSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 3:
                    //bright_sky_blue
                    binding.brightskyBlueSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 4:
                    //nephritis
                    binding.nephritisSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 5:
                    //sunflower
                    binding.sunflowerSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                default:
                    //clouds night
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
        binding.addNewSubroutineColorSelector.setBackground(backgroundNoteIndicator);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}