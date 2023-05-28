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

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;
import com.habitdev.sprout.databinding.FragmentPersonalizationBriefingBinding;

import java.util.Locale;

public class HabitSelfAssessmentBriefingFragment extends Fragment {

    private FragmentPersonalizationBriefingBinding binding;

    public HabitSelfAssessmentBriefingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalizationBriefingBinding.inflate(inflater, container, false);

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

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Start taking habit self assessment?")
                        .setCancelable(false)
                        .setPositiveButton("YES", (dialogInterface, i) -> {
                            Navigation.findNavController(view).navigate(R.id.action_navigate_from_personalizationBriefing_to_personalization);
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        return binding.getRoot();
    }

    private void setNumericalRepresentation(int value) {
        binding.lblLiveSampleQuestionNumericalRepresentation.setText(String.format(Locale.getDefault(), "%d%%", value));
    }
}