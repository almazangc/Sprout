package com.habitdev.sprout.ui.menu.home.ui.fab_.custom_;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentHomeAddNewInsertSubroutineBinding;
import com.habitdev.sprout.enums.AppColor;

import java.util.Objects;

public class HomeAddNewInsertSubroutineFragment extends DialogFragment {

    private FragmentHomeAddNewInsertSubroutineBinding binding;
    private Subroutines subroutines;

    public interface onAddSaveInput{
        void sendInput(Subroutines subroutines);
    }
    public onAddSaveInput mOnAddSaveInput;

    public HomeAddNewInsertSubroutineFragment() {}

    public HomeAddNewInsertSubroutineFragment(Subroutines subroutines) {
        this.subroutines = subroutines;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeAddNewInsertSubroutineBinding.inflate(inflater, container, false);
        onCancel();
        onAddSave();
        return binding.getRoot();
    }

    private void onCancel(){
        binding.addNewHabitCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
    }

    private void onAddSave(){
        binding.addNewHabitSubroutineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Update or save
                subroutines = new Subroutines(
                        binding.addNewSubroutineTitle.getText().toString(),
                        binding.addNewSubroutineDescription.getText().toString(),
                        AppColor.SUNFLOWER.getColor(),
                        true
                );
//                subroutines.setSubroutine();
//                subroutines.setDescription();
//                subroutines.setColor();//update
                setmOnAddSaveInput(subroutines);
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
    }

    private void setmOnAddSaveInput(Subroutines subroutines){
        mOnAddSaveInput.sendInput(subroutines);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //for settingup interface
        try {
            //instasiating interface - deprecated
            //getTargetFragment() - null obj reference
            mOnAddSaveInput = (onAddSaveInput) getParentFragment(); //reqcode 1
        } catch (ClassCastException e){
            Log.d("tag", "onAttach: ClassExeption: " + e.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}