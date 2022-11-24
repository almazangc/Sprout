package com.habitdev.sprout.ui.menu.home.ui;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.databinding.FragmentAddDefaultHabitBinding;
import com.habitdev.sprout.databinding.FragmentHomeBinding;
import com.habitdev.sprout.ui.menu.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class AddDefaultHabitFragment extends Fragment {

    private FragmentAddDefaultHabitBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private List<Habits> habitsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddDefaultHabitBinding.inflate(inflater, container, false);
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        habitsList = habitWithSubroutinesViewModel.getAllHabits();
        List<Habits> habitsList = habitWithSubroutinesViewModel.getAllHabits();

        List<String> habitTitles = new ArrayList<>();
        for(Habits habits : habitsList){
            habitTitles.add(habits.getHabit());
        }

        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.adapter_home_parent_add_default_habit_item, habitTitles);
        binding.dropItems.setAdapter(adapterItems);
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().replace(binding.addFromDefaultHabitFrameLayout.getId(), new HomeFragment())
                        .commit();
                binding.addFromDefaultHabitContainer.setVisibility(View.GONE);
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