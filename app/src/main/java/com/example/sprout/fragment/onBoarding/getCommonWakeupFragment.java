package com.example.sprout.fragment.onBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.sprout.R;
import com.example.sprout.databinding.FragmentGetCommonWakeupBinding;
import com.example.sprout.model.BundleKey;
import com.example.sprout.model.InitialTime;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link getCommonWakeupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class getCommonWakeupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //View Binding
    private FragmentGetCommonWakeupBinding binding;

    public getCommonWakeupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment getCommonWakeupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static getCommonWakeupFragment newInstance(String param1, String param2) {
        getCommonWakeupFragment fragment = new getCommonWakeupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetCommonWakeupBinding.inflate(inflater, container, false);
        setInitialTime();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.btnContinue.setOnClickListener(view -> {
            Bundle bundle = getArguments();
            bundle.putInt(new BundleKey().getKEY_WAKEHOUR(), binding.WakeTimePicker.getHour());
            bundle.putInt(new BundleKey().getKEY_WAKEMINUTE(), binding.WakeTimePicker.getMinute());
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_getCommonWakeup_to_getCommonSleepTime, bundle);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setInitialTime() {
        binding.WakeTimePicker.setHour(new InitialTime().getWakeHour());
        binding.WakeTimePicker.setMinute(new InitialTime().getWakeMinute());
    }
}