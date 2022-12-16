package com.habitdev.sprout.ui.menu.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentHomeBinding;
import com.habitdev.sprout.ui.menu.home.adapter.HomeParentItemAdapter;
import com.habitdev.sprout.ui.menu.home.enums.ConfigurationKeys;
import com.habitdev.sprout.ui.menu.home.ui.HomeItemOnClickFragment;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeOnFabClickDialogFragment;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeParentItemAdapterModifyDialogFragment;
import com.habitdev.sprout.ui.menu.home.ui.fab_.custom_.AddNewHabitFragment;
import com.habitdev.sprout.ui.menu.home.ui.fab_.predefined_.AddDefaultHabitFragment;

import java.util.List;

public class HomeFragment extends Fragment
        implements
        HomeParentItemAdapter.HomeParentItemOnClickListener,
        AddDefaultHabitFragment.onAddDefaultReturnHome,
        AddNewHabitFragment.onAddNewHabitReturnHome,
        HomeItemOnClickFragment.onItemOnClickReturnHome,
        HomeParentItemAdapterModifyDialogFragment.onHabitModifyListener {

    private FragmentHomeBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private List<Habits> habitsList;

    private static AddDefaultHabitFragment addDefaultHabitFragment = new AddDefaultHabitFragment();
    private static AddNewHabitFragment addNewHabitHomeFragment = new AddNewHabitFragment();
    private static HomeItemOnClickFragment homeItemOnClickFragment = new HomeItemOnClickFragment();
    private final HomeParentItemAdapter homeParentItemAdapter = new HomeParentItemAdapter();

    private static boolean isOnAddDefault = false;
    private static boolean isOnAddNew = false;
    private static boolean isOnItemClick = false;
    private static boolean isOnModify = false;
    private static boolean isOnFabDialog = false;

    private static int position;
    private static Habits habitOnModify;
    private static Bundle savedInstanceState;

    private static final String TAG = "tag";

    public HomeFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        Log.d(TAG, "onAttach: ");
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);
        addNewHabitHomeFragment.setmOnReturnHome(this);
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(this);
        homeParentItemAdapter.setHomeParentItemOnclickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.d(TAG, "onCreateView: Home");
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        if (savedInstanceState != null) {
            HomeFragment.savedInstanceState = savedInstanceState;
        }

        setRecyclerViewAdapter();
        fabVisibility();
        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
//        Log.d(TAG, "onStart: Home");
        if (savedInstanceState != null) {
//            Log.d(TAG, "onStart: savedInstanceState not null");
            isOnAddDefault = savedInstanceState.getBoolean(ConfigurationKeys.IS_ON_ADD_DEFAULT.getValue());
            isOnAddNew = savedInstanceState.getBoolean(ConfigurationKeys.IS_ON_ADD_NEW.getValue());
            isOnItemClick = savedInstanceState.getBoolean(ConfigurationKeys.IS_ON_ITEM_CLICK.getValue());
            isOnModify = savedInstanceState.getBoolean(ConfigurationKeys.IS_ON_MODIFY.getValue());

            if (isOnAddDefault) {
//                Log.d(TAG, "onStart: isOnAddDefault");
                addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);
                if (!addDefaultHabitFragment.isAdded()){
                    changeFragment(addDefaultHabitFragment);
                }
            }

            if (isOnAddNew) {
//                Log.d(TAG, "onStart: isOnAddNew");
                addNewHabitHomeFragment.setmOnReturnHome(this);
                if (!addNewHabitHomeFragment.isAdded()){
                    changeFragment(addNewHabitHomeFragment);
                }
            }

            if (isOnItemClick) {
//                Log.d(TAG, "onStart: isOnItemClick");
                position = savedInstanceState.getInt(ConfigurationKeys.POSITION.getValue());
                onItemClick(position);
            }

            if (isOnModify) {
//                Log.d(TAG, "onStart: isOnModify");
                habitOnModify = (Habits) (savedInstanceState).getSerializable(ConfigurationKeys.HABIT.getValue());
                position = savedInstanceState.getInt(ConfigurationKeys.POSITION.getValue());
                onClickHabitModify(habitOnModify, position);
            }

            if (isOnFabDialog) {
                displayFabDialog();
            }
        }
    }

    private void setRecyclerViewAdapter() {
//        Log.d(TAG, "setRecyclerViewAdapter: ");
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        habitsList = habitWithSubroutinesViewModel.getAllHabitOnReform();

        homeParentItemAdapter.setOldHabitList(habitsList);
        binding.homeRecyclerView.setAdapter(homeParentItemAdapter);

        setEmptyRVBackground(homeParentItemAdapter); // once

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall);
        binding.homeRecyclerView.setLayoutAnimation(animationController);

        recyclerViewObserver(homeParentItemAdapter);
        setRefreshListener();
//        recyclerViewItemTouchHelper(homeParentItemAdapter);
    }

    private void setRefreshListener() {
//        Log.d(TAG, "setRefreshListener: ");
        binding.homeSwipeRefreshLayout.setOnRefreshListener(() -> {
            Toast.makeText(requireContext(), "Home Refresh, For Online Data Fetch", Toast.LENGTH_SHORT).show();
            binding.homeSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setEmptyRVBackground(HomeParentItemAdapter adapter) {
//        Log.d(TAG, "setEmptyRVBackground: ");
        if (adapter.getItemCount() > 0) {
            binding.homeEmptyLottieRecyclerView.setVisibility(View.INVISIBLE);
            binding.homeEmptyLbl.setVisibility(View.INVISIBLE);
        } else {
            binding.homeEmptyLottieRecyclerView.setVisibility(View.VISIBLE);
            binding.homeEmptyLbl.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Control Number of habits can be added with a maximum of 2
     */
    private void fabVisibility() {
//        Log.d(TAG, "fabVisibility: ");
        binding.homeFab.setVisibility(View.VISIBLE);
        binding.homeFab.setClickable(true);
        binding.homeFab.setOnClickListener(view -> {
            displayFabDialog();
        });
//        habitWithSubroutinesViewModel.getGetHabitOnReformCount().observe(getViewLifecycleOwner(), count -> {
//            if (count <= 1) {
//                binding.homeFab.setVisibility(View.VISIBLE);
//                binding.homeFab.setClickable(true);
//                FabButton();
//            } else {
//                binding.homeFab.setVisibility(View.GONE);
//                binding.homeFab.setClickable(false);
//            }
//        });
    }

    private void recyclerViewObserver(HomeParentItemAdapter homeParentItemAdapter) {
//        Log.d(TAG, "recyclerViewObserver: ");
        habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), habits -> {
            homeParentItemAdapter.setNewHabitList(habits);
            habitsList = habits;
            setEmptyRVBackground(homeParentItemAdapter); //adapts on ui changes
        });
    }

    @Override
    public void onItemClick(int position) {
//        Log.d(TAG, "onItemClick: ");
        HomeFragment.position = position;
        isOnItemClick = true;

        homeItemOnClickFragment.setHabit(habitsList.get(position));
        homeItemOnClickFragment.setPosition(position);
        homeItemOnClickFragment.setAdapter_ref(homeParentItemAdapter);

        if (!homeItemOnClickFragment.isAdded()){
            getChildFragmentManager()
                    .beginTransaction()
                    .addToBackStack(HomeFragment.this.getTag())
                    .replace(binding.homeFrameLayout.getId(), homeItemOnClickFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN)
                    .commit();
        }
        binding.homeContainer.setVisibility(View.GONE);
    }

    @Override
    public void onClickHabitModify(Habits habit, int position) {
//        Log.d(TAG, "onClickHabitModify: ");
        isOnModify = true;
        habitOnModify = habit;
        HomeFragment.position = position;

        HomeParentItemAdapterModifyDialogFragment onModifydialogFragment = new HomeParentItemAdapterModifyDialogFragment(habit, position);
        onModifydialogFragment.setAdapter_ref(homeParentItemAdapter);
        onModifydialogFragment.setmOnHabitModifyListener(this);

        onModifydialogFragment.setTargetFragment(getChildFragmentManager().findFragmentById(HomeFragment.this.getId()), 1);
        onModifydialogFragment.show(getChildFragmentManager(), "Modify Habit Dialog");
    }

    @Override
    public void onDialogDismiss() {
//        Log.d(TAG, "onDialogDismiss: ");
        if (savedInstanceState != null)
            savedInstanceState.putBoolean(ConfigurationKeys.IS_ON_MODIFY.getValue(), false);
        isOnModify = false;
    }

    @Override
    public void onClickHabitRelapse(Habits habit) {
//        Log.d(TAG, "onClickHabitRelapse: ");
        habit.setRelapse(habit.getRelapse() + 1);
        habitWithSubroutinesViewModel.updateHabit(habit);
    }

    @Override
    public void onClickHabitDrop(Habits habit) {
//        Log.d(TAG, "onClickHabitDrop: ");
        habit.setOnReform(false);
        habit.setRelapse(0);
        habit.setCompleted_subroutine(0);
        habitWithSubroutinesViewModel.updateHabit(habit);

        List<Subroutines> subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());
        for (Subroutines subroutine : subroutinesList) {
            subroutine.setLongest_streak(0);
            subroutine.setMax_streak(0);
            subroutine.setMarkDone(false);
            subroutine.setTotal_skips(0);
            subroutine.setTotal_completed(0);
            habitWithSubroutinesViewModel.updateSubroutine(subroutine);
        }

        Snackbar.make(binding.getRoot(), Html.fromHtml("<b>" + habit.getHabit() + "</b>: all progress has been lost"), Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", view -> {
                })
                .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER))
                .setTextColor(getResources().getColor(R.color.NIGHT))
                .setBackgroundTint(getResources().getColor(R.color.CLOUDS))
                .show();
    }

    private void onBackPress() {

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
//                Log.d(TAG, "onBackPress: ");
                requireActivity().moveTaskToBack(true);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void displayFabDialog() {
        isOnFabDialog = true;

        HomeOnFabClickDialogFragment dialog = new HomeOnFabClickDialogFragment();
        dialog.setTargetFragment(getChildFragmentManager()
                .findFragmentById(HomeFragment.this.getId()), 1);
        dialog.show(getChildFragmentManager(), "HomeFabOnClickDialog");

        dialog.setOnClickListener(new HomeOnFabClickDialogFragment.onClickListener() {
            @Override
            public void onPredefinedClick() {
//                    List<Habits> habitsList = habitWithSubroutinesViewModel.getAllHabitListLiveData().getValue();
//                    List<Habits> availableHabits = new ArrayList<>();
//
//                    for (Habits habits : habitsList){
//                        if (!habits.isOnReform()) availableHabits.add(habits);
//                    }
//
//                    if (!availableHabits.isEmpty()) {
                if (!addDefaultHabitFragment.isAdded()){
                    changeFragment(addDefaultHabitFragment);
                }
                isOnAddDefault = true;
                isOnFabDialog = false;
//                    } else {
//                        Toast.makeText(requireActivity(), "No Available Habits", Toast.LENGTH_LONG).show();
//                    }
            }
            @Override
            public void onUserDefineClick() {
                if (!addNewHabitHomeFragment.isAdded()){
                    changeFragment(addNewHabitHomeFragment);
                }
                isOnAddNew = true;
                isOnFabDialog = false;
            }

            @Override
            public void onDismissDialog() {
                isOnFabDialog = false;
            }
        });
    }

    @Override
    public void onAddDefaultHabitClickReturnHome() {
        Log.d(TAG, "onAddDefaultHabitClickReturnHome: ");

        removeChildFragment(addDefaultHabitFragment);
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(null);

        addDefaultHabitFragment = new AddDefaultHabitFragment();
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);

        if (savedInstanceState != null)
            savedInstanceState.putBoolean(ConfigurationKeys.IS_ON_ADD_DEFAULT.getValue(), false);

        isOnAddDefault = false;
    }

    @Override
    public void onAddNewHabitClickReturnHome() {
//        Log.d(TAG, "onAddNewHabitClickReturnHome: ");
        removeChildFragment(addNewHabitHomeFragment);
        addNewHabitHomeFragment.setmOnReturnHome(null);
        addNewHabitHomeFragment = new AddNewHabitFragment();
        addNewHabitHomeFragment.setmOnReturnHome(this);
        if (savedInstanceState != null)
            savedInstanceState.putBoolean(ConfigurationKeys.IS_ON_ADD_NEW.getValue(), false);

        isOnAddNew = false;
    }

    @Override
    public void onHomeItemOnClickReturnHome() {
//        Log.d(TAG, "onHomeItemOnClickReturnHome: ");
        removeChildFragment(homeItemOnClickFragment);
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(null);
        homeItemOnClickFragment = new HomeItemOnClickFragment();
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(this);
        if (savedInstanceState != null)
            savedInstanceState.putBoolean(ConfigurationKeys.IS_ON_ITEM_CLICK.getValue(), false);

        isOnItemClick = false;
    }

    private void removeChildFragment(Fragment fragment) {
//        Log.d(TAG, "removeChildFragment: called");
        getChildFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();
        binding.homeContainer.setVisibility(View.VISIBLE);
    }

    private void changeFragment(Fragment fragment) {
//        Log.d(TAG, "changeFragment: called");

        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(HomeFragment.this.getTag())
                .add(binding.homeFrameLayout.getId(), fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN)
                .commit();


        binding.homeContainer.setVisibility(View.GONE);
    }

    /**
     * Handle Configuration Changes
     *
     * @param outState savedState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        Log.d(TAG, "onSaveInstanceState: ");
        outState.putBoolean(ConfigurationKeys.IS_ON_ADD_DEFAULT.getValue(), isOnAddDefault);
        outState.putBoolean(ConfigurationKeys.IS_ON_ADD_NEW.getValue(), isOnAddNew);

        outState.putBoolean(ConfigurationKeys.IS_ON_ITEM_CLICK.getValue(), isOnItemClick);
        if (isOnItemClick) {
            outState.putInt(ConfigurationKeys.POSITION.getValue(), position);
        }

        outState.putBoolean(ConfigurationKeys.IS_ON_MODIFY.getValue(), isOnModify);
        if (isOnModify) {
            outState.putSerializable(ConfigurationKeys.HABIT.getValue(), habitOnModify);
            outState.putInt(ConfigurationKeys.POSITION.getValue(), position);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.d(TAG, "onPause: Home");
//        if (savedInstanceState != null) {
//            savedInstanceState = null;
////            isOnModify = false;
////            isOnItemClick = false;
////            isOnAddNew = false;
////            isOnAddDefault = false;
//        }
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ConfigurationKeys.HOME_SHAREDPREF.getValue(), Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putBoolean(ConfigurationKeys.IS_ON_ADD_DEFAULT.getValue(), isOnAddDefault)
                .putBoolean(ConfigurationKeys.IS_ON_ADD_NEW.getValue(), isOnAddNew)
                .putBoolean(ConfigurationKeys.IS_ON_ITEM_CLICK.getValue(), isOnItemClick)
                .putBoolean(ConfigurationKeys.IS_ON_MODIFY.getValue(), isOnModify)
                .apply();

        if (isOnItemClick) {
            sharedPreferences.edit().putInt(ConfigurationKeys.POSITION.getValue(), position).apply();
        }

        if (isOnModify) {
            String json = new Gson().toJson(habitOnModify);
            sharedPreferences
                    .edit()
                    .putString(ConfigurationKeys.HABIT.getValue(), json)
                    .putInt(ConfigurationKeys.POSITION.getValue(), position).apply();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        Log.d(TAG, "onStop: Home");
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ConfigurationKeys.HOME_SHAREDPREF.getValue(), Context.MODE_PRIVATE);
        if (sharedPreferences.contains(ConfigurationKeys.IS_ON_ADD_DEFAULT.getValue()) ||
                sharedPreferences.contains(ConfigurationKeys.IS_ON_ADD_NEW.getValue()) ||
                sharedPreferences.contains(ConfigurationKeys.IS_ON_ITEM_CLICK.getValue()) ||
                sharedPreferences.contains(ConfigurationKeys.IS_ON_MODIFY.getValue())) {

            isOnAddDefault = sharedPreferences.getBoolean(ConfigurationKeys.IS_ON_ADD_DEFAULT.getValue(), false);
            isOnAddNew = sharedPreferences.getBoolean(ConfigurationKeys.IS_ON_ADD_NEW.getValue(), false);
            isOnItemClick = sharedPreferences.getBoolean(ConfigurationKeys.IS_ON_ITEM_CLICK.getValue(), false);
            isOnModify = sharedPreferences.getBoolean(ConfigurationKeys.IS_ON_MODIFY.getValue(), false);

            if (isOnAddDefault) {
//                Log.d(TAG, "onStart: isOnAddDefault");
                addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);
                if (!addDefaultHabitFragment.isAdded()){
                    changeFragment(addDefaultHabitFragment);
                }
            }

            if (isOnAddNew) {
//                Log.d(TAG, "onStart: isOnAddNew");
                addNewHabitHomeFragment.setmOnReturnHome(this);
                if (!addNewHabitHomeFragment.isAdded()){
                    changeFragment(addNewHabitHomeFragment);
                }
            }

            if (isOnItemClick) {
//                Log.d(TAG, "onStart: isOnItemClick");
                position = sharedPreferences.getInt(ConfigurationKeys.POSITION.getValue(), 0);
                onItemClick(position);
            }

            if (isOnModify) {
//                Log.d(TAG, "onStart: isOnModify");
                String json = sharedPreferences.getString(ConfigurationKeys.HABIT.getValue(), "");
                habitOnModify = new Gson().fromJson(json, Habits.class);
                position = sharedPreferences.getInt(ConfigurationKeys.POSITION.getValue(), 0);
                onClickHabitModify(habitOnModify, position);
            }

            if (isOnFabDialog) {
                displayFabDialog();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Log.d(TAG, "onDestroyView: Home");
        habitWithSubroutinesViewModel = null;
        habitsList = null;
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        Log.d(TAG, "onDetach: ");
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(null);
        addNewHabitHomeFragment.setmOnReturnHome(null);
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(null);
        homeParentItemAdapter.setHomeParentItemOnclickListener(null);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}