package com.example.sprout.activity.startup.get;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sprout.databinding.ActivityStartupGetCommonWakeUpBinding;

public class CommonWakeUp extends AppCompatActivity {

    ActivityStartupGetCommonWakeUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartupGetCommonWakeUpBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnContinue.setOnClickListener(view -> {
            startActivity((new Intent(this, CommonSleepTime.class)));
        });

        binding.WakeTimePicker.setHour(6);
        binding.WakeTimePicker.setMinute(30);
    }
}