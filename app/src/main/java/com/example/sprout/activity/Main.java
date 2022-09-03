package com.example.sprout.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sprout.R;
import com.example.sprout.activity.startup.Initial;
import com.example.sprout.activity.startup.Introduction;
import com.example.sprout.databinding.ActivityMainBinding;

public class Main extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//      ViewBinding Method
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnMain.setOnClickListener(view -> {
            startActivity((new Intent(this, Introduction.class)));
        });
    }
}