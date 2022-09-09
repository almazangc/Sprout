package com.example.sprout.activity.startup.get;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.activity.Main;
import com.example.sprout.databinding.ActivityStartupGetCommonWakeUpBinding;

public class CommonWakeUp extends AppCompatActivity {

    ActivityStartupGetCommonWakeUpBinding binding;

    private static final String TAG = "TAG: ";
    private static final String SAVE_WAKEHOUR = "CommonWakeUp.SAVE_WAKEMINUTE";
    private static final String SAVE_WAKEMINUTE = "CommonWakeUp.SAVE_WAKEMINUTE";
    private static final String SAVE_TEXT = "CommonWakeUp.SAVE_TEXT";
    private static final String SAVE_TESTINT = "savetestint_int";
    private static int num = 10;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putString(SAVE_WAKEHOUR, Integer.toString(binding.WakeTimePicker.getHour()));
        savedInstanceState.putString(SAVE_WAKEMINUTE, Integer.toString(binding.WakeTimePicker.getMinute()));
        savedInstanceState.putString(SAVE_TEXT, binding.edtTxt.getText().toString());
        savedInstanceState.putInt(SAVE_TESTINT, num);
        Log.d(TAG, "onSaveInstanceState: Bundle done\n" +
                "Data: " + binding.WakeTimePicker.getHour() + ":" + binding.WakeTimePicker.getMinute() +
                "\nText: " + binding.edtTxt.getText().toString() +
                "\nInt: " + num);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.getInt(SAVE_WAKEHOUR, 0);
        binding.WakeTimePicker.setHour(Integer.parseInt(savedInstanceState.getString(SAVE_WAKEHOUR, "")));
        binding.WakeTimePicker.setMinute(Integer.parseInt(savedInstanceState.getString(SAVE_WAKEMINUTE, "")));
        binding.edtTxt.setText((CharSequence) savedInstanceState.getString(SAVE_TEXT, ""));
        Log.d(TAG, "onRestoreInstanceState: extracted bundle:\n" +
                "Data: " + savedInstanceState.getInt(SAVE_WAKEHOUR, 0) + ":" + savedInstanceState.getInt(SAVE_WAKEMINUTE, 0) +
                "\nText: " + savedInstanceState.getString(SAVE_TEXT, "") +
                "\nInt: " + num);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Oncreate: ");

        binding = ActivityStartupGetCommonWakeUpBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

//        for ui data restore when back pressed
        if(savedInstanceState != null) {
            Log.d(TAG, "onCreate: savedInstanceState is not null");
            Log.d(TAG, "onCreate: extracted bundle:\n" +
                    "Data: " + savedInstanceState.getInt(SAVE_WAKEHOUR, 0) + ":" + savedInstanceState.getInt(SAVE_WAKEMINUTE, 0) +
                    "\nText: " + savedInstanceState.getString(SAVE_TEXT, "") +
                    "\nInt: " + num);
//            binding.WakeTimePicker.setHour(savedInstanceState.getInt(SAVE_WAKEHOUR, 0));
//            binding.WakeTimePicker.setMinute(savedInstanceState.getInt(SAVE_WAKEMINUTE, 0));
        } else {
            // Default Initial Startup
            Log.d(TAG, "onCreate: savedInstance is null // Setting Default Time");
            binding.WakeTimePicker.setHour(6);
            binding.WakeTimePicker.setMinute(30);
        }

        binding.btnContinue.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("wakeHour",binding.WakeTimePicker.getHour());
            bundle.putInt("wakeMinute", binding.WakeTimePicker.getMinute());
            startActivity((new Intent(this, CommonSleepTime.class).putExtra("bundle", bundle)));
        });
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: called");
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: called");
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: called");
        super.onRestart();
    }
}