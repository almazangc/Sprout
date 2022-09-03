package com.example.sprout.activity.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sprout.databinding.ActivityStartupInitialBinding;

public class Initial extends AppCompatActivity {

    ActivityStartupInitialBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartupInitialBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnLetsDoIt.setOnClickListener(view -> {
            startActivity((new Intent(this, EULA.class)));
        });
    }
}
