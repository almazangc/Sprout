package com.habitdev.sprout.ui.menu.subroutine.ui;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentSubroutineModifyBinding;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.ui.menu.subroutine.SubroutineFragment;
import com.habitdev.sprout.ui.menu.subroutine.adapter.SubroutineModifyParentItemAdapter;
import com.habitdev.sprout.ui.menu.subroutine.ui.dialog.SubroutineModifyParentItemAdapterDialogFragment;

public class SubroutineModifyFragment extends Fragment implements SubroutineModifyParentItemAdapter.OnClickListener {

    private FragmentSubroutineModifyBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private SubroutineModifyParentItemAdapter subroutineModifyParentItemAdapter;
    private final Habits habit;

    public SubroutineModifyFragment(Habits habit) {
        this.habit = habit;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubroutineModifyBinding.inflate(inflater, container, false);
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        setViewContent();
        onClickInsert();
        onBackPress();
        return binding.getRoot();
    }

    private void setViewContent() {
        setSubroutineRecyclerView();
        binding.subroutineModifyTitle.setText(habit.getHabit());
        setHabitTitleBackground();
    }

    private void setSubroutineRecyclerView() {
        subroutineModifyParentItemAdapter = new SubroutineModifyParentItemAdapter();
        binding.subroutineModifyRecyclerView.setAdapter(subroutineModifyParentItemAdapter);
        subroutineModifyParentItemAdapter.setmOnClickListener(this);
        habitWithSubroutinesViewModel.getAllSubroutinesOnReformHabitLiveData(habit.getPk_habit_uid()).observe(getViewLifecycleOwner(), subroutines -> {
            subroutineModifyParentItemAdapter.submitList(subroutines);
            subroutineModifyParentItemAdapter.notifyDataSetChanged(); //solution for now, due to rv not updating
        });

    }

    private void setHabitTitleBackground(){
        Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;
        cloud = ContextCompat.getDrawable(requireContext(), R.drawable.background_topbar_view_cloud);
        amethyst = ContextCompat.getDrawable(requireContext(), R.drawable.background_topbar_view_amethyst);
        sunflower = ContextCompat.getDrawable(requireContext(), R.drawable.background_topbar_view_sunflower);
        nephritis = ContextCompat.getDrawable(requireContext(), R.drawable.background_topbar_view_nephritis);
        bright_sky_blue = ContextCompat.getDrawable(requireContext(), R.drawable.background_topbar_view_brightsky_blue);
        alzarin = ContextCompat.getDrawable(requireContext(), R.drawable.background_topbar_view_alzarin);


        if (habit.getColor().equals(AppColor.ALZARIN.getColor())) {
            binding.subroutineModifyTitle.setBackground(alzarin);
        } else if (habit.getColor().equals(AppColor.AMETHYST.getColor())) {
            binding.subroutineModifyTitle.setBackground(amethyst);
        } else if (habit.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
            binding.subroutineModifyTitle.setBackground(bright_sky_blue);
        } else if (habit.getColor().equals(AppColor.NEPHRITIS.getColor())) {
            binding.subroutineModifyTitle.setBackground(nephritis);
        } else if (habit.getColor().equals(AppColor.SUNFLOWER.getColor())) {
            binding.subroutineModifyTitle.setBackground(sunflower);
        } else {
            binding.subroutineModifyTitle.setBackground(cloud);
        }
    }

    @Override
    public void onClickModify(Subroutines subroutine) {
        SubroutineModifyParentItemAdapterDialogFragment dialog = new SubroutineModifyParentItemAdapterDialogFragment(subroutine);
        dialog.setTargetFragment(getChildFragmentManager()
                .findFragmentById(SubroutineModifyFragment.this.getId()), 1);
        dialog.show(getChildFragmentManager(), "ModifySubroutineOnClickDialog");
        dialog.setmOnUpdateClickListener(new SubroutineModifyParentItemAdapterDialogFragment.OnUpdateClickListener() {
            @Override
            public void onClickUpdate(Subroutines subroutine) {
                habitWithSubroutinesViewModel.updateSubroutine(subroutine);
            }
        });
    }

    @Override
    public void onClickDelete(Subroutines subroutine) {
        if (habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()).size() > 2) {
            habitWithSubroutinesViewModel.deleteSubroutine(subroutine);
        } else {
            Toast.makeText(requireActivity(), "Required Minimum of 2 Subroutines", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickInsert() {
        binding.subroutineModifyInsertSubroutineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "Insert New", Toast.LENGTH_SHORT).show();
                SubroutineModifyParentItemAdapterDialogFragment dialog = new SubroutineModifyParentItemAdapterDialogFragment();
                dialog.setTargetFragment(getChildFragmentManager()
                        .findFragmentById(SubroutineModifyFragment.this.getId()), 1);
                dialog.show(getChildFragmentManager(), "ModifySubroutineOnClickDialog");
                dialog.setmOnInsertClickListener(new SubroutineModifyParentItemAdapterDialogFragment.OnInsertClickListener() {
                    @Override
                    public void onClickInsert(Subroutines subroutines) {
                        subroutines.setFk_habit_uid(habit.getPk_habit_uid());
                        habitWithSubroutinesViewModel.insertSubroutine(subroutines);
                    }
                });
            }
        });
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SubroutineFragment subroutineFragment = new SubroutineFragment();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(binding.subroutineModifyFrameLayout.getId(), subroutineFragment)
                        .commit();
                binding.subroutineModifyContainer.setVisibility(View.GONE);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}