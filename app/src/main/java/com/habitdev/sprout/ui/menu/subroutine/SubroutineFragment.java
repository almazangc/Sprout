package com.habitdev.sprout.ui.menu.subroutine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.databinding.FragmentSubroutineBinding;
import com.habitdev.sprout.ui.menu.subroutine.adapter.SubroutineParentItemAdapter;
import com.habitdev.sprout.ui.menu.subroutine.ui.AddNewSubroutineFragment;

import java.util.List;

public class SubroutineFragment extends Fragment {

    private FragmentSubroutineBinding binding;
    private SubroutineParentItemAdapter parentAdapterItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSubroutineBinding.inflate(inflater, container, false);
        setRecyclerViewAdapter();
        fabOnClick();
        onBackPress();
        return binding.getRoot();
    }

    private void setRecyclerViewAdapter() {
        HabitWithSubroutinesViewModel habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        binding.subroutineRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        List<Habits> habitsOnReform = habitWithSubroutinesViewModel.getAllHabitOnReform();
        parentAdapterItem = new SubroutineParentItemAdapter(requireActivity(), getViewLifecycleOwner(), habitsOnReform);
        binding.subroutineRecyclerView.setAdapter(parentAdapterItem);

        habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), habits -> {
            parentAdapterItem.setHabitsOnReform(habits);
        });
    }

    private void fabOnClick() {
        binding.fabSubroutine.setOnClickListener(view -> {
            FragmentManager fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction().replace(binding.subroutineFrameLayout.getId(), new AddNewSubroutineFragment())
                    .commit();
            binding.subroutineContainer.setVisibility(View.GONE);
        });
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
        parentAdapterItem = null;
        binding = null;
    }
}