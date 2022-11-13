package com.prototype.sprout.ui.menu.subroutine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.prototype.sprout.database.habit.Habit;
import com.prototype.sprout.database.habit.HabitViewModel;
import com.prototype.sprout.databinding.FragmentSubroutineBinding;
import com.prototype.sprout.ui.menu.subroutine.adapter.SubroutineParentAdapterItem;
import com.prototype.sprout.ui.menu.subroutine.ui.AddNewSubroutineFragment;

import java.util.List;

public class SubroutineFragment extends Fragment {

    FragmentSubroutineBinding binding;

    private SubroutineParentAdapterItem parentAdapterItem;
    private List<Habit> habitsOnReform;
    private HabitViewModel habitViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSubroutineBinding.inflate(inflater, container, false);

        habitViewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);
        binding.subroutineRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        habitsOnReform = habitViewModel.getAllHabitOnReform();
        parentAdapterItem = new SubroutineParentAdapterItem(requireActivity(),habitsOnReform);
        binding.subroutineRecyclerView.setAdapter(parentAdapterItem);

        habitViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), habitsOnReform -> {
            parentAdapterItem.setHabitsOnReform(habitsOnReform);
        });

        binding.fabSubroutine.setOnClickListener(view -> {
            FragmentManager fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction().replace(binding.subroutineFrameLayout.getId(), new AddNewSubroutineFragment())
                    .commit();
            binding.subroutineContainer.setVisibility(View.GONE);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}