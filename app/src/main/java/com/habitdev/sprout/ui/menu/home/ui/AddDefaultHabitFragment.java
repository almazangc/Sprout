package com.habitdev.sprout.ui.menu.home.ui;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentAddDefaultHabitBinding;
import com.habitdev.sprout.databinding.FragmentHomeBinding;
import com.habitdev.sprout.ui.menu.home.HomeFragment;
import com.habitdev.sprout.ui.menu.home.adapter.HomeAddDefaultHabitParentItemAdapter;
import com.habitdev.sprout.ui.menu.home.adapter.HomeParentItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddDefaultHabitFragment extends Fragment {

    private FragmentAddDefaultHabitBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private HomeAddDefaultHabitParentItemAdapter subroutineItemAdapter;
    private List<Habits> habitsList;
    private List<Subroutines> subroutinesList;

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

        List<String> habitTitles = new ArrayList<>();
        for(Habits habits : habitsList){
            habitTitles.add(habits.getHabit());
        }

        updateUI(0);

        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.adapter_home_parent_add_default_habit_item, habitTitles);
        binding.dropItems.setAdapter(adapterItems);

        binding.dropItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                updateUI(position);
            }
        });
    }

    private void updateUI(int position){
        binding.habitDescription.setText(habitsList.get(position).getDescription());

        subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habitsList.get(position).getPk_habit_uid());

        subroutineItemAdapter = new HomeAddDefaultHabitParentItemAdapter(subroutinesList);
        binding.habitSubroutinesRecyclerView.setAdapter(subroutineItemAdapter);

        subroutineItemAdapter.setSubroutines(subroutinesList);

        binding.subroutineCountLbl.setText(String.format(Locale.getDefault(), "%d", subroutinesList.size()));
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
        habitWithSubroutinesViewModel = null;
        habitsList = null;
        binding = null;
    }
}