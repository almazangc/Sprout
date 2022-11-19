package com.habitdev.sprout.ui.onBoarding.assessment;

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

import com.habitdev.sprout.R;
import com.habitdev.sprout.database.assessment.Assessment;
import com.habitdev.sprout.database.assessment.AssessmentViewModel;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentPersonalizationBinding;

import java.util.ArrayList;
import java.util.List;

public class PersonalizationFragment extends Fragment {

    //Unique ID for navigation of Assessment Table Rows
    private int uid = 0;
    // Data Biding
    private FragmentPersonalizationBinding binding;
    // Assessment View Model MVVM Architecture
    private AssessmentViewModel assessmentViewModel;

    /**
     * Scale: Must be able generate radiobutton instance in a radiogroup depending on number of selection
     * Room Database: Entity: Choices Type: ArrayList String use TypeConverter to Store
     */
    public PersonalizationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalizationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assessmentViewModel = new ViewModelProvider(requireActivity()).get(AssessmentViewModel.class);
        setProgressBarMaxSize();
        assessmentViewModel.getAllAssessmentListLiveData().observe(getViewLifecycleOwner(), assessments -> {

            //Initial UI Update and Initialize ProgressBar Value to 1
            if (uid == 0) setUIText(assessments);
            binding.assessmentProgressBar.setProgress(uid+1);

            binding.btnContinue.setOnClickListener(view1 -> {

                //PROMPT: NO SELECTED ANSWER
                if (isAllRadioButtonUnchecked()) {
                    Toast.makeText(requireContext(), "Please Answer the Question before proceeding", Toast.LENGTH_LONG).show();
                    return;
                }

                //ROOM UPDATE: ASSESSMENT USER SELECTION
                if (assessments.size() > uid) {
                    uid++;
                    saveSelection();
                }

                //UI UPDATER
                if ((uid-1) < assessments.size()-1){
                    setUIText(assessments);
                    upCheckedRadioButtons(assessments);
                    binding.assessmentProgressBar.setProgress(uid);
                }

                //PROMPT: CONFIRMATION OF ALL ANSWER
                if (uid == assessments.size()) {
                    new AlertDialog.Builder(requireContext())
                            .setMessage("Are you done answering all?")
                            .setCancelable(false)
                            .setPositiveButton("YES", (dialogInterface, i) -> {
                                setUserAssessmentTrue();
                                Navigation.findNavController(view1).navigate(R.id.action_navigate_from_personalization_to_analysis);
                            })
                            .setNegativeButton("No", null)
                            .show();
                    uid--;
                }
            });

            onBackPress(assessments);

        });
    }

    /**
     * Sets the progressbar size
     */
    private void setProgressBarMaxSize() {
        binding.assessmentProgressBar.setMax(assessmentViewModel.getAllAssessmentList().size());
    }

    /**
     * Handles onBackPress Key
     * @param assessments LiveData List <Assessment>
     */
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

    /**
     * Updates Layout Components Text Value
     * @param assessments List of Assessment
     */
    private void setUIText(List<Assessment> assessments) {
        binding.lblQuestion.setText(assessments.get(uid).getQuestion());
        binding.radioASelect.setText(assessments.get(uid).getASelect());
        binding.radioBSelect.setText(assessments.get(uid).getBSelect());
        binding.radioCSelect.setText(assessments.get(uid).getCSelect());
        binding.radioDSelect.setText(assessments.get(uid).getDSelect());
    }


    /**
     * Validate if user selected an answer
     * @return boolean
     */
    private boolean isAllRadioButtonUnchecked(){
        ArrayList<RadioButton> radioButtonList = getRadioButtonList();
        for (RadioButton radioButton: radioButtonList){
            if (radioButton.isChecked()) return false;
        }
        return true;
    }

    /**
     * Updates selected entity in Assessment Table with updateSelectedUID query
     */
    private void saveSelection(){
        assessmentViewModel.updateSelectedUID(
                uid,
                ((RadioButton)
                        (binding.getRoot().findViewById(binding.radioGroupSelect.getCheckedRadioButtonId())))
                            .getText()
                            .toString());
    }

    /**
     * Iterates though the list of radio button and checks matching selected user selection and update UI of selected radio buttons
     * @param assessments List of Assessment
     */
    private void upCheckedRadioButtons(List<Assessment> assessments){

        ArrayList<RadioButton> radioButtonList = getRadioButtonList();

       uncheckAllRadioButtons();

        for (RadioButton radioButton: radioButtonList){
            if ((radioButton.getText().toString()).equals(assessments.get(uid).getSelected())){
                radioButton.setChecked(true);
                break;
            }
        }
    }


    /**
     * Unchecks all radiobutton in radiogroup
     */
    private void uncheckAllRadioButtons(){
        ((RadioButton) (binding.getRoot().findViewById(binding.radioGroupSelect.getCheckedRadioButtonId()))).setChecked(false);
    }

    /**
     * Gets all list of radio buttons
     * @return matched instance of ArrayList of Radio Buttons
     */
    private ArrayList<RadioButton> getRadioButtonList(){
        ArrayList<RadioButton> radioButtonList = new ArrayList<>();
        int count = binding.radioGroupSelect.getChildCount();
        for (int i = 0; i < count; i++){
            View view = binding.radioGroupSelect.getChildAt(i);
            if (view instanceof RadioButton) radioButtonList.add((RadioButton) view);
        }
        return radioButtonList;
    }

    /**
     * Updates Room Database: Assessment Entity of User Table to True
     */
    private void setUserAssessmentTrue(){
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setAssessment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}