package com.example.sprout.activity.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sprout.activity.startup.get.CommonWakeUp;
import com.example.sprout.databinding.ActivityStartupIntroductionBinding;

public class Introduction extends AppCompatActivity {

    ActivityStartupIntroductionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartupIntroductionBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnContinue.setOnClickListener(view -> {
            startActivity((new Intent(this, CommonWakeUp.class)));
        });
    }
}
