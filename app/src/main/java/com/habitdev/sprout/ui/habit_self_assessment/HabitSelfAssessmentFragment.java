package com.habitdev.sprout.ui.habit_self_assessment;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.assessment.AssessmentViewModel;
import com.habitdev.sprout.database.assessment.model.Answer;
import com.habitdev.sprout.database.assessment.model.AssessmentRecord;
import com.habitdev.sprout.database.assessment.model.Choices;
import com.habitdev.sprout.database.assessment.model.Question;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentPersonalizationBinding;
import com.habitdev.sprout.enums.OnBoardingConfigurationKeys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HabitSelfAssessmentFragment extends Fragment {

    private FragmentPersonalizationBinding binding;
    private AssessmentViewModel assessmentViewModel;
    private static List<Question> questionsList;
    private static List<Choices> choicesList;
    private static List<Answer> answersList;
    private static AssessmentRecord assessmentRecord;
    private static int position;
    private static boolean isOnRekateAssessment;
    private static Bundle savedInstanceState;
    private static AnalysisFragment analysisFragment;

    public HabitSelfAssessmentFragment() {
        position = 0;
        questionsList = new ArrayList<>();
        choicesList = new ArrayList<>();
        answersList = new ArrayList<>();
        isOnRekateAssessment = false;
    }

    public HabitSelfAssessmentFragment(boolean isOnRekateAssessment) {
        position = 0;
        questionsList = new ArrayList<>();
        choicesList = new ArrayList<>();
        answersList = new ArrayList<>();
        HabitSelfAssessmentFragment.isOnRekateAssessment = isOnRekateAssessment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalizationBinding.inflate(inflater, container, false);

        if (savedInstanceState != null)
            HabitSelfAssessmentFragment.savedInstanceState = savedInstanceState;

        assessmentViewModel = new ViewModelProvider(requireActivity()).get(AssessmentViewModel.class);
        questionsList = assessmentViewModel.getShuffledQuestions();

        if (assessmentViewModel.getUncompletedAssessmentRecordCount() == 1) {
            assessmentRecord = assessmentViewModel.getAssessmentRecordByUID(assessmentViewModel.getUncompletedAssessmentRecordUID());
        } else if (assessmentViewModel.getUncompletedAssessmentRecordCount() == 0) {
            long uid = assessmentViewModel.insertAssessmentRecord(new AssessmentRecord(false));
            assessmentRecord = assessmentViewModel.getAssessmentRecordByUID(uid);
        }
        answersList = assessmentViewModel.getAllAnswerList(assessmentRecord.getPk_assessment_record_uid());

        onBackPress();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAssessment();// once on start
        setContinueListener();// continue button listener
        setProgressBarMaxSize(); // set max progress bar
        observeAnswerList();
    }

    /**
     * Restore Saved Instance State Due to Configurigation Changes
     */
    @Override
    public void onStart() {
        super.onStart();
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(OnBoardingConfigurationKeys.POSITION.getKey());
            setAssessment();
        }
    }


    private void setContinueListener() {
        binding.btnContinue.setOnClickListener(v -> {
            if (isAllRadioButtonUnchecked()) {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), Html.fromHtml("Please set your answers"), Snackbar.LENGTH_SHORT)
                        .setTextColor(ContextCompat.getColor(requireContext(), R.color.ClOUDS_))
                        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.POMEGRANATE))
                        .setDuration(2000); //to seconds duration
                // Get the Snackbar's default text view
                TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setTextSize(16);
                snackbar.show();
            } else {
                saveSelection();
                setAssessment();
            }
        });
    }

    /**
     * Create Instance of Radiobuttons on RadioGroup and set attributes
     */
    private void setAssessment() {
        if (position < questionsList.size()) {

            binding.choicesRadioGroup.removeAllViews();
            binding.lblQuestion.setText(questionsList.get(position).getQuestion());

            getCurrentQuestionChoices(questionsList.get(position).getPk_question_uid());

            //Increasing Touch Target Size from 42dp to 48dp
            for (Choices choice : choicesList) {
                RadioButton radioButton = new RadioButton(requireContext());
                radioButton.setText(choice.getChoices());
                radioButton.setButtonDrawable(null);
                radioButton.setChecked(false);
                radioButton.setEllipsize(TextUtils.TruncateAt.END);
                radioButton.setPadding(23, 23, 23, 23);
                radioButton.setMaxLines(1);
                radioButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                radioButton.setTextColor(
                        new ColorStateList(
                                new int[][]{
                                        new int[]{android.R.attr.state_pressed},
                                        new int[]{android.R.attr.state_checked},
                                        new int[]{}
                                },
                                new int[]{
                                        requireContext().getColor(R.color.NIGHT),
                                        requireContext().getColor(R.color.CLOUDS),
                                        requireContext().getColor(R.color.A80_NIGHT)
                                }
                        ));
                radioButton.setTextSize(20);
                radioButton.setBackgroundResource(R.drawable.selector_background_radio_button);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 10, 0, 10);
                radioButton.setLayoutParams(params);
                binding.choicesRadioGroup.addView(radioButton);
            }

            upCheckedRadioButtons();
            updateProgressBar();
        } else {
            // End of Questions
            new AlertDialog.Builder(requireContext())
                    .setMessage("Are you done answering all?")
                    .setCancelable(false)
                    .setPositiveButton("YES", (dialogInterface, i) -> {
                        if (isOnRekateAssessment) {
                            analysisFragment = null;
                            analysisFragment = new AnalysisFragment(true, assessmentRecord);
                            getChildFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(HabitSelfAssessmentFragment.this.getTag())
                                    .add(binding.personalizationRelativeLayout.getId(), analysisFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();
                            binding.personalizationContainer.setVisibility(View.GONE);
                            updateAssessmentRecord();
                            assessmentViewModel.getGetAllAnswerListLiveData(assessmentRecord.getPk_assessment_record_uid()).removeObservers(getViewLifecycleOwner());
                            assessmentViewModel = null;
                        } else {
                            updateAssessmentRecord();
                            setUserAssessmentTrue();
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigate_from_personalization_to_analysis);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    private void updateAssessmentRecord() {
        assessmentRecord.setCompleted(true);
        assessmentRecord.setDate(
                new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm a", Locale.getDefault()).format(new Date())
        );
        assessmentViewModel.updateAssessmentRecord(assessmentRecord);
    }

    /**
     * Get Choices of the current question
     */
    private void getCurrentQuestionChoices(long uid) {
        choicesList = assessmentViewModel.getAllChoicesByUID(uid);
        position++;
    }

    /**
     * Sets the progressbar size
     */
    private void setProgressBarMaxSize() {
        binding.assessmentProgressBar.setMax(questionsList.size() - 1);
        updateProgressBar();
    }

    /**
     * Updates Progress Bar
     */
    private void updateProgressBar() {
        binding.assessmentProgressBar.setProgress(position - 1, true);
    }

    /**
     * Handles onBackPress Key
     */
    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (position > 1) {
                    position = position - 2;
                    setAssessment();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    /**
     * Validate if user selected an answer
     *
     * @return boolean
     */
    private boolean isAllRadioButtonUnchecked() {
        ArrayList<RadioButton> radioButtonList = getRadioButtonList();
        for (RadioButton radioButton : radioButtonList) {
            if (radioButton.isChecked()) return false;
        }
        return true;
    }

    /**
     * Gets all list of radio buttons
     *
     * @return matched instance of ArrayList of Radio Buttons
     */
    private ArrayList<RadioButton> getRadioButtonList() {
        ArrayList<RadioButton> radioButtonList = new ArrayList<>();
        int count = binding.choicesRadioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = binding.choicesRadioGroup.getChildAt(i);
            if (view instanceof RadioButton) radioButtonList.add((RadioButton) view);
        }
        return radioButtonList;
    }

    /**
     *
     */
    private void saveSelection() {
        long fk_question_uid = questionsList.get(position - 1).getPk_question_uid();
        String selected_answer = ((RadioButton)
                (binding.getRoot().findViewById(binding.choicesRadioGroup.getCheckedRadioButtonId())))
                .getText()
                .toString();
        long fk_user_uid = 1;

        if (answersList.isEmpty()) {
            assessmentViewModel.insertAnswer(new Answer(fk_question_uid, assessmentRecord.getPk_assessment_record_uid(), selected_answer, fk_user_uid));
        } else {
            if (assessmentViewModel.doesAnswerExist(assessmentRecord.getPk_assessment_record_uid(), fk_question_uid) > 1) {
                //
            } else if (assessmentViewModel.doesAnswerExist(assessmentRecord.getPk_assessment_record_uid(), fk_question_uid) == 1) {
                Answer answer = assessmentViewModel.getAnswerByFkQuestionUID(assessmentRecord.getPk_assessment_record_uid(), fk_question_uid);
                assessmentViewModel.updateAnswer(new Answer(answer.getPk_answer_uid(), fk_question_uid, selected_answer, fk_user_uid));
            } else {
                assessmentViewModel.insertAnswer(new Answer(fk_question_uid, assessmentRecord.getPk_assessment_record_uid(), selected_answer, fk_user_uid));
            }
        }
    }

    private void observeAnswerList() {
        assessmentViewModel.getGetAllAnswerListLiveData(assessmentRecord.getPk_assessment_record_uid()).observe(getViewLifecycleOwner(), answers -> answersList = new ArrayList<>(answers));
    }

    /**
     * Iterates though the list of radio button and checks matching selected user selection and updateAnswer UI of selected radio buttons
     */
    private void upCheckedRadioButtons() {
        ArrayList<RadioButton> radioButtonList = getRadioButtonList();
        for (RadioButton radioButton : radioButtonList) {
            Question current_question = questionsList.get(position - 1);
            if ((radioButton.getText().toString()).equals(
                    assessmentViewModel.getAnswerByFkQuestionUID(assessmentRecord.getPk_assessment_record_uid(), current_question.getPk_question_uid()) != null ? assessmentViewModel.getAnswerByFkQuestionUID(assessmentRecord.getPk_assessment_record_uid(), current_question.getPk_question_uid()).getSelected_answer() : "")
            ) {
                radioButton.setChecked(true);
                break;
            }
        }
    }

    /**
     * Updates Room Database: Assessment Entity of User Table to True
     */
    private void setUserAssessmentTrue() {
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setAssessment();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (binding != null) {
            saveSelection();
        }
        outState.putInt(OnBoardingConfigurationKeys.POSITION.getKey(), position - 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}