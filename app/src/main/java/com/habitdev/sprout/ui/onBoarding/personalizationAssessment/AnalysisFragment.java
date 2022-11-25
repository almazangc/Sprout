package com.habitdev.sprout.ui.onBoarding.personalizationAssessment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.habit.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.habit.model.Habits;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentAnalysisBinding;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.ui.menu.setting.SettingFragment;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalysisBinding.inflate(inflater, container, false);

        //Recommender Algorithm Here to display result according to the analysis

        habitWithSubroutinesViewModel = new ViewModelProvider(requireActivity()).get(HabitWithSubroutinesViewModel.class);
        habitWithSubroutinesViewModel.getAllHabitListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Habits>>() {
            @Override
            public void onChanged(List<Habits> habits) {
                habitsList = habits;
                habitWithSubroutinesViewModel.getAllHabitListLiveData().removeObservers(getViewLifecycleOwner());
            }
        });

        List<String> habitTitles = new ArrayList<>();
        for(Habits habits : habitsList){
            habitTitles.add(habits.getHabit());
        }

        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.adapter_analysis_parent_habit_item, habitTitles);
        binding.dropItems.setAdapter(adapterItems);


        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        AtomicInteger item_position = new AtomicInteger();

        binding.dropItems.setOnItemClickListener((adapterView, view, position, id) -> {
            item_position.set(position+1);
        });

        binding.btnContinue.setOnClickListener(view -> {
            Toast.makeText(requireContext(), "WHAT", Toast.LENGTH_SHORT).show();
            for (Habits habits : habitsList){
                    if (habits.getPk_habit_uid() == item_position.longValue()){
                        habitWithSubroutinesViewModel.update(
                                new Habits(
                                        habits.getPk_habit_uid(),
                                        habits.getHabit(),
                                        habits.getDescription(),
                                        true,
                                        habits.isModifiable(),
                                        habits.getAbstinence(),
                                        habits.getRelapse(),
                                        new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm a", Locale.getDefault())
                                                .format(new Date()),
                                        habits.getTotal_subroutine()
                                ));
                    }
                }
                Log.d("tag", "onClick: " + item_position.longValue());
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
        habitWithSubroutinesViewModel = null;
        binding = null;
    }
}