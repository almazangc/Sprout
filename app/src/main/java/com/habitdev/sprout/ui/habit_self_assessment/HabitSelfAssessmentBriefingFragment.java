package com.habitdev.sprout.ui.habit_self_assessment;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.FragmentPersonalizationBriefingBinding;
import com.habitdev.sprout.ui.menu.setting.ui.ProfileFragment;

import java.util.Locale;

public class HabitSelfAssessmentBriefingFragment extends Fragment {

    private FragmentPersonalizationBriefingBinding binding;
    private static boolean isOnRekateAssessment;
    private static HabitSelfAssessmentFragment habitSelfAssessmentFragment;

    public HabitSelfAssessmentBriefingFragment() {
        // Required empty public constructor
    }

    public HabitSelfAssessmentBriefingFragment(boolean isOnRekateAssessment) {
        if (isOnRekateAssessment)
            habitSelfAssessmentFragment = new HabitSelfAssessmentFragment(true);

        HabitSelfAssessmentBriefingFragment.isOnRekateAssessment = isOnRekateAssessment;
    }

    public interface OnReturnSetting {
        void returnFromProfileToSetting();
    }

    private OnReturnSetting mOnReturnSetting;

    public void setmOnReturnSetting(OnReturnSetting mOnReturnSetting) {
        this.mOnReturnSetting = mOnReturnSetting;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalizationBriefingBinding.inflate(inflater, container, false);
        setRadioButtonStyle();
        setChoiceListener();
        setOnContinueListener();
        onBackPress();
        return binding.getRoot();
    }

    /**
     * Handles onBackPress Key
     */
    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isOnRekateAssessment) {
                   if(mOnReturnSetting != null)
                       mOnReturnSetting.returnFromProfileToSetting();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void setChoiceListener() {
        binding.choicesRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            String selected_answer = ((RadioButton)
                    (binding.getRoot().findViewById(binding.choicesRadioGroup.getCheckedRadioButtonId())))
                    .getText()
                    .toString();

            binding.lblLiveCurentlySelected.setText(selected_answer);
            //set equivalent value:
            switch (selected_answer) {
                case "Always":
                    setNumericalRepresentation(100);
                    break;
                case "Often":
                    setNumericalRepresentation(50);
                    break;
                case "Sometimes":
                    setNumericalRepresentation(30);
                    break;
                case "Rarely":
                    setNumericalRepresentation(10);
                    break;
                case "Never":
                    setNumericalRepresentation(0);
                    break;
                default:
                    //do nothing
                    break;
            }
        });
    }

    private void setOnContinueListener() {
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Start taking habit self assessment?")
                        .setCancelable(false)
                        .setPositiveButton("YES", (dialogInterface, i) -> {
                            if (isOnRekateAssessment) {
                                getChildFragmentManager()
                                        .beginTransaction()
                                        .addToBackStack(HabitSelfAssessmentBriefingFragment.this.getTag())
                                        .add(binding.personalizationBriefingRelativeLayout.getId(), habitSelfAssessmentFragment)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                        .commit();
                                binding.personalizationBriefingContainer.setVisibility(View.GONE);

                                habitSelfAssessmentFragment.setmOnReturnSetting(() -> {
                                    getChildFragmentManager()
                                            .beginTransaction()
                                            .remove(habitSelfAssessmentFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                            .commit();
                                    binding.personalizationBriefingContainer.setVisibility(View.VISIBLE);
                                });

                            } else {
                                Navigation.findNavController(view).navigate(R.id.action_navigate_from_personalizationBriefing_to_personalization);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void setRadioButtonStyle() {
        for (int i = 0; i < binding.choicesRadioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) binding.choicesRadioGroup.getChildAt(i);
            radioButton.setButtonDrawable(null);
            radioButton.setChecked(false);
            radioButton.setEllipsize(TextUtils.TruncateAt.END);
            radioButton.setPadding(10, 10, 10, 10);
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
            radioButton.setTextSize(16);
            radioButton.setBackgroundResource(R.drawable.selector_background_radio_button);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 10, 0, 10);
            radioButton.setLayoutParams(params);
        }

        ((RadioButton) binding.choicesRadioGroup.getChildAt(2)).setChecked(true);
    }

    private void setNumericalRepresentation(int value) {
        binding.lblLiveSampleQuestionNumericalRepresentation.setText(String.format(Locale.getDefault(), "%d%%", value));
    }
}