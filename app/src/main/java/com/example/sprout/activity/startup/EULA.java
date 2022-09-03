package com.example.sprout.activity.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sprout.activity.Main;
import com.example.sprout.databinding.ActivityStartupEulaBinding;

public class EULA extends AppCompatActivity {

    ActivityStartupEulaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartupEulaBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnAgree.setOnClickListener(view -> {
            startActivity((new Intent(this, Introduction.class)));
        });

        binding.btnDisagree.setOnClickListener(view -> {
            startActivity((new Intent(this, Main.class)));
        });
    }
}