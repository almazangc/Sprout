package com.habitdev.sprout.ui.menu.subroutine.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentSubroutineModifyBinding;
import com.habitdev.sprout.ui.menu.subroutine.SubroutineFragment;
import com.habitdev.sprout.ui.menu.subroutine.adapter.SubroutineModifyParentItemAdapter;
import com.habitdev.sprout.ui.menu.subroutine.ui.dialog.ModifySubroutineParentItemAdapterDialogFragment;

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
        binding.subroutineModifyTitle.setText(habit.getHabit());
        setSubroutineRecyclerView();
    }

    private void setSubroutineRecyclerView() {
        subroutineModifyParentItemAdapter = new SubroutineModifyParentItemAdapter();
        binding.subroutineModifyRecyclerView.setAdapter(subroutineModifyParentItemAdapter);
        subroutineModifyParentItemAdapter.setmOnClickListener(this);
        habitWithSubroutinesViewModel.getAllSubroutinesOnReformHabitLiveData(habit.getPk_habit_uid()).observe(getViewLifecycleOwner(), subroutines -> {
            if (subroutines != null) {
                subroutineModifyParentItemAdapter.submitList(subroutines);
            }
        });
    }

    @Override
    public void onClickModify(Subroutines subroutine) {
        ModifySubroutineParentItemAdapterDialogFragment dialog = new ModifySubroutineParentItemAdapterDialogFragment(subroutine);
        dialog.setTargetFragment(getChildFragmentManager()
                .findFragmentById(SubroutineModifyFragment.this.getId()), 1);
        dialog.show(getChildFragmentManager(), "ModifySubroutineOnClickDialog");
        dialog.setmOnClickListener(new ModifySubroutineParentItemAdapterDialogFragment.OnClickListener() {
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