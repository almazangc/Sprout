package com.habitdev.sprout.ui.menu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.FragmentBottomNavigationBinding;
import com.habitdev.sprout.ui.menu.analytic.AnalyticFragment;
import com.habitdev.sprout.ui.menu.home.HomeParentItemFragment;
import com.habitdev.sprout.ui.menu.journal.JournalFragment;
import com.habitdev.sprout.ui.menu.setting.SettingFragment;
import com.habitdev.sprout.ui.menu.subroutine.SubroutineFragment;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class BottomNavigationFragment extends Fragment {

    private FragmentBottomNavigationBinding binding;
    private FragmentManager fragmentManager;
    private int last_menu_selected, last_selected_index;
    private SwipeListener swipeListener;

    public BottomNavigationFragment() {
        //Required Empty Constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBottomNavigationBinding.inflate(inflater, container, false);
        swipeListener = new SwipeListener();

        if (savedInstanceState == null) {
            binding.bottomBar.selectTabById(R.id.tab_home, true);
            fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction().replace(binding.mainNavFragmentContainer.getId(), new HomeParentItemFragment())
                    .commit();
            binding.bottomBar.selectTabAt(0, true);
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

        Fragment Home = new HomeParentItemFragment();
        Fragment Subroutine = new SubroutineFragment();
        Fragment Analytics = new AnalyticFragment();
        Fragment Journal = new JournalFragment();
        Fragment Settings = new SettingFragment();

        Fragment fragment;

        if (id == R.id.tab_subroutine) {
            fragment = Subroutine;
        } else if (id == R.id.tab_analytic) {
            fragment = Analytics;
        } else if (id == R.id.tab_journal) {
            fragment = Journal;
        } else if (id == R.id.tab_settings) {
            fragment = Settings;
        } else {
            fragment = Home;
        }

        fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(binding.mainNavFragmentContainer.getId(), fragment)
                .commit();
        binding.bottomBar.selectTabAt(tabID, true);
    }

    /**
     * Listener on Touch Swipe Action Handle Navigation Through Swiping
     */
    private class SwipeListener implements View.OnTouchListener {

        private final int SWIPE_THRESHOLD = 100;
        private final int SWIPE_VELOCITY_THRESHOLD = 100;
        private final GestureDetector gestureDetector;

        public SwipeListener() {
            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
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
                                    onSwipeRight();
                                } else {
                                    onSwipeLeft();
                                }
                            }
                            result = true;
                        } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                onSwipeBottom();
                            } else {
                                onSwipeTop();
                            }
                        }
                        result = true;

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    return result;
                }
            };

            gestureDetector = new GestureDetector(listener);
            gestureDetector.setContextClickListener(listener);

            binding.mainNavFragmentContainer.setOnTouchListener(this);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }

        void onSwipeRight() {
            switch (last_selected_index) {
                case 0:
                    updateFragment( 4);
                    break;
                case 1:
                    updateFragment( 0);
                    break;
                case 2:
                    updateFragment( 1);
                    break;
                case 3:
                    updateFragment( 2);
                    break;
                case 4:
                    updateFragment( 3);
                    break;
            }
        }

        void onSwipeLeft() {
            switch (last_selected_index) {
                case 0:
                    updateFragment( 1);
                    break;
                case 1:
                    updateFragment( 2);
                    break;
                case 2:
                    updateFragment( 3);
                    break;
                case 3:
                    updateFragment( 4);
                    break;
                case 4:
                    updateFragment( 0);
                    break;
            }
        }

        void onSwipeTop() {
            Toast.makeText(requireContext(), "Top Swipe", Toast.LENGTH_SHORT).show();
        }

        void onSwipeBottom() {
            Toast.makeText(requireContext(), "Bottom Swipe", Toast.LENGTH_SHORT).show();
        }

        void updateFragment(int newTab){
            binding.bottomBar.selectTabAt(newTab, true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentManager = null;
        binding = null;
    }
}