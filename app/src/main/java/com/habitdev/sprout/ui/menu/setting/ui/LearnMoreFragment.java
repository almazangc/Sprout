package com.habitdev.sprout.ui.menu.setting.ui;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.habitdev.sprout.R;
import com.habitdev.sprout.activity.startup.Main;
import com.habitdev.sprout.databinding.FragmentLearnMoreBinding;
import com.habitdev.sprout.ui.menu.setting.adapter.LearnMoreSlideAdapter;

public class LearnMoreFragment extends Fragment {

    private FragmentLearnMoreBinding binding;

    public interface OnReturnSetting {
        void returnFromLearnMoreToSetting();
    }

    private OnReturnSetting mOnReturnSetting;

    public void setmOnReturnSetting(OnReturnSetting mOnReturnSetting) {
        this.mOnReturnSetting = mOnReturnSetting;
    }

    private TextView[] sliderDots;
    private int currentSlideView;

    public LearnMoreFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLearnMoreBinding.inflate(inflater, container, false);
        setBackground();
        setSlideAdapter();
        onBackPress();
        return binding.getRoot();
    }

    private void setSlideAdapter() {
        LearnMoreSlideAdapter learnMoreSlideAdapter = new LearnMoreSlideAdapter(requireActivity());
        binding.learnMoreViewPager.setAdapter(learnMoreSlideAdapter);
        setIndicatorDots(0);
        binding.learnMoreViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicatorDots(position);
                currentSlideView = position;

                if (position == 0) {
                    binding.settingLearnMoreNextBtn.setEnabled(true);
                    binding.settingLearnMoreBackBtn.setEnabled(false);
                    binding.settingLearnMoreBackBtn.setVisibility(View.INVISIBLE);

                    binding.settingLearnMoreNextBtn.setText("Next");
                    binding.settingLearnMoreBackBtn.setText("");

                } else if (position == sliderDots.length - 1) {
                    Log.d("tag", "onPageSelected: " + sliderDots.length);
                    binding.settingLearnMoreNextBtn.setEnabled(false);
                    binding.settingLearnMoreBackBtn.setEnabled(true);
                    binding.settingLearnMoreBackBtn.setVisibility(View.VISIBLE);
                    binding.settingLearnMoreNextBtn.setVisibility(View.INVISIBLE);

                    binding.settingLearnMoreNextBtn.setText("");
                    binding.settingLearnMoreBackBtn.setText("Back");
                } else {
                    binding.settingLearnMoreNextBtn.setEnabled(true);
                    binding.settingLearnMoreBackBtn.setEnabled(true);
                    binding.settingLearnMoreBackBtn.setVisibility(View.VISIBLE);
                    binding.settingLearnMoreNextBtn.setVisibility(View.VISIBLE);
                    binding.settingLearnMoreNextBtn.setText("Next");
                    binding.settingLearnMoreBackBtn.setText("Back");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.settingLearnMoreNextBtn.setOnClickListener(view -> binding.learnMoreViewPager.setCurrentItem(currentSlideView + 1));

        binding.settingLearnMoreBackBtn.setOnClickListener(view -> binding.learnMoreViewPager.setCurrentItem(currentSlideView - 1));
    }

    private void setBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // The device's SDK version is greater than or equal to 28, so continue executing the app
            final String SharedPreferences_KEY = "SP_DB";
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SharedPreferences_KEY, Main.MODE_PRIVATE);

            final String SHARED_PREF_KEY = "THEME_SHARED.PREF";
            int theme = sharedPreferences.getInt(SHARED_PREF_KEY, -1);
            if (theme == 1) {
                binding.learnMoreFrameLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.background_image_light));
            } else if (theme == 2) {
                binding.learnMoreFrameLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.background_image_night));
            } else {
                binding.learnMoreFrameLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.background_image_light));
            }
        }
    }

    private void setIndicatorDots(int pos) {
        sliderDots = new TextView[5];
        binding.settingLearnMoreDotsLayout.removeAllViews(); // clear views

        for (int position = 0; position < sliderDots.length; position++) {
            sliderDots[position] = new TextView(requireActivity());
            sliderDots[position].setText(Html.fromHtml("&#8226;"));
            sliderDots[position].setTextSize(30);
            sliderDots[position].setTextColor(ContextCompat.getColor(requireContext(), R.color.CLOUDS));
            binding.settingLearnMoreDotsLayout.addView(sliderDots[position]);
        }

        if (sliderDots.length > 0) {
            sliderDots[pos].setTextColor(ContextCompat.getColor(requireContext(), R.color.NIGHT));
        }
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnReturnSetting != null) mOnReturnSetting.returnFromLearnMoreToSetting();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}