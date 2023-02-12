package com.habitdev.sprout.ui.menu.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.model.room.Subroutines;
import com.habitdev.sprout.databinding.FragmentHomeBinding;
import com.habitdev.sprout.enums.HomeConfigurationKeys;
import com.habitdev.sprout.enums.TimeMilestone;
import com.habitdev.sprout.ui.menu.OnBackPressDialogFragment;
import com.habitdev.sprout.ui.menu.home.adapter.HomeParentItemAdapter;
import com.habitdev.sprout.ui.menu.home.ui.HomeItemOnClickFragment;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeOnFabClickDialogFragment;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeParentItemAdapterModifyDialogFragment;
import com.habitdev.sprout.ui.menu.home.ui.fab_.custom_.AddNewHabitFragment;
import com.habitdev.sprout.ui.menu.home.ui.fab_.predefined_.AddDefaultHabitFragment;
import com.habitdev.sprout.utill.DateTimeElapsedUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment
        implements
        HomeParentItemAdapter.HomeParentItemOnClickListener,
        AddDefaultHabitFragment.OnAddDefaultReturnHome,
        AddNewHabitFragment.OnAddNewHabitReturnHome,
        HomeItemOnClickFragment.OnItemOnClickReturnHome,
        HomeParentItemAdapterModifyDialogFragment.OnHabitModifyListener {

    private static AddDefaultHabitFragment addDefaultHabitFragment = new AddDefaultHabitFragment();
    private static AddNewHabitFragment addNewHabitHomeFragment = new AddNewHabitFragment();
    private static HomeItemOnClickFragment homeItemOnClickFragment = new HomeItemOnClickFragment();
    private static boolean isOnAddDefault = false;
    private static boolean isOnAddNew = false;
    private static boolean isOnItemClick = false;
    private static boolean isOnModify = false;
    private static boolean isOnFabDialog = false;
    private static int position = 0;
    private static Habits habitOnModify = null;
    private static Bundle savedInstanceState = null;
    private final HomeParentItemAdapter homeParentItemAdapter = new HomeParentItemAdapter();
    private FragmentHomeBinding binding = null;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private List<Habits> habitsList = null;

    public HomeFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);
        addNewHabitHomeFragment.setmOnReturnHome(this);
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(this);
        homeParentItemAdapter.setHomeParentItemOnclickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
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
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            isOnAddDefault = savedInstanceState.getBoolean(HomeConfigurationKeys.IS_ON_ADD_DEFAULT.getKey());
            isOnAddNew = savedInstanceState.getBoolean(HomeConfigurationKeys.IS_ON_ADD_NEW.getKey());
            isOnItemClick = savedInstanceState.getBoolean(HomeConfigurationKeys.IS_ON_ITEM_CLICK.getKey());
            isOnModify = savedInstanceState.getBoolean(HomeConfigurationKeys.IS_ON_MODIFY.getKey());

            if (isOnAddDefault) {
                addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);
                if (!addDefaultHabitFragment.isAdded()) {
                    changeFragment(addDefaultHabitFragment);
                }
            }

            if (isOnAddNew) {
                addNewHabitHomeFragment.setmOnReturnHome(this);
                if (!addNewHabitHomeFragment.isAdded()) {
                    changeFragment(addNewHabitHomeFragment);
                }
            }

            if (isOnItemClick) {
                position = savedInstanceState.getInt(HomeConfigurationKeys.POSITION.getKey());
                onItemClick(position);
            }

            if (isOnModify) {
                habitOnModify = (Habits) (savedInstanceState).getSerializable(HomeConfigurationKeys.HABIT.getKey());
                position = savedInstanceState.getInt(HomeConfigurationKeys.POSITION.getKey());
                onClickHabitModify(habitOnModify, position);
            }

            if (isOnFabDialog) {
                displayFabDialog();
            }
        }
    }

    private void setRecyclerViewAdapter() {
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        habitsList = habitWithSubroutinesViewModel.getAllHabitOnReform();

        homeParentItemAdapter.setOldHabitList(new ArrayList<>(habitsList));
        binding.homeRecyclerView.setAdapter(homeParentItemAdapter);

        setEmptyRVBackground(homeParentItemAdapter);

//        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall);
//        binding.homeRecyclerView.setLayoutAnimation(animationController);

        setRecyclerViewObserver(homeParentItemAdapter);
//        recyclerViewItemTouchHelper(homeParentItemAdapter);
    }

    private void setEmptyRVBackground(@NonNull HomeParentItemAdapter adapter) {
        if (adapter.getItemCount() > 0) {
            binding.homeEmptyLottieRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            binding.homeEmptyLottieRecyclerView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Control Number of habits can be added with a maximum of 2 Habits
     */
    private void fabVisibility() {
        binding.homeFab.setVisibility(View.VISIBLE);
        binding.homeFab.setClickable(true);
        binding.homeFab.setOnClickListener(view -> displayFabDialog());
        habitWithSubroutinesViewModel.getGetHabitOnReformCount().observe(getViewLifecycleOwner(), count -> {
            if (count <= 5) { // 2 HABITS
                binding.homeFab.setVisibility(View.VISIBLE);
                binding.homeFab.setClickable(true);
            } else {
                binding.homeFab.setVisibility(View.GONE);
                binding.homeFab.setClickable(false);
            }
        });
    }

    private void setRecyclerViewObserver(@NonNull HomeParentItemAdapter homeParentItemAdapter) {
        habitWithSubroutinesViewModel.getAllHabitOnReformLiveData().observe(getViewLifecycleOwner(), habits -> {
            homeParentItemAdapter.setNewHabitList(new ArrayList<>(habits));
            habitsList = new ArrayList<>(habits);
            setEmptyRVBackground(homeParentItemAdapter); //adapts on ui changes
        });
    }

    @Override
    public void onItemClick(int position) {

        final int FRAGMENT_TRANSITION = !isOnItemClick ? FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN : FragmentTransaction.TRANSIT_NONE;

        HomeFragment.position = position;
        isOnItemClick = true;

        homeItemOnClickFragment.setHabit(habitsList.get(position));
        homeItemOnClickFragment.setPosition(position);
        homeItemOnClickFragment.setAdapter_ref(homeParentItemAdapter);

        if (!homeItemOnClickFragment.isAdded()) {
            getChildFragmentManager()
                    .beginTransaction()
                    .addToBackStack(HomeFragment.this.getTag())
                    .replace(binding.homeFrameLayout.getId(), homeItemOnClickFragment)
                    .setTransition(FRAGMENT_TRANSITION)
                    .commit();
        }
        binding.homeContainer.setVisibility(View.GONE);
    }

    @Override
    public void onClickHabitModify(Habits habit, int position) {
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
        if (savedInstanceState != null && !savedInstanceState.isEmpty())
            savedInstanceState.putBoolean(HomeConfigurationKeys.IS_ON_MODIFY.getKey(), false);
        isOnModify = false;
    }

//    @Override
//    public void onClickHabitRelapse(Habits habit) {
//        //it will only work once every 3 hours, use current time and subtract it for countdown
//        //also need to display message this is not spammable and can only add relapse once every hour
//        habit.setRelapse(habit.getRelapse() + 1);
//        habitWithSubroutinesViewModel.updateHabit(habit);
//    }

    @Override
    public void onClickHabitRelapse(Habits habit) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("HabitPreferences", MODE_PRIVATE);
        long lastRelapseTime = sharedPreferences.getLong(habit.getHabit() + "LastRelapseTime", 0);
        long timeDifference = System.currentTimeMillis() - lastRelapseTime;

        if (timeDifference >= convertToMilliseconds(3, 0, 0)) {
            habit.setRelapse(habit.getRelapse() + 1);
            habitWithSubroutinesViewModel.updateHabit(habit);
            sharedPreferences.edit().putLong(habit.getHabit() + "LastRelapseTime", System.currentTimeMillis()).apply();
        } else {
            Toast.makeText(requireActivity() , "Limited relapse every (3) hour, configurable time limit. You can do it.", Toast.LENGTH_SHORT).show();
        }
    }

    private long convertToMilliseconds(long hours, long minutes, long seconds) {
        return (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
    }

    @Override
    public void onClickHabitDrop(Habits habit) {
        showDialog(habit);
    }

    private void showDialog(Habits habit) {
        final DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(habit.getDate_started());
        dateTimeElapsedUtil.calculateElapsedDateTime();

        String[] options;
        if (dateTimeElapsedUtil.getElapsed_day() >= TimeMilestone.MIN_HABIT_BREAK_DAY.getDays()) {
            options = new String[3];
            options[0] = "Archive Habit";
            options[1] = "Drop Habit";
            options[2] = "Cancel";
        } else {
            options = new String[2];
            options[0] = "Drop Habit";
            options[1] = "Cancel";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Select an option")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (options[which].equals("Cancel")) {
                            dialog.dismiss();
                        } else if (options[which].equals("Drop Habit")) {
                            showConfirmationDialog(habit, 0);
                        } else if (options[which].equals("Archive Habit")){
                            showConfirmationDialog(habit, 1);
                        }
                    }
                });

        builder.create().show();
    }

    private void showConfirmationDialog(Habits habit, int type) {
        String title = "";

        if (type == 0) {
            title = "Are you sure you want to drop the habit?";
        }
        if (type == 1){
            title = "Did you achieve your goal do you want to archive this habit?";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(title)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform the action when the user confirms
                        if (type == 0) {
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

                            Snackbar.make(binding.getRoot(), Html.fromHtml("<b>" + habit.getHabit() + "</b>: All progress has been lost"), Snackbar.LENGTH_LONG)
                                    .setAction("Dismiss", view -> {
                                        //Dismiss snack bar
                                    })
                                    .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER))
                                    .setTextColor(getResources().getColor(R.color.NIGHT))
                                    .setBackgroundTint(getResources().getColor(R.color.CLOUDS))
                                    .show();
                        } else if (type == 1) {
                            habit.setOnReform(false);
                            habitWithSubroutinesViewModel.updateHabit(habit);

                            Snackbar.make(binding.getRoot(), Html.fromHtml("<b>" + habit.getHabit() + "</b>: All progress was saved"), Snackbar.LENGTH_LONG)
                                    .setAction("Dismiss", view -> {
                                        //Dismiss snack bar
                                    })
                                    .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER))
                                    .setTextColor(getResources().getColor(R.color.NIGHT))
                                    .setBackgroundTint(getResources().getColor(R.color.CLOUDS))
                                    .show();
                        }
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private void onBackPress() {
        final int[] keypress_count = {0};
        final boolean[] isOnBackPressDialogShowing = {false};

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                keypress_count[0]++;

                //toast msg double backpress to close app not minimize

                new CountDownTimer(200, 200) {
                    @Override
                    public void onTick(long l) {}

                    @Override
                    public void onFinish() {
                        if (keypress_count[0] > 1) {
                            //Dialog is displayed twice
                            OnBackPressDialogFragment dialog = new OnBackPressDialogFragment();
                            if (!isOnBackPressDialogShowing[0]) {
                                dialog.setTargetFragment(getChildFragmentManager().findFragmentById(HomeFragment.this.getId()), 1);
                                dialog.show(getChildFragmentManager(), "Menu.onBackPress");
                                dialog.setmOnCancelDialog(new OnBackPressDialogFragment.onCancelDialog() {
                                    @Override
                                    public void cancelDialog() {
                                        keypress_count[0] = 0;
                                        isOnBackPressDialogShowing[0] = false;
                                    }
                                });
                                isOnBackPressDialogShowing[0] = true;
                            }
                        } else {
                            requireActivity().moveTaskToBack(true);
                            keypress_count[0] = 0;
                        }
                        this.cancel();
                    }
                }.start();
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

        dialog.setOnClickListener(new HomeOnFabClickDialogFragment.OnClickListener() {
            @Override
            public void onPredefinedClick() {
                //WHEN ALL HABITS WAS ADDED
//                    List<Habits> habitsList = habitWithSubroutinesViewModel.getAllHabitListLiveData().getValue();
//                    List<Habits> availableHabits = new ArrayList<>();
//
//                    for (Habits habits : habitsList){
//                        if (!habits.isOnReform()) availableHabits.add(habits);
//                    }
//
//                    if (!availableHabits.isEmpty()) {
                if (!addDefaultHabitFragment.isAdded()) {
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
                if (!addNewHabitHomeFragment.isAdded()) {
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
        removeChildFragment(addDefaultHabitFragment);
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(null);
        addDefaultHabitFragment = new AddDefaultHabitFragment();
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);

        if (savedInstanceState != null) {
            savedInstanceState.putBoolean(HomeConfigurationKeys.IS_ON_ADD_DEFAULT.getKey(), false);
        }

        isOnAddDefault = false;
    }

    @Override
    public void onAddNewHabitClickReturnHome() {
        removeChildFragment(addNewHabitHomeFragment);
        addNewHabitHomeFragment.setmOnReturnHome(null);
        addNewHabitHomeFragment = new AddNewHabitFragment();
        addNewHabitHomeFragment.setmOnReturnHome(this);
        if (savedInstanceState != null)
            savedInstanceState.putBoolean(HomeConfigurationKeys.IS_ON_ADD_NEW.getKey(), false);

        isOnAddNew = false;
    }

    @Override
    public void onHomeItemOnClickReturnHome() {
        removeChildFragment(homeItemOnClickFragment);
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(null);

        homeItemOnClickFragment = new HomeItemOnClickFragment();
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(this);

        if (savedInstanceState != null && !savedInstanceState.isEmpty())
            savedInstanceState.putBoolean(HomeConfigurationKeys.IS_ON_ITEM_CLICK.getKey(), false);

        isOnItemClick = false;
    }

    private void removeChildFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();
        binding.homeContainer.setVisibility(View.VISIBLE);
    }

    private void changeFragment(Fragment fragment) {

        final int FRAGMENT_TRANSITION = (!isOnAddDefault && !isOnAddNew) ? FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN : FragmentTransaction.TRANSIT_NONE;

        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(HomeFragment.this.getTag())
                .add(binding.homeFrameLayout.getId(), fragment)
                .setTransition(FRAGMENT_TRANSITION)
                .commit();

        binding.homeContainer.setVisibility(View.GONE);
    }

    /**
     * Save states prior to configuration changes.
     *
     * @param outState savedState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(HomeConfigurationKeys.IS_ON_ADD_DEFAULT.getKey(), isOnAddDefault);
        outState.putBoolean(HomeConfigurationKeys.IS_ON_ADD_NEW.getKey(), isOnAddNew);
        outState.putBoolean(HomeConfigurationKeys.IS_ON_ITEM_CLICK.getKey(), isOnItemClick);
        if (isOnItemClick) {
            outState.putInt(HomeConfigurationKeys.POSITION.getKey(), position);
        }
    }

    /**
     * Save states on shared preferences when leaving home menu
     */
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(HomeConfigurationKeys.HOME_SHAREDPREF.getKey(), MODE_PRIVATE);
        sharedPreferences.edit()
                .putBoolean(HomeConfigurationKeys.IS_ON_ADD_DEFAULT.getKey(), isOnAddDefault)
                .putBoolean(HomeConfigurationKeys.IS_ON_ADD_NEW.getKey(), isOnAddNew)
                .putBoolean(HomeConfigurationKeys.IS_ON_ITEM_CLICK.getKey(), isOnItemClick)
                .putBoolean(HomeConfigurationKeys.IS_ON_MODIFY.getKey(), isOnModify)
                .apply();

        if (isOnItemClick)
            sharedPreferences.edit().putInt(HomeConfigurationKeys.POSITION.getKey(), position).apply();
    }

    /**
     * Restore states from shared preferences when re-entering home menu
     */
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(HomeConfigurationKeys.HOME_SHAREDPREF.getKey(), MODE_PRIVATE);

        if (!sharedPreferences.getAll().isEmpty()) {
            isOnAddDefault = sharedPreferences.getBoolean(HomeConfigurationKeys.IS_ON_ADD_DEFAULT.getKey(), false);
            isOnAddNew = sharedPreferences.getBoolean(HomeConfigurationKeys.IS_ON_ADD_NEW.getKey(), false);
            isOnItemClick = sharedPreferences.getBoolean(HomeConfigurationKeys.IS_ON_ITEM_CLICK.getKey(), false);
            isOnModify = sharedPreferences.getBoolean(HomeConfigurationKeys.IS_ON_MODIFY.getKey(), false);

            if (isOnAddDefault) {
                addDefaultHabitFragment.setmOnAddDefaultReturnHome(this);
                if (!addDefaultHabitFragment.isAdded()) {
                    changeFragment(addDefaultHabitFragment);
                }
            }

            if (isOnAddNew) {
                addNewHabitHomeFragment.setmOnReturnHome(this);
                if (!addNewHabitHomeFragment.isAdded()) {
                    changeFragment(addNewHabitHomeFragment);
                }
            }

            if (isOnItemClick) {
                position = sharedPreferences.getInt(HomeConfigurationKeys.POSITION.getKey(), 0);
                onItemClick(position);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        habitWithSubroutinesViewModel = null;
        habitsList = null;
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addDefaultHabitFragment.setmOnAddDefaultReturnHome(null);
        addNewHabitHomeFragment.setmOnReturnHome(null);
        homeItemOnClickFragment.setmOnItemOnClickReturnHome(null);
        homeParentItemAdapter.setHomeParentItemOnclickListener(null);
    }
}