package com.habitdev.sprout.ui.menu.home;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.habitdev.sprout.database.habits_with_subroutines.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habits_with_subroutines.Habits;
import com.habitdev.sprout.database.quotes.Quotes;
import com.habitdev.sprout.databinding.FragmentHomeBinding;
import com.habitdev.sprout.ui.menu.home.adapter.HomeParentAdapterItem;
import com.habitdev.sprout.ui.menu.home.ui.AddDefaultHabitFragment;
import com.habitdev.sprout.ui.menu.home.ui.AddNewHabitFragment;
import com.habitdev.sprout.utill.NetworkStateManager;

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

        binding.homeSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(requireContext(), "Home Refesh, For Online Data Fetch", Toast.LENGTH_SHORT).show();
                binding.homeSwipeRefreshLayout.setRefreshing(false);
            }
        });

        final Observer<Boolean> activeNetworkStateObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                Log.d("tag", "onChanged: " + isConnected);
            }
        };

        NetworkStateManager.getInstance().getNetworkConnectivityStatus().observe(requireActivity(), activeNetworkStateObserver);

        fabVisibility();
        onBackPress();
        return binding.getRoot();
    }

    private void setRecyclerViewAdapter() {
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);

        habitsOnReform = habitWithSubroutinesViewModel.getAllHabitOnReform();
        homeParentAdapterItem = new HomeParentAdapterItem(habitsOnReform);
        binding.homeRecyclerView.setAdapter(homeParentAdapterItem);

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
            homeParentAdapterItem.setHabits(habits);
            habitsOnReform = habits;
        });
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
        habitsOnReform = null;
        binding = null;
    }
}