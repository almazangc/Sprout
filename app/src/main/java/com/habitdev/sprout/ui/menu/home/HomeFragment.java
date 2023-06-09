package com.habitdev.sprout.ui.menu.home;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.achievement.AchievementViewModel;
import com.habitdev.sprout.database.achievement.model.Achievement;
import com.habitdev.sprout.database.habit.firestore.HabitFireStoreViewModel;
import com.habitdev.sprout.database.habit.model.firestore.HabitFireStore;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.model.room.Subroutines;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.databinding.FragmentHomeBinding;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.enums.HomeConfigurationKeys;
import com.habitdev.sprout.enums.TimeMilestone;
import com.habitdev.sprout.ui.menu.OnBackPressDialogFragment;
import com.habitdev.sprout.ui.menu.home.adapter.HomeParentItemAdapter;
import com.habitdev.sprout.ui.menu.home.ui.HomeItemOnClickFragment;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeOnFabClickDialogFragment;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeParentItemAdapterModifyDialogFragment;
import com.habitdev.sprout.ui.menu.home.ui.fab_.custom_.AddNewHabitFragment;
import com.habitdev.sprout.ui.menu.home.ui.fab_.predefined_.AddDefaultHabitFragment;
import com.habitdev.sprout.ui.dialog.CompletedAchievementDialogFragment;
import com.habitdev.sprout.utill.diffutils.DateTimeElapsedUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class HomeFragment extends Fragment
        implements
        HomeParentItemAdapter.HomeParentItemOnClickListener,
        AddDefaultHabitFragment.OnAddDefaultReturnHome,
        AddNewHabitFragment.OnAddNewHabitReturnHome,
        HomeItemOnClickFragment.OnItemOnClickReturnHome,
        HomeParentItemAdapterModifyDialogFragment.OnHabitModifyListener {

    private FragmentHomeBinding binding = null;
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
    private static boolean isToastShowing = false;
    private final HomeParentItemAdapter homeParentItemAdapter = new HomeParentItemAdapter();
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private HabitFireStoreViewModel habitFireStoreViewModel;
    private List<Habits> habitsList = null;
    private List<HabitFireStore> habitFireStoreList = null;

    public HomeFragment() {}

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

        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        habitFireStoreViewModel = new ViewModelProvider(requireActivity()).get(HabitFireStoreViewModel.class);
        habitFireStoreViewModel.fetchData();
        habitFireStoreList = habitFireStoreViewModel.getData();
        habitFireStoreViewModel.getLiveData().observe(getViewLifecycleOwner(), habitFireStores -> habitFireStoreList = habitFireStores);

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            HomeFragment.savedInstanceState = savedInstanceState;
        }

        checkUnlockedAchievement();
        setRecyclerViewAdapter();
        fabVisibility();
        onBackPress();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        showRecyclerViewTargetPrompt();
    }

    private void checkUnlockedAchievement() {
        if (habitWithSubroutinesViewModel.getAllHabitOnReformCount() == 1) {
            AchievementViewModel achievementViewModel = new ViewModelProvider(requireActivity()).get(AchievementViewModel.class);
            //TODO: UPDATE UID WHEN APPDATABASE CHANGE
            Achievement FIRST_STEP = achievementViewModel.getAchievementByUID(1);
            if (!FIRST_STEP.is_completed()) {
                FIRST_STEP.setIs_completed(true);
                FIRST_STEP.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                FIRST_STEP.setCurrent_progress(FIRST_STEP.getGoal_progress());
                achievementViewModel.updateAchievement(FIRST_STEP);

                CompletedAchievementDialogFragment dialog = new CompletedAchievementDialogFragment(FIRST_STEP.getTitle());
                dialog.setTargetFragment(getChildFragmentManager()
                        .findFragmentById(HomeFragment.this.getId()), 1);
                dialog.show(getChildFragmentManager(), "CompletedAchievementDialog");
            }
        }
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
        habitsList = habitWithSubroutinesViewModel.getAllHabitOnReform();

        homeParentItemAdapter.setOldHabitList(new ArrayList<>(habitsList));
        binding.homeRecyclerView.setAdapter(homeParentItemAdapter);

        setEmptyRVBackground(homeParentItemAdapter);

        setRecyclerViewObserver(homeParentItemAdapter);
    }

    private void setEmptyRVBackground(@NonNull HomeParentItemAdapter adapter) {
        if (adapter.getItemCount() > 0) {
            binding.homeEmptyLottieRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            binding.homeEmptyLottieRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Control Number of habits can be added with a maximum of N Habits
     */
    private void fabVisibility() {
        final int MAXIMUM_HABITS_CURRENTLY_ON_REFORM_STATUS = 5;

        binding.homeFab.setVisibility(View.VISIBLE);
        binding.homeFab.setClickable(true);
        binding.homeFab.setOnClickListener(view -> displayFabDialog());
        habitWithSubroutinesViewModel.getGetHabitOnReformCount().observe(getViewLifecycleOwner(), count -> {
            if (count < MAXIMUM_HABITS_CURRENTLY_ON_REFORM_STATUS) {
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
            habitsList.clear();
            habitsList = new ArrayList<>(habits);
            setEmptyRVBackground(homeParentItemAdapter);
        });

        habitWithSubroutinesViewModel.getAllHabitOnReformCountLiveData().observe(getViewLifecycleOwner(), habitOnReformCount -> {
            if (habitOnReformCount == 1) {
                Habits habit;
                if (habitsList == null)
                    return;

                if (habitsList.isEmpty())
                    return;
                else
                    habit = habitsList.get(0);

                if (habit.isModifiable())
                    binding.homeFabModify.setVisibility(View.VISIBLE);
                else
                    binding.homeFabModify.setVisibility(View.GONE);

                binding.homeFabRelapse.setVisibility(View.VISIBLE);
                binding.homeFabDrop.setVisibility(View.VISIBLE);

                homeParentItemAdapter.notifyDataSetChanged();

                Habits finalHabit = habit;
                if (binding.homeFabModify.getVisibility() == View.VISIBLE) {
                    binding.homeFabModify.setOnClickListener(view -> {
                        onClickHabitModify(finalHabit, 0);
                    });
                }

                binding.homeFabRelapse.setOnClickListener(view -> {
                        onClickHabitRelapse(finalHabit);
                });

                binding.homeFabDrop.setOnClickListener(view -> {
                    onClickHabitDrop(finalHabit);
                });

            } else {
                binding.homeFabModify.setVisibility(View.GONE);
                binding.homeFabRelapse.setVisibility(View.GONE);
                binding.homeFabDrop.setVisibility(View.GONE);
                homeParentItemAdapter.notifyDataSetChanged();
            }
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
        new AlertDialog.Builder(requireContext())
                .setMessage("Do you want to modify the habit?")
                .setCancelable(false)
                .setPositiveButton("YES", (dialogInterface, i) -> {
                    isOnModify = true;
                    habitOnModify = habit;
                    HomeFragment.position = position;

                    HomeParentItemAdapterModifyDialogFragment onModifydialogFragment = new HomeParentItemAdapterModifyDialogFragment(habit, position);
                    onModifydialogFragment.setAdapter_ref(homeParentItemAdapter);
                    onModifydialogFragment.setmOnHabitModifyListener(this);

                    onModifydialogFragment.setTargetFragment(getChildFragmentManager().findFragmentById(HomeFragment.this.getId()), 1);
                    onModifydialogFragment.show(getChildFragmentManager(), "Modify Habit Dialog");
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onDialogDismiss() {
        if (savedInstanceState != null && !savedInstanceState.isEmpty())
            savedInstanceState.putBoolean(HomeConfigurationKeys.IS_ON_MODIFY.getKey(), false);
        isOnModify = false;
    }

    @Override
    public void onClickHabitRelapse(Habits habit) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("HabitPreferences", MODE_PRIVATE);
        long lastRelapseTime = sharedPreferences.getLong(habit.getHabit() + "LastRelapseTime", 0);
        long timeDifference = System.currentTimeMillis() - lastRelapseTime;

        //1 hour limit till relapse will be recorded
        long relapseLimiter = convertToMilliseconds(1, 0, 0);

        if (timeDifference >= relapseLimiter) {
            showMotivationalMessage();
            //Increament Relapse Value
            habit.setRelapse(habit.getRelapse() + 1);
            habitWithSubroutinesViewModel.updateHabit(habit);
            sharedPreferences.edit().putLong(habit.getHabit() + "LastRelapseTime", System.currentTimeMillis()).apply();
        } else {
            if (!isToastShowing) {
                Toast.makeText(requireActivity(), convertToTime(timeDifference, relapseLimiter) + " till relapse will be available. You can to it.", Toast.LENGTH_SHORT).show();
                isToastShowing = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isToastShowing = false;
                    }
                }, 5 * 60 * 1000); // 5 minute delay time in milliseconds for the duration of the toast message
            } else {
                showMotivationalMessage();
            }
        }
    }

    /**
     * Display motivational message that help encourage not be affected by it.
     */
    private void showMotivationalMessage() {
        final String[] motivational_message = {
                "Don't be too hard on yourself. A relapse is a setback, not a failure.",
                "Remember why you started. Visualize the benefits of breaking this habit.",
                "Focus on progress, not perfection. Each try is a step towards your goal.",
                "Take it one day at a time. Focus on making good choices today.",
                "Talk to someone you trust for help and encouragement.",
                "Celebrate your successes, no matter how small. Each step counts.",
                "You've proven you can make positive changes.",
                "Don't let a setback stop you.",
                "Keep getting back up and trying again.",
                "You are capable of overcoming this habit. Believe in yourself.",
                "Believe in yourself.",
                "Setbacks are opportunities for growth. Learn something new each time.",
                "Don't give up on yourself. You are worth the effort.",
                "Breaking a bad habit is not a straight line. Keep moving forward.",
                "Believe in yourself! ʕっ•ᴥ•ʔっ",
                "Believe in yourself! (✿◠‿◠)",
                "Be proud of yourself for trying! (•̀o•́)ง",
                "Don't give up! Keep pushing! (ง'̀-'́)ง",
                "Embrace challenges and grow! (•̀ᴗ•́)و ̑̑",
                "Every small step counts! (＾▽＾)",
                "Every small step counts! Keep moving forward! (•́ε•̀٥)",
                "It's okay to make mistakes, learn from them and keep going! (╥﹏╥)",
                "Keep going, don't give up! ᕦ(ò_óˇ)ᕤ",
                "Keep pushing forward! (ง'̀-'́)ง✧",
                "Remember, progress is progress no matter how small! (＾▽＾)",
                "Success is just around the corner! \n(｡•̀ᴗ-)✧",
                "Strive for progress, not perfection! \nᕙ(⇀‸↼‶)ᕗ",
                "The only way to fail is to give up! \n(ง'̀-'́)ง",
                "You can do it! ٩(◕‿◕)۶",
                "You can do it! (ง •̀_•́)ง",
                "You are stronger than you think! \nᕦ(ò_óˇ)ᕤ",
                "You're amazing and capable of great things! (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧",
                "You've got this! (ง •̀_•́)ง",
                "You've got this! (ง'̀-'́)ง✧",
        };

        Random rand = new Random();
        int index = rand.nextInt(motivational_message.length);

        String message = motivational_message[index];

        Snackbar.make(binding.getRoot(), Html.fromHtml(message), Snackbar.LENGTH_LONG)
                .setAction("Dismiss", view -> {
                    //Dismiss snack bar
                })
                .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER))
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.NIGHT))
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.CLOUDS))
                .setDuration(5000) //5 seconds duration
                .show();
    }

    private long convertToMilliseconds(long hours, long minutes, long seconds) {
        return (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
    }

    @SuppressLint("DefaultLocale")
    private String convertToTime(long timeDifference, long relapseLimiter) {
        long timeRemaining = Math.abs(timeDifference - relapseLimiter);
        long minutes = (timeRemaining % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = ((timeRemaining % (1000 * 60 * 60)) % (1000 * 60)) / 1000;
        return String.format("%02dm:%02ds", minutes, seconds);
    }

    @Override
    public void onClickHabitDrop(Habits habit) {
        showOptionDialog(habit);
    }

    @Override
    public void onClickUpvoteHabit(Habits habit) {

        HabitFireStore habitFireStoreItem = new HabitFireStore();

        if (habitFireStoreList != null) {
            for (HabitFireStore habitFireStore : habitFireStoreList) {
                if (habitFireStore.getPk_uid() == habit.getPk_habit_uid() && habitFireStore.getTitle().equals(habit.getHabit())) {
                    habitFireStoreItem = habitFireStore;
                    habit.setUpvote(habitFireStoreItem.getUpvote());
                    habit.setDownvote(habitFireStoreItem.getDownvote());
                    break;
                }
            }

            if (habitFireStoreItem != null){
                habit.setUpvote(habitFireStoreItem.getUpvote());
                habit.setDownvote(habitFireStoreItem.getDownvote());
                habitWithSubroutinesViewModel.updateHabit(habit);
            }
        }

        Toast toast = Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT);
        switch (habit.getVote_status()) {
            case 0:
                habitFireStoreItem.setUpvote(habitFireStoreItem.getUpvote() + 1);
                habitFireStoreViewModel.updateHabit(habitFireStoreItem);
                habit.setUpvote(habit.getUpvote() + 1);
                habit.setVote_status(1);
                habitWithSubroutinesViewModel.updateHabit(habit);
                toast.setText("Upvoted");
                toast.show();
                break;
            case 1:
                toast.setText("Already upvoted");
                toast.show();
                break;
            case -1:
                habitFireStoreItem.setDownvote(habitFireStoreItem.getDownvote() - 1);
                habitFireStoreViewModel.updateHabit(habitFireStoreItem);

                habit.setDownvote(habit.getDownvote() - 1);
                habit.setVote_status(0);
                habitWithSubroutinesViewModel.updateHabit(habit);
                toast.setText("Downvote was removed");
                toast.show();
                break;
        }
    }

    @Override
    public void onClickDownvoteHabit(Habits habit) {
        HabitFireStore habitFireStoreItem = new HabitFireStore();

        if (habitFireStoreList != null) {
            for (HabitFireStore habitFireStore : habitFireStoreList) {
                if (habitFireStore.getPk_uid() == habit.getPk_habit_uid() && habitFireStore.getTitle().equals(habit.getHabit())) {
                    habitFireStoreItem = habitFireStore;
                    habit.setUpvote(habitFireStoreItem.getUpvote());
                    habit.setDownvote(habitFireStoreItem.getDownvote());
                    break;
                }
            }

            if (habitFireStoreItem != null){
                habit.setUpvote(habitFireStoreItem.getUpvote());
                habit.setDownvote(habitFireStoreItem.getDownvote());
                habitWithSubroutinesViewModel.updateHabit(habit);
            }
        }

        Toast toast = Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT);
        switch (habit.getVote_status()) {
            case 0:
                habitFireStoreItem.setDownvote(habitFireStoreItem.getDownvote() + 1);
                habitFireStoreViewModel.updateHabit(habitFireStoreItem);
                habit.setDownvote(habit.getDownvote() + 1);
                habit.setVote_status(-1);
                habitWithSubroutinesViewModel.updateHabit(habit);
                toast.setText("Downvoted");
                toast.show();
                break;
            case 1:
                habitFireStoreItem.setUpvote(habitFireStoreItem.getUpvote() - 1);
                habitFireStoreViewModel.updateHabit(habitFireStoreItem);
                habit.setUpvote(habit.getUpvote() - 1);
                habit.setVote_status(0);
                habitWithSubroutinesViewModel.updateHabit(habit);
                toast.setText("Upvote was removed");
                toast.show();
                break;
            case -1:
                toast.setText("Already downvoted");
                toast.show();
                break;
        }
    }

    private void showOptionDialog(Habits habit) {
        final DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(habit.getDate_started());
        dateTimeElapsedUtil.calculateElapsedDateTime();

        final String ARCHIVE = "Archive Habit";
        final String CANCEL = "Cancel";
        final String DROP = "Drop Habit";

        String[] options;

        if (dateTimeElapsedUtil.getElapsed_day() >= TimeMilestone.MIN_HABIT_BREAK_DAY.getDays()) {
            options = new String[3];
            options[0] = ARCHIVE;
            options[1] = DROP;
            options[2] = CANCEL;

            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("Select an option")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (options[which]) {
                                case CANCEL:
                                    dialog.dismiss();
                                    break;
                                case DROP:
                                    showConfirmationDialog(habit, 0);
                                    break;
                                case ARCHIVE:
                                    showConfirmationDialog(habit, 1);
                                    break;
                            }
                        }
                    });
            builder.create().show();
        } else {
            showConfirmationDialog(habit, 0);
        }
    }

    private void showConfirmationDialog(Habits habit, int type) {
        final String[] title = {"Are you sure you would like to drop the habit?", "Did you achieve your goal do you want to archive this habit?"};
        final String[] negativeButtonLabel = {"Stay", "No"};

        new AlertDialog.Builder(requireContext())
                .setMessage(title[type])
                .setCancelable(false)
                .setPositiveButton("YES", (dialogInterface, i) -> {
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

                        //remove stored shared pref of the pass relapse
                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("HabitPreferences", MODE_PRIVATE);
                        sharedPreferences.edit().remove(habit.getHabit() + "LastRelapseTime").apply();

                        Snackbar.make(binding.getRoot(), Html.fromHtml("<b>" + habit.getHabit() + "</b>: All progress has been lost"), Snackbar.LENGTH_LONG)
                                .setAction("Dismiss", view -> {
                                    //Dismiss snack bar
                                })
                                .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER))
                                .setTextColor(ContextCompat.getColor(requireContext(), R.color.NIGHT))
                                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.CLOUDS))
                                .show();
                    } else if (type == 1) {
                        habit.setOnReform(false);
                        habitWithSubroutinesViewModel.updateHabit(habit);

                        Snackbar.make(binding.getRoot(), Html.fromHtml("<b>" + habit.getHabit() + "</b>: All progress was saved"), Snackbar.LENGTH_LONG)
                                .setAction("Dismiss", view -> {
                                    //Dismiss snack bar
                                })
                                .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER))
                                .setTextColor(ContextCompat.getColor(requireContext(), R.color.NIGHT))
                                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.CLOUDS))
                                .show();
                    }
                })
                .setNegativeButton(negativeButtonLabel[type], null)
                .show();
    }

    private void onBackPress() {
        final int[] keypress_count = {0};
        final boolean[] isOnBackPressDialogShowing = {false};
        final boolean[] isAchievementDialogShowing = {false};

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                keypress_count[0]++;

                new CountDownTimer(200, 200) {
                    @Override
                    public void onTick(long l) {}

                    @Override
                    public void onFinish() {
                        if (keypress_count[0] > 1) {
                            //Dialog is displayed twice
                            OnBackPressDialogFragment dialog = new OnBackPressDialogFragment();
                            if (!isOnBackPressDialogShowing[0]) {
                                dialog.setTargetFragment(getChildFragmentManager().findFragmentById(HomeFragment.this.getId()), 2);
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

                            if (!isAchievementDialogShowing[0]) {
                                //TODO: UPDATE UID WHEN APPDATABASE CHANGE
                                AchievementViewModel achievementViewModel = new ViewModelProvider(requireActivity()).get(AchievementViewModel.class);
                                Achievement CLOSEAPPPROMPT = achievementViewModel.getAchievementByUID(26);

                                if (!CLOSEAPPPROMPT.is_completed()) {
                                    CLOSEAPPPROMPT.setIs_completed(true);
                                    CLOSEAPPPROMPT.setCurrent_progress(CLOSEAPPPROMPT.getCurrent_progress()+1);
                                    CLOSEAPPPROMPT.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                                    CLOSEAPPPROMPT.setTitle("Close Application");
                                    CLOSEAPPPROMPT.setDescription("Unlocked by pressing back button twice");
                                    achievementViewModel.updateAchievement(CLOSEAPPPROMPT);
                                    CompletedAchievementDialogFragment completedAchievementDialogFragment = new CompletedAchievementDialogFragment(CLOSEAPPPROMPT.getTitle());
                                    completedAchievementDialogFragment.setTargetFragment(getChildFragmentManager()
                                            .findFragmentById(HomeFragment.this.getId()), 1);
                                    completedAchievementDialogFragment.show(getChildFragmentManager(), "CompletedAchievementDialog");
                                    isAchievementDialogShowing[0] = true;
                                }
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
        final long[] predefinedHabitsOnReformCount = {0};
        final int MAXIMUM_PREDEFINED_HABITS_CURRENTLY_ON_REFORM_STATUS = 2;

        habitWithSubroutinesViewModel.getGetPredefinedHabitOnReformCount().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                predefinedHabitsOnReformCount[0] = aLong;
            }
        });

        isOnFabDialog = true;

        HomeOnFabClickDialogFragment dialog = new HomeOnFabClickDialogFragment();
        dialog.setTargetFragment(getChildFragmentManager()
                .findFragmentById(HomeFragment.this.getId()), 1);
        dialog.show(getChildFragmentManager(), "HomeFabOnClickDialog");

        dialog.setOnClickListener(new HomeOnFabClickDialogFragment.OnClickListener() {
            @Override
            public void onPredefinedClick() {
                if (!addDefaultHabitFragment.isAdded()) {
                    if (predefinedHabitsOnReformCount[0] < MAXIMUM_PREDEFINED_HABITS_CURRENTLY_ON_REFORM_STATUS) {
                        changeFragment(addDefaultHabitFragment);
                        isOnAddDefault = true;
                    } else {
                        Snackbar.make(binding.getRoot(), Html.fromHtml("Can only add " + MAXIMUM_PREDEFINED_HABITS_CURRENTLY_ON_REFORM_STATUS + " predefined habits on reform at the same time"), Snackbar.LENGTH_SHORT)
                                .setAction("Dismiss", view -> {
                                    //Dismiss snack bar
                                })
                                .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.PETER_RIVER))
                                .setTextColor(ContextCompat.getColor(requireContext(), R.color.NIGHT))
                                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.CLOUDS))
                                .setDuration(1500) //to seconds duration
                                .show();
                        isOnAddDefault = false;
                    }
                }
                isOnFabDialog = false;
            }

            @Override
            public void onUserNewHabitClick() {
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
        habitFireStoreViewModel = null;
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