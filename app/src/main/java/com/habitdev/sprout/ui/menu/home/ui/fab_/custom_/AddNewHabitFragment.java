package com.habitdev.sprout.ui.menu.home.ui.fab_.custom_;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentAddNewHabitBinding;
import com.habitdev.sprout.enums.AppColor;
import com.habitdev.sprout.ui.menu.home.adapter.HomeAddNewHabitParentAdapter;
import com.habitdev.sprout.ui.menu.home.enums.ConfigurationKeys;
import com.habitdev.sprout.ui.menu.home.ui.dialog.HomeAddNewInsertSubroutineDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AddNewHabitFragment extends Fragment
        implements HomeAddNewInsertSubroutineDialogFragment.onDialoagChange {

    private FragmentAddNewHabitBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private Habits habit;
    private List<Habits> habitsList;
    private List<Subroutines> subroutinesList;
    private Subroutines subroutine;

    private static int current_selected_color;
    private static int old_selected_color;
    private static String color = AppColor.CLOUDS.getColor();

    public interface onAddNewHabitReturnHome {
        void onAddNewHabitClickReturnHome();
    }

    private onAddNewHabitReturnHome mOnAddNewHabitReturnHome;

    public void setmOnReturnHome(onAddNewHabitReturnHome mOnAddNewHabitReturnHome) {
        this.mOnAddNewHabitReturnHome = mOnAddNewHabitReturnHome;
    }

    private HomeAddNewHabitParentAdapter homeAddNewHabitParentAdapter;

    private static Bundle savedInstanceState;

    public AddNewHabitFragment() {
        this.habit = new Habits(
                null,
                null,
                AppColor.CLOUDS.getColor(),
                false,
                true
        );
        this.subroutinesList = new ArrayList<>();
    }

    @Override
    public void addSubroutine(Subroutines subroutines) {
        this.subroutine = subroutines;
        subroutinesList.add(subroutine);
        setRecyclerViewAdapter();
    }

    @Override
    public void modifySubroutine(Subroutines subroutines) {
        this.subroutine = subroutines;
        setRecyclerViewAdapter();
    }

    @Override
    public void removeSubroutine(Subroutines subroutines) {
        this.subroutine = subroutines;
        subroutinesList.remove(subroutines);
        setRecyclerViewAdapter();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddNewHabitBinding.inflate(inflater, container, false);

        if (savedInstanceState != null)
            AddNewHabitFragment.savedInstanceState = savedInstanceState;

        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);

        setHint();
        setDropDownHabits();
        setCurrentTime();
        setHabitColor();
        colorSelect();
        insertSubroutine();
        setRecyclerViewAdapter();
        insertNewHabit();
        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (savedInstanceState != null) {

            current_selected_color = savedInstanceState.getInt(ConfigurationKeys.SELECTED_COLOR.getValue());
            setSelected_color();
            binding.addNewHabitHint.setText(
                    savedInstanceState.getString(ConfigurationKeys.HINT_TEXT.getValue())
            );
            binding.addNewHabitTitle.setText(
                    savedInstanceState.getString(ConfigurationKeys.TITLE.getValue())
            );
            binding.addNewHabitDescription.setText(
                    savedInstanceState.getString(ConfigurationKeys.DESCRIPTION.getValue())
            );

            int size = savedInstanceState.getInt(ConfigurationKeys.LIST_SIZE.getValue(), 0);
            subroutinesList.clear();
            for (int i = 0; i < size; i++) {
                subroutinesList.add((Subroutines) savedInstanceState.getSerializable(ConfigurationKeys.SUBROUTINE.getValue() + i));
            }
            setRecyclerViewAdapter();
            setDropDownHabits();
        }
    }

    private void setHint() {
        final String REQUIRED = "Required*";
        final String EMPTY_TITLE = "Empty Title*";
        final String EMPTY_DESC = "Empty Description*";
        final String MIN_SUBROUTINE = "Required Minimum of 2 Subroutine*";

        binding.addNewHabitTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty())
                    binding.addNewHabitHint.setText(REQUIRED);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.addNewHabitHint.setText(EMPTY_TITLE);
                } else {
                    if (binding.addNewHabitDescription.getText().toString().trim().isEmpty()) {
                        binding.addNewHabitHint.setText(EMPTY_DESC);
                    } else {
                        if (!binding.addNewHabitHint.getText().toString().trim().equals(MIN_SUBROUTINE)) {
                            binding.addNewHabitHint.setText("");
                        }
                    }
                }
            }
        });
        binding.addNewHabitDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty())
                    binding.addNewHabitHint.setText(REQUIRED);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.addNewHabitHint.setText(EMPTY_DESC);
                } else {
                    if (binding.addNewHabitTitle.getText().toString().trim().isEmpty()) {
                        binding.addNewHabitHint.setText(EMPTY_TITLE);
                    } else {
                        if (!binding.addNewHabitHint.getText().toString().trim().equals(MIN_SUBROUTINE)) {
                            binding.addNewHabitHint.setText("");
                        }
                    }
                }
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (binding != null) {
                            if (subroutinesList.isEmpty() || subroutinesList.size() < 2) {
                                if (binding.addNewHabitHint.getText().toString().trim().isEmpty() && !binding.addNewHabitHint.getText().toString().trim().equals(MIN_SUBROUTINE))
                                    binding.addNewHabitHint.setText(MIN_SUBROUTINE);
                            } else {
                                binding.addNewHabitHint.setText("");
                            }
                        } else {
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    private void setDropDownHabits() {
        List<String> habitTitles = new ArrayList<>();
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.adapter_home_parent_habit_drop_down_item, habitTitles);
        habitWithSubroutinesViewModel.getAllUserDefinedHabitListLiveData().observe(getViewLifecycleOwner(), habits -> {
            if (habits != null) {
                if (!habits.isEmpty()) {
                    List<Habits> onDropDownHabits = new ArrayList<>();
                    habitTitles.clear(); //clear contents
                    for (Habits habit : habits) {
                        if (!habit.isOnReform()) {
                            habitTitles.add(habit.getHabit());
                            onDropDownHabits.add(habit);
                        }
                    }
                    habitsList = onDropDownHabits;
                }
                if (habitTitles.isEmpty()) {
                    if (binding.addNewHabitTextInput.getVisibility() == View.VISIBLE)
                        binding.addNewHabitTextInput.setVisibility(View.GONE);
                } else {
                    if (binding.addNewHabitTextInput.getVisibility() == View.GONE)
                        binding.addNewHabitTextInput.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.addNewHabitDropItems.setAdapter(adapterItems);

        binding.addNewHabitDropItems.setOnItemClickListener((adapterView, view, pos, id) -> {
            habit = habitsList.get(pos);
            setDropDownContentView();
        });

        binding.addNewHabitDropItems.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.addNewHabitTitle.setText("");
                    binding.addNewHabitDescription.setText("");
                    color = AppColor.CLOUDS.getColor();
                    setHabitColor();
                    setSelected_color();
                    subroutinesList.clear();
                    setRecyclerViewAdapter();
                } else {
                    for (String title : habitTitles) {
                        if (editable.toString().trim().equals(title.trim())) {
                            binding.fabAddDeleteHabit.setVisibility(View.VISIBLE);
                            deleteHabit();
                            break;
                        } else {
                            binding.fabAddDeleteHabit.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    private void setDropDownContentView() {
        binding.addNewHabitTitle.setText(habit.getHabit());
        binding.addNewHabitDescription.setText(habit.getDescription());
        color = habit.getColor();
        setHabitColor();
        setSelected_color();
        setSubroutinesList(habit.getPk_habit_uid());
    }

    private void setSubroutinesList(long uid) {
        subroutinesList.clear();
        subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(uid);
        setRecyclerViewAdapter();
    }

    private void setCurrentTime() {
        binding.addNewHabitCurrentTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault()).format(new Date())
        );
    }

    private void colorSelect() {
        /*
            No need to toggle hide because on edit mode, gotta disable
         */
//        binding.addNewHabitColorSelector.setOnClickListener(v -> {
//            if (binding.addNewHabitLayoutMiscellaneous.getVisibility() == View.GONE) {
//                binding.addNewHabitLayoutMiscellaneous.setVisibility(View.VISIBLE);
//            } else {
//                binding.addNewHabitLayoutMiscellaneous.setVisibility(View.GONE);
//            }
//        });

        binding.cloudMisc.setOnClickListener(v -> updateSelectedColorIndex(0));

        binding.alzarinMisc.setOnClickListener(v -> updateSelectedColorIndex(1));

        binding.amethystMisc.setOnClickListener(v -> updateSelectedColorIndex(2));

        binding.brightskyBlueMisc.setOnClickListener(v -> updateSelectedColorIndex(3));

        binding.nephritisMisc.setOnClickListener(v -> updateSelectedColorIndex(4));

        binding.sunflowerMisc.setOnClickListener(v -> updateSelectedColorIndex(5));
    }

    private void setHabitColor() {
        if (habit.getColor().equals(AppColor.ALZARIN.getColor())) {
            current_selected_color = 1;
            setSelected_color();
        } else if (habit.getColor().equals(AppColor.AMETHYST.getColor())) {
            current_selected_color = 2;
            setSelected_color();
        } else if (habit.getColor().equals(AppColor.BRIGHT_SKY_BLUE.getColor())) {
            current_selected_color = 3;
            setSelected_color();
        } else if (habit.getColor().equals(AppColor.NEPHRITIS.getColor())) {
            current_selected_color = 4;
            setSelected_color();
        } else if (habit.getColor().equals(AppColor.SUNFLOWER.getColor())) {
            current_selected_color = 5;
            setSelected_color();
        } else if (habit.getColor().equals(AppColor.CLOUDS.getColor())) {
            current_selected_color = 0;
            setSelected_color();
        }
    }

    private void setSelected_color() {
        if (old_selected_color != current_selected_color) {
            int ic_check = R.drawable.ic_check;
            switch (current_selected_color) {
                case 1:
                    //alzarin
                    binding.alzarinSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_alzarin));
                    color = AppColor.ALZARIN.getColor();
                    break;
                case 2:
                    //amethyst
                    binding.amethystSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_amethyst));
                    color = AppColor.AMETHYST.getColor();
                    break;
                case 3:
                    //bright_sky_blue
                    binding.brightskyBlueSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_brightsky_blue));
                    color = AppColor.BRIGHT_SKY_BLUE.getColor();
                    break;
                case 4:
                    //nephritis
                    binding.nephritisSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_nephritis));
                    color = AppColor.NEPHRITIS.getColor();
                    break;
                case 5:
                    //sunflower
                    binding.sunflowerSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_sunflower));
                    color = AppColor.SUNFLOWER.getColor();
                    break;
                default:
                    //clouds night
                    binding.cloudSelected.setImageResource(ic_check);
                    setBackgroundColorIndicator(ContextCompat.getDrawable(requireContext(), R.drawable.background_color_indicator_clouds));
                    color = AppColor.CLOUDS.getColor();
                    break;
            }

            switch (old_selected_color) {
                case 1:
                    //alzarin
                    binding.alzarinSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 2:
                    //amethyst
                    binding.amethystSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 3:
                    //bright_sky_blue
                    binding.brightskyBlueSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 4:
                    //nephritis
                    binding.nephritisSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                case 5:
                    //sunflower
                    binding.sunflowerSelected.setImageResource(R.color.TRANSPARENT);
                    break;
                default:
                    //clouds night
                    binding.cloudSelected.setImageResource(R.color.TRANSPARENT);
                    break;
            }
        }
    }

    private void updateSelectedColorIndex(int newSelected) {
        old_selected_color = current_selected_color;
        current_selected_color = newSelected;
        setSelected_color();
    }

    private void setBackgroundColorIndicator(Drawable backgroundNoteIndicator) {
        binding.addNewHabitColorSelector.setBackground(backgroundNoteIndicator);
    }

    private void insertNewHabit() {
        binding.fabAddNewHabit.setOnClickListener(v -> {
            if (binding.addNewHabitHint.getText().toString().trim().isEmpty()) {
                habit.setHabit(binding.addNewHabitTitle.getText().toString().trim());
                habit.setDescription(binding.addNewHabitDescription.getText().toString().trim());
                habit.setOnReform(true);
                habit.setDate_started(binding.addNewHabitCurrentTime.getText().toString().trim());
                habit.setColor(color);
                habit.setTotal_subroutine(subroutinesList.size());

                long uid = habitWithSubroutinesViewModel.insertHabit(habit);

                if (!subroutinesList.isEmpty()) {
                    for (Subroutines subroutine : subroutinesList) {
                        subroutine.setFk_habit_uid(uid);
                    }
                    habitWithSubroutinesViewModel.insertSubroutines(subroutinesList);
                }
                if (mOnAddNewHabitReturnHome != null)
                    mOnAddNewHabitReturnHome.onAddNewHabitClickReturnHome();

                if (savedInstanceState != null) savedInstanceState = null;
            }
        });
    }

    private void insertSubroutine() {
        binding.addNewHabitSubroutineBtn.setOnClickListener(view -> {
            HomeAddNewInsertSubroutineDialogFragment dialog = new HomeAddNewInsertSubroutineDialogFragment();
            dialog.setTargetFragment(getChildFragmentManager()
                    .findFragmentById(AddNewHabitFragment.this.getId()), 1);
            dialog.show(getChildFragmentManager(), "HomeAddNewInsertSubroutineDialog");
        });
    }

    private void deleteHabit() {
        if (binding.fabAddDeleteHabit.getVisibility() == View.VISIBLE) {
            binding.fabAddDeleteHabit.setOnClickListener(view -> {
                //Delete Habit and its subroutines and comments
                habitWithSubroutinesViewModel.deleteHabit(habit);
                habitWithSubroutinesViewModel.deleteSubroutineList(subroutinesList);
                binding.fabAddDeleteHabit.setVisibility(View.GONE);
                habit = new Habits("", "", AppColor.CLOUDS.getColor(), true, true);
                subroutinesList = new ArrayList<>();
                setDropDownContentView();
                setDropDownHabits();
            });
        }
    }

    private void setRecyclerViewAdapter() {
        if (subroutinesList != null) {
            if (subroutinesList.isEmpty()) {
                binding.addNewHabitSubroutineCardView.setVisibility(View.GONE);
            } else {
                binding.addNewHabitSubroutineCardView.setVisibility(View.VISIBLE);
            }

            if (homeAddNewHabitParentAdapter == null)
                homeAddNewHabitParentAdapter = new HomeAddNewHabitParentAdapter(subroutinesList);

            homeAddNewHabitParentAdapter.setOnClickListener(new HomeAddNewHabitParentAdapter.OnClickListener() {
                @Override
                public void onDelete(Subroutines subroutine) {
                    HomeAddNewInsertSubroutineDialogFragment dialog = new HomeAddNewInsertSubroutineDialogFragment(subroutine, true);
                    dialog.setTargetFragment(getChildFragmentManager().findFragmentById(AddNewHabitFragment.this.getId()), 1);
                    dialog.show(getChildFragmentManager(), "TAG");
                }

                @Override
                public void onModify(Subroutines subroutine) {
                    HomeAddNewInsertSubroutineDialogFragment dialog = new HomeAddNewInsertSubroutineDialogFragment(subroutine);
                    dialog.setTargetFragment(getChildFragmentManager().findFragmentById(AddNewHabitFragment.this.getId()), 1);
                    dialog.show(getChildFragmentManager(), "TAG");
                }
            });

            if (binding.addNewHabitSubroutineRecyclerView.getAdapter() == null) {
                binding.addNewHabitSubroutineRecyclerView.setAdapter(homeAddNewHabitParentAdapter);
            } else {
                homeAddNewHabitParentAdapter.setSubroutinesList(subroutinesList);
            }
        }
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnAddNewHabitReturnHome != null)
                    mOnAddNewHabitReturnHome.onAddNewHabitClickReturnHome();
                if (savedInstanceState != null) savedInstanceState = null;
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConfigurationKeys.SELECTED_COLOR.getValue(), current_selected_color);

        if (binding != null) {
            outState.putString(ConfigurationKeys.HINT_TEXT.getValue(), binding.addNewHabitHint.getText().toString().trim());
            outState.putString(ConfigurationKeys.TITLE.getValue(), binding.addNewHabitTitle.getText().toString().trim());
            outState.putString(ConfigurationKeys.DESCRIPTION.getValue(), binding.addNewHabitDescription.getText().toString().trim());
        }

        //another way was parcelable list, no need for iteration
        if (!subroutinesList.isEmpty()) {
            int counter = 0;
            outState.putInt(ConfigurationKeys.LIST_SIZE.getValue(), subroutinesList.size());
            for (Subroutines subroutine : subroutinesList) {
                outState.putSerializable(ConfigurationKeys.SUBROUTINE.getValue() + counter, subroutine);
                counter++;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mOnAddNewHabitReturnHome = null;
        habitWithSubroutinesViewModel.getAllUserDefinedHabitListLiveData().removeObservers(getViewLifecycleOwner());
        habitWithSubroutinesViewModel = null;
        binding = null;
    }
}