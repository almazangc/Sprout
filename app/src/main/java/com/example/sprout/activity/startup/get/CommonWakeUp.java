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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putString(SAVE_WAKEHOUR, Integer.toString(binding.WakeTimePicker.getHour()));
        savedInstanceState.putString(SAVE_WAKEMINUTE, Integer.toString(binding.WakeTimePicker.getMinute()));
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.getInt(SAVE_WAKEHOUR, 0);
        binding.WakeTimePicker.setHour(Integer.parseInt(savedInstanceState.getString(SAVE_WAKEHOUR, "")));
        binding.WakeTimePicker.setMinute(Integer.parseInt(savedInstanceState.getString(SAVE_WAKEMINUTE, "")));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartupGetCommonWakeUpBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

//        for ui data restore when back pressed
        if(savedInstanceState != null) {
            binding.WakeTimePicker.setHour(savedInstanceState.getInt(SAVE_WAKEHOUR, 0));
            binding.WakeTimePicker.setMinute(savedInstanceState.getInt(SAVE_WAKEMINUTE, 0));
        } else {
            binding.WakeTimePicker.setHour(6);
            binding.WakeTimePicker.setMinute(30);
        }

        binding.btnContinue.setOnClickListener(view -> {
            Bundle bundle = getIntent().getBundleExtra(new BundleKey().getKEY_BUNDLE());
            bundle.putInt(new BundleKey().getKEY_WAKEHOUR(),binding.WakeTimePicker.getHour());
            bundle.putInt(new BundleKey().getKEY_WAKEMINUTE(), binding.WakeTimePicker.getMinute());
            startActivity((new Intent(this, CommonSleepTime.class).putExtra(new BundleKey().getKEY_BUNDLE(), bundle)));
        });
    }
}