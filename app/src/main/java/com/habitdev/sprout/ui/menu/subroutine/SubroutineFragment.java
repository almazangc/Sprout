package com.habitdev.sprout.ui.menu.subroutine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.databinding.FragmentSubroutineBinding;
import com.habitdev.sprout.ui.menu.subroutine.adapter.SubroutineParentItemAdapter;
import com.habitdev.sprout.ui.menu.subroutine.ui.SubroutineModifyFragment;

import java.util.ArrayList;
import java.util.List;

public class SubroutineFragment extends Fragment implements SubroutineParentItemAdapter.OnClickListener, SubroutineModifyFragment.onClickBackPress {

    private FragmentSubroutineBinding binding;
    protected SubroutineModifyFragment subroutineModifyFragment = new SubroutineModifyFragment();

    public SubroutineFragment() {
        subroutineModifyFragment.setmOnClickBackPress(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSubroutineBinding.inflate(inflater, container, false);
        setRecyclerViewAdapter();
        onBackPress();
        return binding.getRoot();
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
        subroutineModifyFragment.setHabit(habit);
        getChildFragmentManager().beginTransaction()
                .addToBackStack(SubroutineFragment.this.getTag())
                .add(binding.subroutineFrameLayout.getId(), subroutineModifyFragment)
                .commit();
        binding.subroutineContainer.setVisibility(View.GONE);
    }

    /**
     * Interface from SubroutineModify Fragment
     */
    @Override
    public void returnSubroutineFragment() {
        getChildFragmentManager()
                .beginTransaction()
                .remove(subroutineModifyFragment)
                .commit();
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

    /**
     * On Fragment Destroy
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}