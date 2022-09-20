package com.prototype.sprout.activity.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.prototype.sprout.databinding.ActivityMainBinding;
import com.prototype.sprout.databinding.OnbordingBinding;

public class Main extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}