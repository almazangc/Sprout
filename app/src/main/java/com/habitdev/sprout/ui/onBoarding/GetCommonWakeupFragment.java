package com.habitdev.sprout.ui.onBoarding;

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

public class GetCommonWakeupFragment extends Fragment {

    //View Binding
    private FragmentGetCommonWakeupBinding binding;
    private TimePicker timePicker;

    public GetCommonWakeupFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetCommonWakeupBinding.inflate(inflater, container, false);
        timePicker = binding.WakeTimePicker.getRoot();
        setInitialTime();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timePicker = null;
        binding = null;
    }

}