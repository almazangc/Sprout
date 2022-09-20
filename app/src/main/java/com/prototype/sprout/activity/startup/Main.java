package com.prototype.sprout.activity.startup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.prototype.sprout.databinding.ActivityMainBinding;

public class Main extends AppCompatActivity {

    //ViewBinding
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
