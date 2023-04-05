package com.habitdev.sprout.ui.habit_assessment;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.habitdev.sprout.database.assessment.model.Choices;
import com.habitdev.sprout.database.assessment.model.Question;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.databinding.FragmentPersonalizationBinding;
import com.habitdev.sprout.enums.OnBoardingConfigurationKeys;

import java.util.ArrayList;
import java.util.List;

public class PersonalizationFragment extends Fragment {

    private FragmentPersonalizationBinding binding;
    private AssessmentViewModel assessmentViewModel;
    private static List<Question> questionsList;
    private static List<Choices> choicesList;
    private static List<Answer> answersList;
    private static int position;
    private static boolean isOnRekateAssessment;
    private static Bundle savedInstanceState;
    private static AnalysisFragment analysisFragment;

    public PersonalizationFragment() {
        position = 0;
        questionsList = new ArrayList<>();
        choicesList = new ArrayList<>();
        answersList = new ArrayList<>();
        isOnRekateAssessment = false;
    }

    public PersonalizationFragment(boolean isOnRekateAssessment) {
        position = 0;
        questionsList = new ArrayList<>();
        choicesList = new ArrayList<>();
        answersList = new ArrayList<>();
        PersonalizationFragment.isOnRekateAssessment = isOnRekateAssessment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalizationBinding.inflate(inflater, container, false);

        if (savedInstanceState != null)
            PersonalizationFragment.savedInstanceState = savedInstanceState;

        assessmentViewModel = new ViewModelProvider(requireActivity()).get(AssessmentViewModel.class);

        questionsList = assessmentViewModel.getShuffledQuestions();
        answersList = assessmentViewModel.getAllAnswerList();

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
                        .setTextColor(getResources().getColor(R.color.ClOUDS_))
                        .setBackgroundTint(getResources().getColor(R.color.POMEGRANATE))
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

            for (Choices choice : choicesList) {
                RadioButton radioButton = new RadioButton(requireContext());
                radioButton.setText(choice.getChoices());
                radioButton.setButtonDrawable(null);
                radioButton.setChecked(false);
                radioButton.setEllipsize(TextUtils.TruncateAt.END);
                radioButton.setPadding(20, 20, 20, 20);
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
                params.setMargins(0, 15, 0, 15);
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
                            analysisFragment = new AnalysisFragment(true);
                            getChildFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(PersonalizationFragment.this.getTag())
                                    .add(binding.personalizationRelativeLayout.getId(), analysisFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();
                            binding.personalizationContainer.setVisibility(View.GONE);
                            assessmentViewModel.getGetAllAnswerListLiveData().removeObservers(getViewLifecycleOwner());
                            assessmentViewModel = null;
                        } else {
                            setUserAssessmentTrue();
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigate_from_personalization_to_analysis);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
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
                } else {
//                    requireActivity().moveTaskToBack(true);
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
            assessmentViewModel.insertAnswer(new Answer(fk_question_uid, selected_answer, fk_user_uid));
        } else {
            //check if question answer exist
            if (assessmentViewModel.doesAnswerExist(fk_question_uid) > 1) {
//                Log.d("tag", "onChanged: duplicate found");
                //In case of error for duplicate
            } else if (assessmentViewModel.doesAnswerExist(fk_question_uid) == 1) {
                // updateAnswer
                Answer answer = assessmentViewModel.getAnswerByFkQuestionUID(fk_question_uid);
                assessmentViewModel.updateAnswer(new Answer(answer.getPk_answer_uid(), fk_question_uid, selected_answer, fk_user_uid));
            } else {
                // new insertAnswer
                assessmentViewModel.insertAnswer(new Answer(fk_question_uid, selected_answer, fk_user_uid));
            }
        }
    }

    private void observeAnswerList() {
        assessmentViewModel.getGetAllAnswerListLiveData().observe(getViewLifecycleOwner(), answers ->
                answersList = new ArrayList<>(answers));
    }

    /**
     * Iterates though the list of radio button and checks matching selected user selection and updateAnswer UI of selected radio buttons
     */
    private void upCheckedRadioButtons() {
        ArrayList<RadioButton> radioButtonList = getRadioButtonList();
        for (RadioButton radioButton : radioButtonList) {
            Question current_question = questionsList.get(position - 1);
            if ((radioButton.getText().toString()).equals(assessmentViewModel.getAnswerByFkQuestionUID(current_question.getPk_question_uid()) != null ? assessmentViewModel.getAnswerByFkQuestionUID(current_question.getPk_question_uid()).getSelected_answer() : "")) {
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