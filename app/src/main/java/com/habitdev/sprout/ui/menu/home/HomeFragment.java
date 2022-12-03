package com.habitdev.sprout.ui.menu.home;

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
import androidx.recyclerview.widget.RecyclerView;

import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.databinding.FragmentHomeBinding;
import com.habitdev.sprout.interfaces.IRecyclerView;
import com.habitdev.sprout.ui.menu.home.adapter.HomeParentItemAdapter;
import com.habitdev.sprout.ui.menu.home.ui.HomeItemOnClickFragment;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeOnFabClickDialogFragment;
import com.habitdev.sprout.ui.menu.home.ui.fab_.custom_.AddNewHabitFragment;
import com.habitdev.sprout.ui.menu.home.ui.fab_.predefined_.AddDefaultHabitFragment;

import java.util.List;

public class HomeFragment extends Fragment implements IRecyclerView {

    private FragmentHomeBinding binding;
    private HomeParentItemAdapter homeParentItemAdapter;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private List<Habits> habitsOnReform;

    public HomeFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setRecyclerViewAdapter();

        binding.homeSwipeRefreshLayout.setOnRefreshListener(() -> {
            Toast.makeText(requireContext(), "Home Refresh, For Online Data Fetch", Toast.LENGTH_SHORT).show();
            binding.homeSwipeRefreshLayout.setRefreshing(false);
        });

        fabVisibility();
        onBackPress();
        return binding.getRoot();
    }

    private void setRecyclerViewAdapter() {
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);

        habitsOnReform = habitWithSubroutinesViewModel.getAllHabitOnReform();
        homeParentItemAdapter = new HomeParentItemAdapter(habitsOnReform, this, requireActivity(), getChildFragmentManager(), HomeFragment.this.getId());
        binding.homeRecyclerView.setAdapter(homeParentItemAdapter);

        recyclerViewObserver();
        recyclerViewItemTouchHelper();
    }

    private void recyclerViewItemTouchHelper() {
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
                        Habits habits = habitsOnReform.get(viewHolder.getBindingAdapterPosition());
                        habits.setOnReform(false);
                        habitWithSubroutinesViewModel.updateHabit(habits);
                        homeParentItemAdapter.notifyItemRemoved(viewHolder.getBindingAdapterPosition());
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
        habitWithSubroutinesViewModel.getGetHabitOnReformCount().observe(getViewLifecycleOwner(), count -> {
            if (count <= 1) {
                binding.homeFab.setVisibility(View.VISIBLE);
                binding.homeFab.setClickable(true);
                FabButton();
            } else {
                binding.homeFab.setVisibility(View.GONE);
                binding.homeFab.setClickable(false);
            }
        });
    }

    private void recyclerViewObserver() {
        habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), habits -> {
            homeParentItemAdapter.setHabits(habits);
            habitsOnReform = habits;
        });
    }

    @Override
    public void onItemClick(int position) {
        HomeItemOnClickFragment onClickFragment = new HomeItemOnClickFragment(habitsOnReform.get(position));
        getChildFragmentManager()
                .beginTransaction()
                .replace(binding.homeFrameLayout.getId(), onClickFragment)
                .commit();
        binding.homeContainer.setVisibility(View.GONE);
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Promp app exit, or exits
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void FabButton() {
        binding.homeFab.setOnClickListener(view -> {
            FragmentManager fragmentManager = getChildFragmentManager();

            //Store in xml string
//            String[] items = {"Choose from Predefined-list", "Add New Habit"};
//            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//            builder.setTitle("Add Habit on Reform");
//            builder.setItems(items, (dialog, which) -> {
//                switch (which) {
//                    case 0:
//                        AddDefaultHabitFragment addDefaultHabitFragment = new AddDefaultHabitFragment();
//                        fragmentManager.beginTransaction().replace(binding.homeFrameLayout.getId(), addDefaultHabitFragment)
//                                .commit();
//                        binding.homeContainer.setVisibility(View.GONE);
//                        break;
//                    case 1:
//                        AddNewHabitFragment addHabitHomeFragment = new AddNewHabitFragment();
//                        fragmentManager.beginTransaction().replace(binding.homeFrameLayout.getId(), addHabitHomeFragment)
//                                .commit();
//                        binding.homeContainer.setVisibility(View.GONE);
//                        break;
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();

            HomeOnFabClickDialogFragment dialog = new HomeOnFabClickDialogFragment();
            dialog.setTargetFragment(getChildFragmentManager()
                    .findFragmentById(HomeFragment.this.getId()), 1);
            dialog.show(getChildFragmentManager(), "HomeFabOnClickDialog");

            dialog.setOnClickListener(new HomeOnFabClickDialogFragment.onClickListener() {
                @Override
                public void onPredefinedClick() {
                    AddDefaultHabitFragment addDefaultHabitFragment = new AddDefaultHabitFragment();
                        fragmentManager.beginTransaction().replace(binding.homeFrameLayout.getId(), addDefaultHabitFragment)
                                .commit();
                        binding.homeContainer.setVisibility(View.GONE);
                }

                @Override
                public void onUserDefineClick() {
                    AddNewHabitFragment addHabitHomeFragment = new AddNewHabitFragment();
                    fragmentManager.beginTransaction().replace(binding.homeFrameLayout.getId(), addHabitHomeFragment)
                            .commit();
                    binding.homeContainer.setVisibility(View.GONE);
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeParentItemAdapter = null;
        habitWithSubroutinesViewModel = null;
        habitsOnReform = null;
        binding = null;
    }
}