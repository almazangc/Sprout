package com.prototype.sprout.ui.menu.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prototype.sprout.database.habits_with_subroutines.HabitWithSubroutinesViewModel;
import com.prototype.sprout.database.habits_with_subroutines.Habits;
import com.prototype.sprout.databinding.FragmentHomeBinding;
import com.prototype.sprout.ui.menu.home.adapter.HomeParentAdapterItem;
import com.prototype.sprout.ui.menu.home.ui.AddDefaultHabitFragment;
import com.prototype.sprout.ui.menu.home.ui.AddNewHabitFragment;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeParentAdapterItem homeParentAdapterItem;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private List<Habits> habitsOnReform;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        setRecyclerViewAdapter();
        fabVisibility();
        onBackPress();
        return binding.getRoot();
    }

    private void setRecyclerViewAdapter() {
        binding.homeRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);

        habitsOnReform = habitWithSubroutinesViewModel.getAllHabitOnReform();
        homeParentAdapterItem = new HomeParentAdapterItem(habitsOnReform);
        binding.homeRecyclerView.setAdapter(homeParentAdapterItem);

        habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), habits -> {
            homeParentAdapterItem.setHabits(habits);
            habitsOnReform = habits;
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.END | ItemTouchHelper.START);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction){
                    case ItemTouchHelper.END:
                        long uid = habitsOnReform.get(viewHolder.getBindingAdapterPosition()).getPk_habit_uid();
                        habitWithSubroutinesViewModel.updateOnReformStatus(false, uid);
                        homeParentAdapterItem.notifyItemRemoved(viewHolder.getBindingAdapterPosition());
                        break;
                    case ItemTouchHelper.START:
                        //
                        break;
                }
            }
        });
        //Attach on Recycler View
        itemTouchHelper.attachToRecyclerView(binding.homeRecyclerView);

    }

    private void fabVisibility() {
        if (habitWithSubroutinesViewModel.getGetHabitOnReformCount() <= 1) {
            binding.homeFab.setVisibility(View.VISIBLE);
            binding.homeFab.setClickable(true);
            FabButton();
        } else {
            binding.homeFab.setVisibility(View.GONE);
            binding.homeFab.setClickable(false);
        }
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

    /**
     * Listens to the FAB Button Click
     */
    void FabButton() {
        binding.homeFab.setOnClickListener(view -> {
            FragmentManager fragmentManager = getChildFragmentManager();

            //Store in xml string
            String[] items = {"Choose from Predefined-list", "Add New Habit"};

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Add Habit on Reform");
            builder.setItems(items, (dialog, which) -> {
                switch (which) {
                    case 0:
                        AddDefaultHabitFragment addDefaultHabitFragment = new AddDefaultHabitFragment();
                        fragmentManager.beginTransaction().replace(binding.homeFrameLayout.getId(), addDefaultHabitFragment)
                                .commit();
                        binding.homeContainer.setVisibility(View.GONE);
                        break;
                    case 1:
                        AddNewHabitFragment addHabitHomeFragment = new AddNewHabitFragment();
                        fragmentManager.beginTransaction().replace(binding.homeFrameLayout.getId(), addHabitHomeFragment)
                                .commit();
                        binding.homeContainer.setVisibility(View.GONE);
                        break;
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeParentAdapterItem = null;
        habitWithSubroutinesViewModel = null;
        binding = null;
    }
}