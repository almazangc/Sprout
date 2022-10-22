package com.prototype.sprout.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.prototype.sprout.R;
import com.prototype.sprout.database.habit.HabitViewModel;
import com.prototype.sprout.databinding.FragmentBottomNavigationBinding;
import com.prototype.sprout.ui.home.journalNote.JournalFragment;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class BottomNavigationFragment extends Fragment {

    private FragmentBottomNavigationBinding binding;
    private FragmentManager fragmentManager;
    private Fragment fragment, Home, Task, Analytics, Settings, Journal;
    private HabitViewModel habitViewModel;

    public BottomNavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBottomNavigationBinding.inflate(inflater, container, false);

        Home = new HomeFragment();
        Task = new TaskFragment();
        Analytics = new AnalyticsFragment();
        Journal = new JournalFragment();
        Settings = new SettingsFragment();

        if(savedInstanceState == null){
            binding.bottomBar.selectTabById(R.id.tab_home, true);
            fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction().replace(binding.fragmentContainer.getId(), Home)
                    .commit();
        }

        binding.bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab LastTab, int newIndex, @NonNull AnimatedBottomBar.Tab newTab) {
                switch (newTab.getId()){
                    case R.id.tab_home:
                            fragment = Home;
                        break;
                    case R.id.tab_task:
                            fragment = Task;
                        break;
                    case R.id.tab_analytic:
                            fragment = Analytics;
                        break;
                    case R.id.tab_journal:
                            fragment = Journal;
                        break;
                    case R.id.tab_settings:
                            fragment = Settings;
                        break;
                    default:
                        fragment = Home;
                        break;
                }

                if(fragment != null){
                    fragmentManager = getChildFragmentManager();
                    fragmentManager.beginTransaction().replace(binding.fragmentContainer.getId(), fragment)
                            .commit();
                }
            }

            @Override
            public void onTabReselected(int lastIndex, @NonNull AnimatedBottomBar.Tab lastTab) {

            }
        });

//        habitViewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);
//
//        List<Habit> allHabit = habitViewModel.getAllhabitsList();
//        for (Habit habit: allHabit){
//            String desc = habit.getHabits();
//            ArrayList<Integer> arrayList = habit.getSubRoutineUID();
//            String list = ArrayListConverter.fromArrayList(arrayList);
//            Log.d("TAG", "DESC: " + desc + "\tREF: " + list);
//        }

        return binding.getRoot();
    }
}