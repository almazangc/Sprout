package com.habitdev.sprout.ui.menu.subroutine.ui;

import android.content.res.ColorStateList;
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
        ColorStateList cs_cloud, cs_amethyst, cs_sunflower, cs_nephritis, cs_bright_sky_blue, cs_alzarin;
        cs_cloud = ContextCompat.getColorStateList(requireContext(), R.color.CLOUDS);
        cs_amethyst = ContextCompat.getColorStateList(requireContext(), R.color.AMETHYST);
        cs_sunflower = ContextCompat.getColorStateList(requireContext(),R.color.SUNFLOWER);
        cs_nephritis = ContextCompat.getColorStateList(requireContext(), R.color.NEPHRITIS);
        cs_bright_sky_blue = ContextCompat.getColorStateList(requireContext(), R.color.BRIGHT_SKY_BLUE);
        cs_alzarin = ContextCompat.getColorStateList(requireContext(), R.color.ALIZARIN);

        if (habit.getColor().equals(AppColor.ALZARIN.getColor())) {
            binding.subroutineModifyTitleCardView.setBackgroundTintList(cs_alzarin);
        } else if (habit.getColor().equals(AppColor.AMETHYST.getColor())) {
            binding.subroutineModifyTitleCardView.setBackgroundTintList(cs_amethyst);
        } else if (habit.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
            binding.subroutineModifyTitleCardView.setBackgroundTintList(cs_bright_sky_blue);
        } else if (habit.getColor().equals(AppColor.NEPHRITIS.getColor())) {
            binding.subroutineModifyTitleCardView.setBackgroundTintList(cs_nephritis);
        } else if (habit.getColor().equals(AppColor.SUNFLOWER.getColor())) {
            binding.subroutineModifyTitleCardView.setBackgroundTintList(cs_sunflower);
        } else {
            binding.subroutineModifyTitleCardView.setBackgroundTintList(cs_cloud);
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