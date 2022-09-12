package com.example.sprout.activity.startup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.activity.startup.get.Nickname;
import com.example.sprout.databinding.ActivityStartupGreetingsBinding;

public class Greetings extends AppCompatActivity {

    ActivityStartupGreetingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartupGreetingsBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        Bundle bundle = getIntent().getBundleExtra("bundle");

        binding.btnContinue.setOnClickListener(view -> startActivity((new Intent(this, Nickname.class))
                .putExtra("bundle", bundle)
        ));
    }
}


