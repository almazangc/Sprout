package com.prototype.sprout.fragment.onBoarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.prototype.sprout.R;
import com.prototype.sprout.activity.startup.Main;
import com.prototype.sprout.database.User.UserViewModel;
import com.prototype.sprout.databinding.FragmentAnalysisBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link analysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class analysisFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentAnalysisBinding binding;

    public analysisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment analysisFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static analysisFragment newInstance(String param1, String param2) {
        analysisFragment fragment = new analysisFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalysisBinding.inflate(inflater, container, false);

        // TODO: CREATE ROOM DATABASE
        String[] items = new String[]{"YES, AN UPGRADE", "YES, GONNA RESELL IT", "NAH NO NEED", "...... Im speechless"};
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(requireContext(), R.layout.list_item, items);
        binding.dropItems.setAdapter(adapterItems);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.btnContinue.setOnClickListener(view -> {
            setOnBoarding();
            startActivity(new Intent(requireActivity(), Main.class));
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(requireContext(), "You shall not amend", Toast.LENGTH_SHORT).show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setOnBoarding(){
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setOnBoarding();
    }

}