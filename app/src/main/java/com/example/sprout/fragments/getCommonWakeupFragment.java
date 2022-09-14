package com.example.sprout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sprout.R;
import com.example.sprout.model.BundleKey;
import com.example.sprout.databinding.FragmentGetCommonWakeupBinding;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetCommonWakeupBinding.inflate(inflater, container, false);
        setInitialTime();

        binding.btnContinue.setOnClickListener(view -> {
            Bundle bundle = getArguments();
            bundle.putInt(new BundleKey().getKEY_WAKEHOUR(), binding.WakeTimePicker.getHour());
            bundle.putInt(new BundleKey().getKEY_WAKEMINUTE(), binding.WakeTimePicker.getMinute());
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_getCommonWakeup_to_getCommonSleepTime, bundle);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setInitialTime() {
        binding.WakeTimePicker.setHour(20);
        binding.WakeTimePicker.setMinute(15);
    }
}