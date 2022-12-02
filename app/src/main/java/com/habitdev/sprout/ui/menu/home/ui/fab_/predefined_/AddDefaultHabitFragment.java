package com.habitdev.sprout.ui.menu.home.ui.fab_.predefined_;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.habit.model.Subroutines;
import com.habitdev.sprout.databinding.FragmentAddDefaultHabitBinding;
import com.habitdev.sprout.enums.AppColor;
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
    private Habits habit;
    private List<Subroutines> subroutinesList;

    private final int ic_check;
    private int current_selected_color;
    private int old_selected_color;
    private String color;

    public AddDefaultHabitFragment(){
        habitsList = new ArrayList<>();
        this.ic_check = R.drawable.ic_check;
        this.current_selected_color = 0;
        this.old_selected_color = 0;
        this.color = AppColor.CLOUDS.getColor();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddDefaultHabitBinding.inflate(inflater, container, false);
        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        colorSelect();
        setHabitColor();
        upDateHabitList();
        addHabitOnReform();

        onBackPress();
        return binding.getRoot();
    }

    private void setDropDownItem(){
        List<String> habitTitles = new ArrayList<>();

        for (Habits habits : habitsList){
            habitTitles.add(habits.getHabit());
        }

        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.adapter_home_parent_habit_drop_down_item, habitTitles);
        binding.dropItems.setAdapter(adapterItems);

        binding.dropItems.setOnItemClickListener((adapterView, view, pos, id) -> {
            habit = habitsList.get(pos);
            updateUI();
        });
    }

    private void updateUI(){
        binding.habitDescription.setText(habit.getDescription());
        subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());

        HomeAddDefaultHabitParentItemAdapter subroutineItemAdapter = new HomeAddDefaultHabitParentItemAdapter(subroutinesList);
        binding.habitSubroutinesRecyclerView.setAdapter(subroutineItemAdapter);
        subroutineItemAdapter.setSubroutines(subroutinesList);
        binding.subroutineCountLbl.setText(String.format(Locale.getDefault(), "%d", subroutinesList.size()));
    }

    private void addHabitOnReform(){
        binding.addHabitOnReformBtn.setOnClickListener(view -> {
            habitWithSubroutinesViewModel.update(new Habits(
                    habit.getPk_habit_uid(),
                    habit.getHabit(),
                    habit.getDescription(),
                    color,
                    true,
                    habit.isModifiable(),
                    habit.getAbstinence(),
                    0,
                    new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault())
                            .format(new Date()),
                    subroutinesList.size(),
                   0
            ));
            habitWithSubroutinesViewModel.getAllHabitListLiveData().removeObservers(getViewLifecycleOwner());
            returnHomeFragment();
        });
    }

    /**
     * Filter List of Habits
     */
    private void upDateHabitList(){
        //change to synchronous and drop observer
        habitWithSubroutinesViewModel.getAllHabitListLiveData().observe(getViewLifecycleOwner(), habits -> {
            List<Habits> habitsLiveData = new ArrayList<>();
            for(Habits habit : habits)
                if (!habit.isOnReform() && !habit.isModifiable())
                    habitsLiveData.add(habit);

            habitsList = habitsLiveData;
//            updateUI();
            setDropDownItem();
        });
    }

    private void colorSelect() {
        binding.addFromDefaultHabitColorSelector.setOnClickListener(v -> {
            if (binding.addFromDefaultHabitMiscellaneous.getVisibility() == View.GONE) {
                binding.addFromDefaultHabitMiscellaneous.setVisibility(View.VISIBLE);
            } else {
                binding.addFromDefaultHabitMiscellaneous.setVisibility(View.GONE);
            }
        });

        binding.cloudMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(0);
            setSelected_color();
        });

        binding.alzarinMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(1);
            setSelected_color();
        });

        binding.amethystMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(2);
            setSelected_color();
        });

        binding.brightskyBlueMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(3);
            setSelected_color();
        });

        binding.nephritisMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(4);
            setSelected_color();
        });

        binding.sunflowerMisc.setOnClickListener(v -> {
            updateSelectedColorIndex(5);
            setSelected_color();
        });
    }

    private void setHabitColor() {
        if (habit != null){
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
            } else {
                old_selected_color = 1;
                setSelected_color();
            }
        } else {
            old_selected_color = 1;
            setSelected_color();
        }
    }

    private void setSelected_color() {
        if (old_selected_color != current_selected_color) {
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
    }

    private void setBackgroundColorIndicator(Drawable backgroundNoteIndicator) {
        binding.addFromDefaultHabitColorSelector.setBackground(backgroundNoteIndicator);
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