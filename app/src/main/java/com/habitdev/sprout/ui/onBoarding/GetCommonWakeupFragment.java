package com.habitdev.sprout.ui.onBoarding;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.FragmentGetCommonWakeupBinding;
import com.habitdev.sprout.enums.BundleKeys;
import com.habitdev.sprout.enums.InitialTime;
import com.habitdev.sprout.enums.OnBoardingConfigurationKeys;

public class GetCommonWakeupFragment extends Fragment {

    private FragmentGetCommonWakeupBinding binding;
    private TimePicker timePicker;
    private int hour, minute;

    public GetCommonWakeupFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetCommonWakeupBinding.inflate(inflater, container, false);

        timePicker = binding.WakeTimePicker.getRoot();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(OnBoardingConfigurationKeys.WAKEUP_SHAREDPREF.getValue(), Context.MODE_PRIVATE);
        if (sharedPreferences.contains(OnBoardingConfigurationKeys.HOUR.getValue()) || sharedPreferences.contains(OnBoardingConfigurationKeys.MINUTE.getValue())) {
            updateFromSharedPref();
        } else {
            if (savedInstanceState == null) {
                setInitialTime();
            } else {
                restoreTimePickerView(savedInstanceState);
            }
        }

        timePicker.setOnTimeChangedListener((timePicker, hour, minutes) -> {
            setHour(hour);
            setMinute(minutes);
        });

        setContinueOnClickListener();
        return binding.getRoot();
    }

    private void setContinueOnClickListener() {
        binding.btnContinue.setOnClickListener(view -> {
            Bundle bundle = getArguments();
            assert bundle != null;
            bundle.putInt(BundleKeys.WAKE_HOUR.getKEY(), timePicker.getHour());
            bundle.putInt(BundleKeys.WAKE_MINUTE.getKEY(), timePicker.getMinute());
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_getCommonWakeup_to_getCommonSleepTime, bundle);
        });
    }

    private void setInitialTime() {
        timePicker.setHour(InitialTime.WAKE_HOUR.getValue());
        timePicker.setMinute(InitialTime.WAKE_MINUTE.getValue());
    }

    private void restoreTimePickerView(@NonNull Bundle savedInstanceState) {
        setHour(savedInstanceState.getInt(BundleKeys.WAKE_HOUR.getKEY()));
        setMinute(savedInstanceState.getInt(BundleKeys.WAKE_MINUTE.getKEY()));
        timePicker.setHour(hour);
        timePicker.setMinute(minute);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (binding != null) {
            outState.putInt(BundleKeys.WAKE_HOUR.getKEY(), hour);
            outState.putInt(BundleKeys.WAKE_MINUTE.getKEY(), minute);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(OnBoardingConfigurationKeys.WAKEUP_SHAREDPREF.getValue(), Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putInt(OnBoardingConfigurationKeys.HOUR.getValue(), hour)
                .putInt(OnBoardingConfigurationKeys.MINUTE.getValue(), minute)
                .apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFromSharedPref();
    }

    private void updateFromSharedPref() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(OnBoardingConfigurationKeys.WAKEUP_SHAREDPREF.getValue(), Context.MODE_PRIVATE);
        setHour(sharedPreferences.getInt(OnBoardingConfigurationKeys.HOUR.getValue(), InitialTime.WAKE_HOUR.getValue()));
        setMinute(sharedPreferences.getInt(OnBoardingConfigurationKeys.MINUTE.getValue(), InitialTime.WAKE_MINUTE.getValue()));
        timePicker.setHour(hour);
        timePicker.setMinute(minute);
    }

    private void setHour(int hour) {
        this.hour = hour;
    }

    private void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timePicker = null;
        binding = null;
    }

}