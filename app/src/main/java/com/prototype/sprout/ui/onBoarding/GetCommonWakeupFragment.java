package com.prototype.sprout.ui.onBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.prototype.sprout.R;
import com.prototype.sprout.databinding.FragmentGetCommonWakeupBinding;
import com.prototype.sprout.model.BundleKey;
import com.prototype.sprout.model.InitialTime;

public class GetCommonWakeupFragment extends Fragment {

    //View Binding
    private FragmentGetCommonWakeupBinding binding;
    private TimePicker timePicker;

    public GetCommonWakeupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
            bundle.putInt(new BundleKey().getKEY_WAKEHOUR(), timePicker.getHour());
            bundle.putInt(new BundleKey().getKEY_WAKEMINUTE(), timePicker.getMinute());
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_getCommonWakeup_to_getCommonSleepTime, bundle);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setInitialTime() {
        timePicker.setHour(InitialTime.WAKE_HOUR.getValue());
        timePicker.setMinute(InitialTime.WAKE_MINUTE.getValue());
    }
}