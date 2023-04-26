package com.habitdev.sprout.ui.menu;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.R;
import com.habitdev.sprout.activity.startup.Main;
import com.habitdev.sprout.database.achievement.AchievementViewModel;
import com.habitdev.sprout.database.achievement.model.Achievement;
import com.habitdev.sprout.databinding.FragmentBottomNavigationBinding;
import com.habitdev.sprout.ui.menu.analytic.AnalyticFragment;
import com.habitdev.sprout.ui.menu.home.HomeFragment;
import com.habitdev.sprout.ui.menu.journal.JournalFragment;
import com.habitdev.sprout.ui.menu.setting.SettingFragment;
import com.habitdev.sprout.ui.menu.subroutine.SubroutineFragment;
import com.habitdev.sprout.ui.dialog.CompletedAchievementDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class BottomNavigationFragment extends Fragment {

    private FragmentBottomNavigationBinding binding;

    private int last_menu_selected, last_selected_index;

    private HomeFragment Home = new HomeFragment();
    private SubroutineFragment Subroutine = new SubroutineFragment();
    private AnalyticFragment Analytics = new AnalyticFragment();
    private JournalFragment Journal = new JournalFragment();
    private SettingFragment Settings = new SettingFragment();

    private Drawable amethyst, sunflower, nephritis, bright_sky_blue, alzarin;

    public BottomNavigationFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBottomNavigationBinding.inflate(inflater, container, false);

        amethyst = ContextCompat.getDrawable(requireContext(), R.drawable.background_bottom_bar_view_amethyst);
        sunflower = ContextCompat.getDrawable(requireContext(), R.drawable.background_bottom_bar_view_sunflower);
        nephritis = ContextCompat.getDrawable(requireContext(), R.drawable.background_bottom_bar_view_nephritis);
        bright_sky_blue = ContextCompat.getDrawable(requireContext(), R.drawable.background_bottom_bar_view_brightsky_blue);
        alzarin = ContextCompat.getDrawable(requireContext(), R.drawable.background_bottom_bar_view_alzarin);

        setTheme();

        SwipeListener swipeListener = new SwipeListener();

        if (savedInstanceState == null) {
            binding.bottomBar.selectTabById(R.id.tab_home, true);
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(binding.mainNavFragmentContainer.getId(), Home)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            binding.bottomBar.selectTabAt(0, true);
//            binding.bottomBar.setIndicatorColor(getResources().getColor(R.color.ALIZARIN));
            binding.bottomBarView.setBackground(alzarin);

        } else {
            setMenu(last_menu_selected, last_selected_index);
        }

        binding.bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab LastTab, int newIndex, @NonNull AnimatedBottomBar.Tab newTab) {
                last_menu_selected = newTab.getId();
                last_selected_index = newIndex;
                setMenu(newTab.getId(), newIndex);
            }

            @Override
            public void onTabReselected(int lastIndex, @NonNull AnimatedBottomBar.Tab lastTab) {

            }
        });
        return binding.getRoot();
    }

    private void setTheme() {
        final String SharedPreferences_KEY = "SP_DB";
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SharedPreferences_KEY, Main.MODE_PRIVATE);

        final String SHARED_PREF_KEY = "THEME_SHARED.PREF";
        int theme = sharedPreferences.getInt(SHARED_PREF_KEY, -1);
        if (theme == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (theme == 2) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("last_menu_selected", last_menu_selected);
        outState.putInt("last_selected_index", last_selected_index);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        setSavedInstance(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSavedInstance(savedInstanceState);
    }

    private void setSavedInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            last_menu_selected = savedInstanceState.getInt("last_menu_selected");
            last_selected_index = savedInstanceState.getInt("last_selected_index");
        }
    }

    private void setMenu(int id, int tabID) {

        if (id == R.id.tab_subroutine) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(binding.mainNavFragmentContainer.getId(), Subroutine)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
//            binding.bottomBar.setIndicatorColor(getResources().getColor(R.color.AMETHYST));
            binding.bottomBarView.setBackground(amethyst);
        } else if (id == R.id.tab_analytic) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(binding.mainNavFragmentContainer.getId(), Analytics)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
//            binding.bottomBar.setIndicatorColor(getResources().getColor(R.color.BRIGHT_SKY_BLUE));
            binding.bottomBarView.setBackground(bright_sky_blue);
        } else if (id == R.id.tab_journal) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(binding.mainNavFragmentContainer.getId(), Journal)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
//            binding.bottomBar.setIndicatorColor(getResources().getColor(R.color.NEPHRITIS));
            binding.bottomBarView.setBackground(nephritis);
        } else if (id == R.id.tab_settings) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(binding.mainNavFragmentContainer.getId(), Settings)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
//            binding.bottomBar.setIndicatorColor(getResources().getColor(R.color.SUNFLOWER));
            binding.bottomBarView.setBackground(sunflower);
        } else {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(binding.mainNavFragmentContainer.getId(), Home)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
//            binding.bottomBar.setIndicatorColor(getResources().getColor(R.color.ALIZARIN));
            binding.bottomBarView.setBackground(alzarin);
        }

        binding.bottomBar.selectTabAt(tabID, true);
    }

    /**
     * Listener on Touch Swipe Action Handle Navigation Through Swiping
     */
    @SuppressLint("ClickableViewAccessibility")
    private class SwipeListener implements View.OnTouchListener {

        private final int SWIPE_THRESHOLD = 100;
        private final int SWIPE_VELOCITY_THRESHOLD = 100;
        private final GestureDetector GESTURE_DETECTOR = new GestureDetector(new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {}

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;

                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                //TODO: UPDATE UID WHEN APPDATABASE CHANGE
                                AchievementViewModel achievementViewModel = new ViewModelProvider(requireActivity()).get(AchievementViewModel.class);
                                Achievement RIGTHSWIPE = achievementViewModel.getAchievementByUID(24);
                                if (!RIGTHSWIPE.is_completed()) {
                                    RIGTHSWIPE.setIs_completed(true);
                                    RIGTHSWIPE.setCurrent_progress(RIGTHSWIPE.getCurrent_progress()+1);
                                    RIGTHSWIPE.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                                    RIGTHSWIPE.setTitle("RIGHT SWIPE");
                                    RIGTHSWIPE.setDescription("Unlocked by right swiping on top bar to navigate menus");
                                    achievementViewModel.updateAchievement(RIGTHSWIPE);
                                    displayAchievementDialog(RIGTHSWIPE);
                                }
                                onSwipeRight();
                            } else {
                                //TODO: UPDATE UID WHEN APPDATABASE CHANGE
                                AchievementViewModel achievementViewModel = new ViewModelProvider(requireActivity()).get(AchievementViewModel.class);
                                Achievement LEFTSWIPE = achievementViewModel.getAchievementByUID(25);
                                if (!LEFTSWIPE.is_completed()) {
                                    LEFTSWIPE.setIs_completed(true);
                                    LEFTSWIPE.setCurrent_progress(LEFTSWIPE.getCurrent_progress()+1);
                                    LEFTSWIPE.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                                    LEFTSWIPE.setTitle("LEFT SWIPE");
                                    LEFTSWIPE.setDescription("Unlocked by left swiping on top bar  to navigate menus");
                                    achievementViewModel.updateAchievement(LEFTSWIPE);
                                    displayAchievementDialog(LEFTSWIPE);
                                }
                                onSwipeLeft();
                            }
                        }
                        result = true;
                    }
                    result = true;

                } catch (Exception exception) {
                    exception.printStackTrace();
                    Log.d("tag", "onFling: Bottom Swipe --> " + exception.getMessage());
                }
                return result;
            }

            private void displayAchievementDialog(Achievement achievement) {
                CompletedAchievementDialogFragment dialog = new CompletedAchievementDialogFragment(achievement.getTitle());
                dialog.setTargetFragment(getChildFragmentManager()
                        .findFragmentById(BottomNavigationFragment.this.getId()), 1);
                dialog.show(getChildFragmentManager(), "CompletedAchievementDialog");
            }
        });

        public SwipeListener() {
            binding.mainNavFragmentContainer.setOnTouchListener(this);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return GESTURE_DETECTOR.onTouchEvent(motionEvent);
        }

        void onSwipeRight() {
            switch (last_selected_index) {
                case 0:
                    updateFragment(4);
                    break;
                case 1:
                    updateFragment(0);
                    break;
                case 2:
                    updateFragment(1);
                    break;
                case 3:
                    updateFragment(2);
                    break;
                case 4:
                    updateFragment(3);
                    break;
            }
        }

        void onSwipeLeft() {
            switch (last_selected_index) {
                case 0:
                    updateFragment(1);
                    break;
                case 1:
                    updateFragment(2);
                    break;
                case 2:
                    updateFragment(3);
                    break;
                case 3:
                    updateFragment(4);
                    break;
                case 4:
                    updateFragment(0);
                    break;
            }
        }

//        void onSwipeTop() {
////            Toast.makeText(requireContext(), "Top Swipe", Toast.LENGTH_SHORT).show();
//        }
//
//        void onSwipeBottom() {
////            Toast.makeText(requireContext(), "Bottom Swipe", Toast.LENGTH_SHORT).show();
//        }

        void updateFragment(int newTab) {
            binding.bottomBar.selectTabAt(newTab, true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Home = null;
        Subroutine = null;
        Analytics = null;
        Journal = null;
        Settings = null;
    }
}