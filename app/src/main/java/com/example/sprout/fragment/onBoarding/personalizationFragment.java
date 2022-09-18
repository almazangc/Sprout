package com.example.sprout.fragment.onBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.sprout.R;
import com.example.sprout.database.AppDatabase;
import com.example.sprout.database.Assestment.Assessment;
import com.example.sprout.database.Assestment.AssessmentViewModel;
import com.example.sprout.database.User.UserViewModel;
import com.example.sprout.databinding.FragmentPersonalizationBinding;

import java.util.ArrayList;
import java.util.List;

public class personalizationFragment extends Fragment {

    private static int uid = 0;
    private FragmentPersonalizationBinding binding;
    private List<Assessment> currentQuestion = new ArrayList<>();
    private List<Assessment> allAssesstmentList;

    public personalizationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalizationBinding.inflate(inflater, container, false);
        AssessmentViewModel assessmentViewModel = new ViewModelProvider(requireActivity()).get(AssessmentViewModel.class);
        allAssesstmentList = assessmentViewModel.getAllAssesstmentList();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAssesstmentText();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.btnContinue.setOnClickListener(view -> {
            if (!allAssesstmentList.isEmpty() && allAssesstmentList.size() < uid + 1) {
                //Also check if end of index getSize
                uid++;
                setAssesstmentText();
            } else {
                uid--;
                Toast.makeText(requireContext(), "End of Questions", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(requireContext())
                        .setMessage("Are you done answering all?")
                        .setCancelable(false)
                        //analysis
                        .setPositiveButton("YES", (dialogInterface, i) -> {
                            setUserAssesstmentTrue();
                            Navigation.findNavController(view).navigate(R.id.action_navigate_from_personalization_to_analysis);
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                uid--;
                setAssesstmentText();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void setAssesstmentText() {
        //Over Index
//        if (uid > allAssesstmentList.size()) return;
        binding.lblQuestion.setText(allAssesstmentList.get(uid).getQuestion());
        binding.radioASelect.setText(allAssesstmentList.get(uid).getASelect());
        binding.radioBselect.setText(allAssesstmentList.get(uid).getBSelect());
        binding.radioCselect.setText(allAssesstmentList.get(uid).getCSelect());
        binding.radioDselect.setText(allAssesstmentList.get(uid).getDSelect());
    }

    private String addListenerOnButton() {
        int selectedId = binding.radioGroupSelect.getCheckedRadioButtonId();
        RadioButton radioButton = (binding.getRoot().findViewById(selectedId));
        return radioButton.getText().toString();
    }

    // Update User: AsssestmentToTrue
    private void setUserAssesstmentTrue(){
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setAssesstment();
    }
}