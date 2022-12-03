package com.habitdev.sprout.ui.onBoarding.personalizationAssessment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentAnalysisBinding;
import com.habitdev.sprout.enums.BundleKeys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class AnalysisFragment extends Fragment {

    private FragmentAnalysisBinding binding;
    private HabitWithSubroutinesViewModel habitWithSubroutinesViewModel;
    private List<Habits> habitsList;

    public AnalysisFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalysisBinding.inflate(inflater, container, false);

        //Recommender Algorithm Here to display result according to the analysis

        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);

        List<String> habitTitles = new ArrayList<>();

        habitWithSubroutinesViewModel.getAllHabitListLiveData().observe(getViewLifecycleOwner(), habits -> {
            for (Habits habit : habits) {
                habitTitles.add(habit.getHabit());
            }
            habitsList = habits;
//                habitWithSubroutinesViewModel.getAllHabitListLiveData().removeObservers(getViewLifecycleOwner());
        });

        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.adapter_analysis_parent_habit_item, habitTitles);
        binding.dropItems.setAdapter(adapterItems);

        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        AtomicInteger item_position = new AtomicInteger();

        binding.dropItems.setOnItemClickListener((adapterView, view, position, id) -> item_position.set(position + 1));

        binding.btnContinue.setOnClickListener(view -> {
            for (Habits habits : habitsList) {
                if (habits.getPk_habit_uid() == item_position.longValue()) {
                    habitWithSubroutinesViewModel.updateHabit(
                            new Habits(
                                    habits.getPk_habit_uid(),
                                    habits.getHabit(),
                                    habits.getDescription(),
                                    habits.getColor(),
                                    true,
                                    habits.isModifiable(),
                                    habits.getAbstinence(),
                                    habits.getRelapse(),
                                    new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault())
                                            .format(new Date()),
                                    habits.getTotal_subroutine(),
                                    habits.getCompleted_subroutine()
                            ));
                }
            }
            setOnBoarding();
            Bundle bundle = new Bundle();
            bundle.putBoolean(BundleKeys.ANALYSIS.getKEY(), true);
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_analysis_to_Home, bundle);
        });
    }

    private void setOnBoarding() {
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setOnBoarding();
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(requireContext(), "You shall not amend", Toast.LENGTH_SHORT).show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        habitWithSubroutinesViewModel.getAllHabitListLiveData().removeObservers(getViewLifecycleOwner());
        habitWithSubroutinesViewModel = null;
        binding = null;
    }
}