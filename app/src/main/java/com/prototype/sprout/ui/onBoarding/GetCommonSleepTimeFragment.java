package com.prototype.sprout.ui.onBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.prototype.sprout.R;
import com.prototype.sprout.databinding.FragmentGetCommonSleepTimeBinding;
import com.prototype.sprout.model.BundleKey;
import com.prototype.sprout.model.InitialTime;

public class GetCommonSleepTimeFragment extends Fragment {

    //View Binding
    private FragmentGetCommonSleepTimeBinding binding;
    private TimePicker timePicker;

    public GetCommonSleepTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetCommonSleepTimeBinding.inflate(inflater, container, false);
        timePicker = binding.SleepTimePicker.getRoot();
        setInitialTime();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.btnContinue.setOnClickListener(view -> {
            Bundle bundle = getArguments();
            bundle.putInt(new BundleKey().getKEY_SLEEP_HOUR(), timePicker.getHour());
            bundle.putInt(new BundleKey().getKEY_SLEEP_MINUTE(), timePicker.getMinute());
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_getCommonSleepTime_to_introduction, bundle);
        });
    }

    private void setInitialTime() {
        timePicker.setHour(InitialTime.SLEEP_HOUR.getValue());
        timePicker.setMinute(InitialTime.SLEEP_MINUTE.getValue());
    }
}