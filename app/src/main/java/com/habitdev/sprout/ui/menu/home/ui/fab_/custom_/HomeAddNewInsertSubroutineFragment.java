package com.habitdev.sprout.ui.menu.home.ui.fab_.custom_;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentHomeAddNewInsertSubroutineBinding;
import com.habitdev.sprout.enums.AppColor;

import java.util.Objects;

public class HomeAddNewInsertSubroutineFragment extends DialogFragment {

    private FragmentHomeAddNewInsertSubroutineBinding binding;
    private Subroutines subroutine;
    private final boolean onModify;
    private final boolean onRemove;

    private final int ic_check;
    private int current_selected_color;
    private int old_selected_color;
    private String color;

    public interface onDialoagChange {
        void addSubroutine(Subroutines subroutines);
        void modifySubroutine(Subroutines subroutines);
        void removeSubroutine(Subroutines subroutines);
    }
    public onDialoagChange mOnDialoagChange;

    public HomeAddNewInsertSubroutineFragment() {
        this.onModify = false;
        this.onRemove = false;
        this.ic_check = R.drawable.ic_check;
    }

    public HomeAddNewInsertSubroutineFragment(Subroutines subroutine) {
        this.subroutine = subroutine;
        this.onModify = true;
        this.onRemove = false;
        this.ic_check = R.drawable.ic_check;
        this.current_selected_color = 0;
        this.old_selected_color = 0;
        this.color = AppColor.CLOUDS.getColor();
    }

    public HomeAddNewInsertSubroutineFragment(Subroutines subroutines ,boolean onRemove) {
        this.subroutine = subroutines;
        this.onModify = false;
        this.onRemove = onRemove;
        this.ic_check = R.drawable.ic_check;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeAddNewInsertSubroutineBinding.inflate(inflater, container, false);
        onRemove();
        setSubroutine();
        onCancel();
        onClickSave();
        return binding.getRoot();
    }

    private void setSubroutine(){
        if(subroutine != null){
            binding.addNewSubroutineTitle.setText(subroutine.getSubroutine());
            binding.addNewSubroutineDescription.setText(subroutine.getDescription());
        }
        setSubroutineColor();
        colorSelect();
    }

    private void onCancel(){
        binding.addNewHabitCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
    }

    private void onClickSave(){
        binding.addNewHabitSubroutineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subroutine == null) {
                    onAdd();
                } else {
                    onModify();
                }
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
    }

    private void onAdd(){
        subroutine = new Subroutines(
                binding.addNewSubroutineTitle.getText().toString(),
                binding.addNewSubroutineDescription.getText().toString(),
                color,
                true
        );
        mOnDialoagChange.addSubroutine(subroutine);
    }

    private void onModify(){
        if (onModify){
            subroutine.setSubroutine(binding.addNewSubroutineTitle.getText().toString());
            subroutine.setDescription(binding.addNewSubroutineDescription.getText().toString());
            subroutine.setColor(color);
            mOnDialoagChange.modifySubroutine(subroutine);
        }
    }

    private void onRemove(){
       if (onRemove){
           mOnDialoagChange.removeSubroutine(subroutine);
           dismiss();
       }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //for setting up interface
        try {
            //instantiating interface - deprecated
            //getTargetFragment() - null obj reference
            mOnDialoagChange = (onDialoagChange) getParentFragment(); //request code 1
        } catch (ClassCastException e){
            Log.d("tag", "onAttach: ClassException: " + e.getMessage());
        }
    }

    private void colorSelect() {
        binding.addNewSubroutineColorSelector.setOnClickListener(v -> {
            if (binding.addNewSubroutineLayoutMiscellaneous.getVisibility() == View.GONE) {
                binding.addNewSubroutineLayoutMiscellaneous.setVisibility(View.VISIBLE);
            } else {
                binding.addNewSubroutineLayoutMiscellaneous.setVisibility(View.GONE);
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

    private void setSubroutineColor(){
        if (subroutine != null){
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