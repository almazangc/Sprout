package com.prototype.sprout.ui.onBoarding;

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

import com.prototype.sprout.R;
import com.prototype.sprout.database.assessment.Assessment;
import com.prototype.sprout.database.assessment.AssessmentViewModel;
import com.prototype.sprout.database.user.UserViewModel;
import com.prototype.sprout.databinding.FragmentPersonalizationBinding;

import java.util.ArrayList;
import java.util.List;

public class PersonalizationFragment extends Fragment {

    private int uid = 0;
    private FragmentPersonalizationBinding binding;
    private AssessmentViewModel assessmentViewModel;

    public PersonalizationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalizationBinding.inflate(inflater, container, false);

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
        assessmentViewModel = new ViewModelProvider(requireActivity()).get(AssessmentViewModel.class);
        binding.assessmentProgressBar.setMax(assessmentViewModel.getAllAssessmentList().size());
        assessmentViewModel.getAllAssessmentListLiveData().observe(getViewLifecycleOwner(), assessments -> {

            if (uid == 0)setUIText(assessments);
            binding.assessmentProgressBar.setProgress(uid+1);

            binding.btnContinue.setOnClickListener(view1 -> {
                if (!assessments.isEmpty() && assessments.size() > uid+1) {
                    uid++;
                    saveSelection();
                    setUIText(assessments);
                    upCheckedRadioButtons(assessments);
                    binding.assessmentProgressBar.setProgress(uid);
                } else {
                    uid--;
                    saveSelection();
                    Toast.makeText(requireContext(), "End of Questions", Toast.LENGTH_LONG).show();
                    new AlertDialog.Builder(requireContext())
                            .setMessage("Are you done answering all?")
                            .setCancelable(false)
                            //analysis
                            .setPositiveButton("YES", (dialogInterface, i) -> {
                                setUserAssessmentTrue();
                                Navigation.findNavController(view1).navigate(R.id.action_navigate_from_personalization_to_analysis);
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });

            onBackPress(assessments);

        });
    }

    private void onBackPress(List<Assessment> assessments) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (uid > 0) {
                    uid--;
                    binding.assessmentProgressBar.setProgress(uid);
                    setUIText(assessments);
                    upCheckedRadioButtons(assessments);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void setUIText(List<Assessment> assessments) {
        binding.lblQuestion.setText(assessments.get(uid).getQuestion());
        binding.radioASelect.setText(assessments.get(uid).getASelect());
        binding.radioBSelect.setText(assessments.get(uid).getBSelect());
        binding.radioCSelect.setText(assessments.get(uid).getCSelect());
        binding.radioDSelect.setText(assessments.get(uid).getDSelect());
    }

    private void saveSelection(){
        assessmentViewModel.updateSelectedUID(
                uid,
                ((RadioButton)
                        (binding.getRoot().findViewById(binding.radioGroupSelect.getCheckedRadioButtonId())))
                            .getText()
                            .toString());
    }

    private void upCheckedRadioButtons(List<Assessment> assessments){
        ArrayList<RadioButton> radioButtonList = getRadioButtonList();

        ((RadioButton) (binding.getRoot().findViewById(binding.radioGroupSelect.getCheckedRadioButtonId()))).setChecked(false);

        for (RadioButton radioButton: radioButtonList){
            if ((radioButton.getText().toString()).equals(assessments.get(uid).getSelected())){
                radioButton.setChecked(true);
                break;
            }
        }
    }

    private ArrayList<RadioButton> getRadioButtonList(){
        ArrayList<RadioButton> radioButtonList = new ArrayList<>();
        int count = binding.radioGroupSelect.getChildCount();
        for (int i = 0; i < count; i++){
            View view = binding.radioGroupSelect.getChildAt(i);
            if (view instanceof RadioButton) radioButtonList.add((RadioButton) view);
        }
        return radioButtonList;
    }

    // Update User Personalization Complete to true
    private void setUserAssessmentTrue(){
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setAssessment();
    }
}