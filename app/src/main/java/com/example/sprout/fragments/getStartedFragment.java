package com.example.sprout.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sprout.R;
import com.example.sprout.database.Assestment.PopulateAssestmentDatabase;
import com.example.sprout.databinding.FragmentGetStartedBinding;
import com.example.sprout.model.BundleKey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link getStartedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class getStartedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // View Binding
    private FragmentGetStartedBinding binding;

    public getStartedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment getStartedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static getStartedFragment newInstance(String param1, String param2) {
        getStartedFragment fragment = new getStartedFragment();
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
        binding = FragmentGetStartedBinding.inflate(inflater, container, false);

        binding.lblWelcome.setText(String.format("Warm Welcome\n %s", getArguments().getString(new BundleKey().getKEY_NICKNAME())));

        binding.btnContinue.setOnClickListener(view -> {
            PopulateAssestmentDatabase populateAssestmentDatabase = new PopulateAssestmentDatabase(requireContext());
            populateAssestmentDatabase.populateAssestmentDatabase();
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_getStarted_to_personalization);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // TODO: DISABLE ONBACKPRESS FOR FRAGMENTS

}