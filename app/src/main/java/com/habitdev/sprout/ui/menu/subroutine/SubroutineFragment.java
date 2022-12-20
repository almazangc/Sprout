package com.habitdev.sprout.ui.menu.subroutine;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.databinding.FragmentSubroutineBinding;
import com.habitdev.sprout.enums.SubroutineConfigurationKeys;
import com.habitdev.sprout.ui.menu.subroutine.adapter.SubroutineParentItemAdapter;
import com.habitdev.sprout.ui.menu.subroutine.ui.SubroutineModifyFragment;

public class SubroutineFragment extends Fragment
        implements
        SubroutineParentItemAdapter.OnClickListener,
        SubroutineModifyFragment.OnReturnSubroutine {

    private FragmentSubroutineBinding binding;
    private static SubroutineModifyFragment subroutineModifyFragment = new SubroutineModifyFragment();
    private static boolean isOnSubroutineModify;

    public SubroutineFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        subroutineModifyFragment.setmOnClickBackPress(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSubroutineBinding.inflate(inflater, container, false);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()){
            isOnSubroutineModify = savedInstanceState.getBoolean(SubroutineConfigurationKeys.IS_ON_SUBROUTINE_MODIFY.getValue());
        }
        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        setRecyclerViewAdapter();

        if (isOnSubroutineModify) {
            setSubroutineModifyFragment();
        }
    }

    /**
     * Initialized and sets adapter for recyclerView
     */
    private void setRecyclerViewAdapter() {
        HabitWithSubroutinesViewModel habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        binding.subroutineRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        SubroutineParentItemAdapter parentAdapterItem = new SubroutineParentItemAdapter();
        parentAdapterItem.setOldHabitList(habitWithSubroutinesViewModel.getAllHabitOnReform());

        setEmptyRVBackground(parentAdapterItem);

        parentAdapterItem.setmOnClickListener(this);
        parentAdapterItem.setSubroutineLifecycleOwner(getViewLifecycleOwner());
        parentAdapterItem.setHabitWithSubroutinesViewModel(habitWithSubroutinesViewModel);

        binding.subroutineRecyclerView.setAdapter(parentAdapterItem);

        habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), habits -> {
            parentAdapterItem.setNewHabitList(habits);
            setEmptyRVBackground(parentAdapterItem);
        });
    }

    /**
     * Displays No subroutine on reform
     *
     * @param adapter SubroutineParentItemAdapter
     */
    private void setEmptyRVBackground(SubroutineParentItemAdapter adapter) {
        if (adapter.getItemCount() > 0) {
            binding.subroutineEmptyLottieRecyclerView.setVisibility(View.INVISIBLE);
            binding.subroutineEmptyLbl.setVisibility(View.INVISIBLE);
        } else {
            binding.subroutineEmptyLottieRecyclerView.setVisibility(View.VISIBLE);
            binding.subroutineEmptyLbl.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Interface from SubroutineParentItemAdapter
     *
     * @param habit Habit on Modify
     */
    @Override
    public void onModifySubroutine(Habits habit) {
        isOnSubroutineModify = true;
        subroutineModifyFragment.setHabit(habit);
        setSubroutineModifyFragment();
    }

    private void setSubroutineModifyFragment() {
        final int TRANSITION = isOnSubroutineModify ? FragmentTransaction.TRANSIT_NONE : FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN;
        getChildFragmentManager().beginTransaction()
                .addToBackStack(SubroutineFragment.this.getTag())
                .add(binding.subroutineFrameLayout.getId(), subroutineModifyFragment)
                .setTransition(TRANSITION)
                .commit();
        binding.subroutineContainer.setVisibility(View.GONE);
    }

    /**
     * Interface from SubroutineModify Fragment
     */
    @Override
    public void returnSubroutineFragment() {
        isOnSubroutineModify = false;
        getChildFragmentManager()
                .beginTransaction()
                .remove(subroutineModifyFragment)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();
        subroutineModifyFragment = new SubroutineModifyFragment();
        subroutineModifyFragment.setmOnClickBackPress(this);
        binding.subroutineContainer.setVisibility(View.VISIBLE);
    }

    /**
     * Handle on BackPress
     */
    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().moveTaskToBack(true);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SubroutineConfigurationKeys.IS_ON_SUBROUTINE_MODIFY.getValue(), isOnSubroutineModify);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        subroutineModifyFragment.setmOnClickBackPress(null);
    }

    /**
     * On Fragment Destroy
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}