package com.example.sprout.fragment;

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
import androidx.navigation.Navigation;

import com.example.sprout.R;
import com.example.sprout.database.AppDatabase;
import com.example.sprout.database.Assestment.Assestment;
import com.example.sprout.databinding.FragmentPersonalizationBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link personalizationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class personalizationFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentPersonalizationBinding binding;
    private static int uid = 1;
    private List<Assestment> currentQuestion = new ArrayList<>();

    public personalizationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalizationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static personalizationFragment newInstance(String param1, String param2) {
        personalizationFragment fragment = new personalizationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        currentQuestion = getCurrentAssessments(uid);
        setText();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.btnContinue.setOnClickListener(view -> {

            AppDatabase.getDbInstance(requireContext()).assestmentDao().updateSelectedUID(uid-1, addListenerOnButton());

            currentQuestion = getCurrentAssessments(uid);

            if (!currentQuestion.isEmpty()) {
                setText();
            } else {
                uid--;
                Toast.makeText(requireContext(),"End of Questions", Toast.LENGTH_LONG).show();

                new AlertDialog.Builder(requireContext())
                        .setMessage("Are you done answering all?")
                        .setCancelable(false)
                        //analysis
                        .setPositiveButton("Yes", (dialogInterface, i) -> Navigation.findNavController(view).navigate(R.id.action_navigate_from_personalization_to_analysis))
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                uid -= 2;
                currentQuestion = getCurrentAssessments(uid);
                setText();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private List<Assestment> getCurrentAssessments(int uid){
        personalizationFragment.uid++;
        return currentQuestion = AppDatabase.getDbInstance(requireContext()).assestmentDao().getQuestionUID(uid);
    }

    private void setText(){
        binding.lblQuestion.setText(currentQuestion.get(0).getQuestion());
        binding.radioAselect.setText(currentQuestion.get(0).getAselect());
        binding.radioBselect.setText(currentQuestion.get(0).getBselect());
        binding.radioCselect.setText(currentQuestion.get(0).getCselect());
        binding.radioDselect.setText(currentQuestion.get(0).getDselect());
    }

    private String addListenerOnButton() {
        int selectedId = binding.radiogroupSelect.getCheckedRadioButtonId();
        RadioButton radioButton = (binding.getRoot().findViewById(selectedId));
        return radioButton.getText().toString();
    }
}