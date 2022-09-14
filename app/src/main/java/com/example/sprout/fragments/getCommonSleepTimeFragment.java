package com.example.sprout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sprout.R;
import com.example.sprout.databinding.FragmentGetCommonSleepTimeBinding;
import com.example.sprout.model.BundleKey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link getCommonSleepTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class getCommonSleepTimeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //View Binding
    private FragmentGetCommonSleepTimeBinding binding;

    public getCommonSleepTimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment getCommonSleepTimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static getCommonSleepTimeFragment newInstance(String param1, String param2) {
        getCommonSleepTimeFragment fragment = new getCommonSleepTimeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGetCommonSleepTimeBinding.inflate(inflater, container, false);

        setInitialTime();

        binding.btnContinue.setOnClickListener(view -> {
            Bundle bundle = getArguments();
            bundle.putInt(new BundleKey().getKEY_SLEEPHOUR(), binding.SleepTimePicker.getHour());
            bundle.putInt(new BundleKey().getKEY_SLEEPMINUTE(), binding.SleepTimePicker.getMinute());
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_getCommonSleepTime_to_introduction, bundle);
        });

        return binding.getRoot();
    }

    private void setInitialTime() {
        binding.SleepTimePicker.setHour(20);
        binding.SleepTimePicker.setMinute(15);
    }
}