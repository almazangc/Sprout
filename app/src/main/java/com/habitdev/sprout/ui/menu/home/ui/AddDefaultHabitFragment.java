package com.habitdev.sprout.ui.menu.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentAddDefaultHabitBinding;
import com.habitdev.sprout.ui.menu.home.HomeFragment;
import com.habitdev.sprout.ui.menu.home.adapter.HomeAddDefaultHabitParentItemAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddDefaultHabitFragment extends Fragment {

    private FragmentAddDefaultHabitBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private List<Habits> habitsList;
    private List<Subroutines> subroutinesList;
    private int position;

    public AddDefaultHabitFragment() {
        this.position = 0;
        habitsList = new ArrayList<>();
    }

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
        upDateHabitList();
        addHabitOnReform();
    }

    private void setDropDownItem(){
        List<String> habitTitles = new ArrayList<>();

        for (Habits habits : habitsList){
            habitTitles.add(habits.getHabit());
        }

        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.adapter_home_parent_add_default_habit_item, habitTitles);
        binding.dropItems.setAdapter(adapterItems);

        binding.dropItems.setOnItemClickListener((adapterView, view, pos, id) -> {
            position = pos;
            updateUI();
        });
    }

    private void updateUI(){
        binding.habitDescription.setText(habitsList.get(position).getDescription());
        subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habitsList.get(position).getPk_habit_uid());
        HomeAddDefaultHabitParentItemAdapter subroutineItemAdapter = new HomeAddDefaultHabitParentItemAdapter(subroutinesList);
        binding.habitSubroutinesRecyclerView.setAdapter(subroutineItemAdapter);
        subroutineItemAdapter.setSubroutines(subroutinesList);
        binding.subroutineCountLbl.setText(String.format(Locale.getDefault(), "%d", subroutinesList.size()));
    }

    private void addHabitOnReform(){
        binding.addHabitOnReformBtn.setOnClickListener(view -> {
            Habits habits = habitsList.get(position);
            habitWithSubroutinesViewModel.update(new Habits(
                    habits.getPk_habit_uid(),
                    habits.getHabit(),
                    habits.getDescription(),
                    true,
                    habits.isModifiable(),
                    habits.getAbstinence(),
                    habits.getRelapse(),
                    new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault())
                            .format(new Date()),
                    subroutinesList.size(),
                    habits.getCompleted_subroutine()
            ));
            habitWithSubroutinesViewModel.getAllHabitListLiveData().removeObservers(getViewLifecycleOwner());
            returnHomeFragment();
        });
    }

    private void upDateHabitList(){
        habitWithSubroutinesViewModel.getAllHabitListLiveData().observe(getViewLifecycleOwner(), habits -> {
            List<Habits> habitsLiveData = new ArrayList<>();
            for(Habits habit : habits) if (!habit.isOnReform() && !habit.isModifiable()) habitsLiveData.add(habit);
            habitsList = habitsLiveData;
            updateUI();
            setDropDownItem();
        });
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                returnHomeFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void returnHomeFragment(){
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(binding.addFromDefaultHabitFrameLayout.getId(), new HomeFragment())
                .commit();
        binding.addFromDefaultHabitContainer.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        habitWithSubroutinesViewModel = null;
        habitsList = null;
        binding = null;
    }
}