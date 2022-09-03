package com.example.sprout.activity.startup.get;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sprout.activity.startup.GetStarted;
import com.example.sprout.databinding.ActivityStartupGetIdentityBinding;

public class Identity extends AppCompatActivity {

    ActivityStartupGetIdentityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartupGetIdentityBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnContinue.setOnClickListener(view -> {
            startActivity((new Intent(this, GetStarted.class)));
        });
    }
}