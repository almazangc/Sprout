package com.habitdev.sprout.ui.menu.subroutine.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.model.room.Subroutines;
import com.habitdev.sprout.databinding.FragmentSubroutineModifyBinding;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.enums.SubroutineConfigurationKeys;
import com.habitdev.sprout.ui.menu.subroutine.adapter.SubroutineModifyParentItemAdapter;
import com.habitdev.sprout.ui.menu.subroutine.adapter.SubroutineModifyParentOnclickListener;
import com.habitdev.sprout.ui.menu.subroutine.ui.dialog.SubroutineModifyParentItemAdapterDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class SubroutineModifyFragment extends Fragment implements SubroutineModifyParentOnclickListener{

    private FragmentSubroutineModifyBinding binding;
    private static HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private Habits habit;
    private static SubroutineModifyParentItemAdapter adapter;
    private static boolean isOnInsertSubroutine;
    private static boolean isOnUpdateSubroutine;
    private static int item_position;

    public interface OnReturnSubroutine {
        void returnSubroutineFragment();
    }

    private OnReturnSubroutine mOnReturnSubroutine;

    public void setmOnClickBackPress(OnReturnSubroutine mOnReturnSubroutine) {
        this.mOnReturnSubroutine = mOnReturnSubroutine;
    }

    public void setHabit(Habits habit) {
        this.habit = habit;
    }

    public SubroutineModifyFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (mOnReturnSubroutine == null) {
                mOnReturnSubroutine = (OnReturnSubroutine) getParentFragment();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubroutineModifyBinding.inflate(inflater, container, false);
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            habit = (Habits) savedInstanceState.getSerializable(SubroutineConfigurationKeys.HABIT.getKey());
            isOnUpdateSubroutine = savedInstanceState.getBoolean(SubroutineConfigurationKeys.IS_ON_SUBROUTINE_UPDATE.getKey());
            isOnInsertSubroutine = savedInstanceState.getBoolean(SubroutineConfigurationKeys.IS_ON_SUBROUTINE_INSERT.getKey());
            item_position  = savedInstanceState.getInt(SubroutineConfigurationKeys.ITEM_POSITION.getKey());
        }

        setViewContent();
        setOnClickInsert();

        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnUpdateSubroutine) {
            onItemUpdate(item_position);
        } else if (isOnInsertSubroutine) {
            onItemInsert();
        }
    }

    private void setViewContent() {
        setSubroutineRecyclerView();
        binding.subroutineModifyTitle.setText(habit.getHabit());
        setHabitTitleBackground();
    }

    private void setSubroutineRecyclerView() {
        adapter = new SubroutineModifyParentItemAdapter(habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()));
        binding.subroutineModifyRecyclerView.setAdapter(adapter);
        adapter.setmSubroutineModifyOnclickListener(this);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall);
        binding.subroutineModifyRecyclerView.setLayoutAnimation(animationController);

        habitWithSubroutinesViewModel.getAllSubroutinesOnReformHabitLiveData(habit.getPk_habit_uid()).observe(getViewLifecycleOwner(), new Observer<List<Subroutines>>() {
            @Override
            public void onChanged(List<Subroutines> subroutines) {
                adapter.setNewSubroutineList(new ArrayList<>(subroutines));
            }
        });
    }

    private void setHabitTitleBackground() {

        final Drawable cloud, amethyst, sunflower, nephritis, bright_sky_blue, alzarin;

        cloud = ContextCompat.getDrawable(requireContext(), R.drawable.background_topbar_view_cloud);
        amethyst = ContextCompat.getDrawable(requireContext(), R.drawable.background_top_bar_view_amethyst);
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

    /**
     * Modify a subroutine
     * @param position of selected subroutine
     */
    @Override
    public void onItemUpdate(int position) {

        isOnUpdateSubroutine = true;
        item_position = position;

        final Subroutines subroutine = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()).get(position);

        SubroutineModifyParentItemAdapterDialogFragment dialog = new SubroutineModifyParentItemAdapterDialogFragment(subroutine);

        dialog.setTargetFragment(getChildFragmentManager().findFragmentById(SubroutineModifyFragment.this.getId()), 1);
        dialog.show(getChildFragmentManager(), "ModifySubroutineOnClickDialog");

        dialog.setmOnUpdateClickListener(new SubroutineModifyParentItemAdapterDialogFragment.OnUpdateClickListener() {
            @Override
            public void onClickUpdate(Subroutines subroutine) {
                habitWithSubroutinesViewModel.updateSubroutine(subroutine);
                adapter.notifyItemChanged(position);
                isOnUpdateSubroutine = false;
            }

            @Override
            public void onDialogDismiss() {
                isOnUpdateSubroutine = false;
            }
        });
    }

    /**
     * Removes a subroutine
     * @param position of selected subroutine
     */
    @Override
    public void onItemDelete(int position) {
        if (habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()).size() > 2) {
            final Subroutines subroutine = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid()).get(position);
            new AlertDialog.Builder(requireContext())
                    .setMessage("Removing a subroutine will also remove the progress of the subroutine.\nAre you sure you want to remove the subroutine " + subroutine.getSubroutine().toLowerCase() + "?")
                    .setCancelable(false)
                    .setPositiveButton("YES", (dialogInterface, i) -> {
                        habitWithSubroutinesViewModel.deleteSubroutine(subroutine);
                        updateTotalSubroutine();

                        //Show snackbar notif
                        Snackbar.make(binding.getRoot(), Html.fromHtml("<b>" + habit.getHabit() + "</b>: " + subroutine.getSubroutine().toLowerCase() + "has been removed"), Snackbar.LENGTH_LONG)
                                .setAction("Dismiss", view -> {
                                    //Dismiss snack bar
                                })
                                .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER))
                                .setTextColor(getResources().getColor(R.color.NIGHT))
                                .setBackgroundTint(getResources().getColor(R.color.CLOUDS))
                                .show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            Toast.makeText(requireActivity(), "Required minimum of (2) subroutines", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyItemChanged(position);
    }

    /**
     * Insert new subroutine on current habit that is on modify
     */
    private void setOnClickInsert() {
        binding.subroutineModifyInsertSubroutineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemInsert();
            }
        });
    }

    private void onItemInsert() {

        isOnInsertSubroutine = true;

        SubroutineModifyParentItemAdapterDialogFragment dialog = new SubroutineModifyParentItemAdapterDialogFragment();

        dialog.setTargetFragment(getChildFragmentManager().findFragmentById(SubroutineModifyFragment.this.getId()), 1);
        dialog.show(getChildFragmentManager(), "ModifySubroutineOnClickDialog");

        dialog.setmOnInsertClickListener(new SubroutineModifyParentItemAdapterDialogFragment.OnInsertClickListener() {
            @Override
            public void onClickInsert(Subroutines subroutines) {
                subroutines.setFk_habit_uid(habit.getPk_habit_uid());
                habitWithSubroutinesViewModel.insertSubroutine(subroutines);
                updateTotalSubroutine();
                isOnInsertSubroutine = false;
            }

            @Override
            public void onDialogDismiss() {
                isOnInsertSubroutine = false;
            }
        });
    }

    private void updateTotalSubroutine() {
        final List<Subroutines> subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());
        habit.setTotal_subroutine(subroutinesList.size());
        habitWithSubroutinesViewModel.updateHabit(habit);
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnReturnSubroutine != null) mOnReturnSubroutine.returnSubroutineFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SubroutineConfigurationKeys.HABIT.getKey(), habit);
        outState.putBoolean(SubroutineConfigurationKeys.IS_ON_SUBROUTINE_UPDATE.getKey(), isOnUpdateSubroutine);
        outState.putInt(SubroutineConfigurationKeys.ITEM_POSITION.getKey(), item_position);
        outState.putBoolean(SubroutineConfigurationKeys.IS_ON_SUBROUTINE_INSERT.getKey(), isOnInsertSubroutine);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        habitWithSubroutinesViewModel = null;
        adapter = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOnReturnSubroutine != null) mOnReturnSubroutine = null;
    }
}