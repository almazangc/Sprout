package com.example.sprout.activity.startup.get;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.Database.AppDatabase;
import com.example.sprout.Database.Assestment;
import com.example.sprout.activity.startup.Analysis;
import com.example.sprout.activity.startup.GetStarted;
import com.example.sprout.databinding.ActivityStartupPersonalizationBinding;

import java.util.ArrayList;
import java.util.List;

public class Personalization extends AppCompatActivity {

    ActivityStartupPersonalizationBinding binding;
    private static int uid = 0;
    private List<Assestment> currentQuestion = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartupPersonalizationBinding.inflate(getLayoutInflater());
        View getStartedBindingRoot = binding.getRoot();
        setContentView(getStartedBindingRoot);

        currentQuestion = getCurrentAssessments(1);
        setText();

        binding.btnContinue.setOnClickListener(view -> {
            AppDatabase.getDbInstance(getApplicationContext()).assestmentDao().updateSelectedbyUID(uid-1, addListenerOnButton());
            currentQuestion = getCurrentAssessments(uid);
            if (!currentQuestion.isEmpty()) {
                setText();
            } else {
                //Do something here
                uid--;
                Toast.makeText(this,"End of Questions", Toast.LENGTH_LONG).show();

                new AlertDialog.Builder(this)
                        .setMessage("Are you done answering all?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            startActivity((new Intent(this, Analysis.class)));
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

//    TODO: FETCHER of Personalization Question, and ROOM database populate once upon installation
    private List<Assestment> getCurrentAssessments(int uid){
        Personalization.uid++;
        return currentQuestion = AppDatabase.getDbInstance(getApplicationContext()).assestmentDao().getQuestionbyUID(uid);
    }

    @Override
    public void onBackPressed() {
        uid -= 2;
        currentQuestion = getCurrentAssessments(uid);
        setText();
    }

    private void setText(){
        binding.lblQuestion.setText(currentQuestion.get(0).getQuestion());
        binding.radioAselect.setText(currentQuestion.get(0).getAselect());
        binding.radioBselect.setText(currentQuestion.get(0).getBselect());
        binding.radioCselect.setText(currentQuestion.get(0).getCselect());
        binding.radioDselect.setText(currentQuestion.get(0).getDselect());
    }

    /**
     * Listen ang get the id of the selected radio button
     * @return String selectedId
     */
    private String addListenerOnButton() {
        int selectedId = binding.radiogroupSelect.getCheckedRadioButtonId();
        RadioButton radioButton = (binding.getRoot().findViewById(selectedId));
        return radioButton.getText().toString();
    }
}