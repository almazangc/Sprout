package com.example.sprout.activity.startup.get;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.Database.AppDatabase;
import com.example.sprout.Database.Assestment;
import com.example.sprout.Database.User;
import com.example.sprout.databinding.ActivityStartupPersonalizationBinding;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.Iterator;
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

        currentQuestion = getCurrentAssestment(1);
        Log.d("TAG", "onCreate: isempty " + currentQuestion.isEmpty());
        Log.d("TAG", "onCreate: " + currentQuestion.toString());

        setText();

        binding.btnContinue.setOnClickListener(view -> {
            currentQuestion = getCurrentAssestment(uid);
            if (!currentQuestion.isEmpty()) {
                setText();
            } else {
                //Do something here
                Toast.makeText(this,"End of Questions", Toast.LENGTH_LONG).show();
            }
        });
    }

//    TODO: FETCHER of Personalization Question, and ROOM database populate once upon insatallation
    private List<Assestment>  getCurrentAssestment(int uid){
        Personalization.uid++;
        return currentQuestion = AppDatabase.getDbInstance(getApplicationContext()).assestmentDao().getQuestionbyUID(uid);
    }

    @Override
    public void onBackPressed() {
        uid -= 2;
        currentQuestion = getCurrentAssestment(uid);
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