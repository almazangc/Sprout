package com.example.sprout.activity.startup.get;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sprout.R;
import com.example.sprout.activity.startup.Greetings;
import com.example.sprout.activity.startup.Introduction;
import com.example.sprout.databinding.ActivityStartupGetCommonSleepTimeBinding;

public class CommonSleepTime extends AppCompatActivity {

    ActivityStartupGetCommonSleepTimeBinding binding;
    Button btn_greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartupGetCommonSleepTimeBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnContinue.setOnClickListener(view -> {
            startActivity((new Intent(this, Greetings.class)));
        });

        binding.SleepTimePicker.setHour(20);
        binding.SleepTimePicker.setMinute(15);
    }
}