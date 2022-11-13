package com.prototype.sprout.ui.menu.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.prototype.sprout.database.habit.Habit;
import com.prototype.sprout.database.habit.HabitViewModel;
import com.prototype.sprout.databinding.FragmentHomeBinding;
import com.prototype.sprout.ui.menu.home.adapter.HabitAdapter;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private HabitAdapter habitAdapter;
    private List<Habit> habitsOnReform;
    private HabitViewModel habitViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.homeRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        habitViewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);

        habitsOnReform = habitViewModel.getAllHabitOnReform();
        Log.d("TAG", "Initial: "+ habitsOnReform.toString());
        habitAdapter = new HabitAdapter(habitsOnReform);
        binding.homeRecyclerView.setAdapter(habitAdapter);

        habitViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), habitsOnReform -> {
            habitAdapter.setHabits(habitsOnReform);
            Log.d("TAG", "LiveData: "+ habitsOnReform.toString());
        });

        binding.homeFab.setOnClickListener(view -> {
            Toast.makeText(requireContext(), "FAB Button", Toast.LENGTH_SHORT).show();

            String[] items = {"Choose from Predefined-list", "Add New Habit"};

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Add Habit on Reform");
            // add a list
            builder.setItems(items, (dialog, which) -> {
                switch (which) {
                    case 0: //
                        break;
                    case 1:
                        FragmentManager fragmentManager;
                        fragmentManager = getChildFragmentManager();
                        AddNewHabitFragment addHabitHomeFragment = new AddNewHabitFragment();
                        fragmentManager.beginTransaction().replace(binding.homeFrameLayout.getId(), addHabitHomeFragment)
                                .commit();
                        binding.homeContainer.setVisibility(View.GONE);
                        break;
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        onBackPress();
        return binding.getRoot();
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Do Something
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}