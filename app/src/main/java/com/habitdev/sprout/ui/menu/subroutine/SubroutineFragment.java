package com.habitdev.sprout.ui.menu.subroutine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.databinding.FragmentSubroutineBinding;
import com.habitdev.sprout.ui.menu.subroutine.adapter.SubroutineParentItemAdapter;
import com.habitdev.sprout.ui.menu.subroutine.ui.SubroutineModifyFragment;

import java.util.List;

public class SubroutineFragment extends Fragment implements SubroutineParentItemAdapter.OnClickListener {

    private FragmentSubroutineBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSubroutineBinding.inflate(inflater, container, false);
        setRecyclerViewAdapter();
        onBackPress();
        return binding.getRoot();
    }

    private void setRecyclerViewAdapter() {
        HabitWithSubroutinesViewModel habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        binding.subroutineRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        List<Habits> habitsOnReform = habitWithSubroutinesViewModel.getAllHabitOnReform();
        SubroutineParentItemAdapter parentAdapterItem = new SubroutineParentItemAdapter();
        parentAdapterItem.setHabitsOnReform(habitsOnReform);
        parentAdapterItem.setmOnClickListener(this);
        parentAdapterItem.setHabitWithSubroutinesViewModel(habitWithSubroutinesViewModel);
        parentAdapterItem.setLifecycleOwner(getViewLifecycleOwner());

        binding.subroutineRecyclerView.setAdapter(parentAdapterItem);

        habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), parentAdapterItem::setHabitsOnReform);
    }

    @Override
    public void onModifySubroutine(Habits habit) {
        getChildFragmentManager().beginTransaction()
                .replace(binding.subroutineFrameLayout.getId(), new SubroutineModifyFragment(habit))
                .commit();
        binding.subroutineContainer.setVisibility(View.GONE);
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Do something
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