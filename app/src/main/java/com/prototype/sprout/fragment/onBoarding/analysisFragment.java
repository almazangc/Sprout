package com.prototype.sprout.fragment.onBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.prototype.sprout.R;
import com.prototype.sprout.database.user.UserViewModel;
import com.prototype.sprout.databinding.FragmentAnalysisBinding;
import com.prototype.sprout.model.BundleKey;

public class analysisFragment extends Fragment {

    private FragmentAnalysisBinding binding;

    public analysisFragment() {
        // Required empty public constructor
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
            Bundle bundle = new Bundle();
            bundle.putBoolean(new BundleKey().getKEY_ANALYSIS(), true);
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_analysis_to_startup, bundle);
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