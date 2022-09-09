package com.example.sprout.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.activity.startup.Introduction;
import com.example.sprout.databinding.ActivityMainBinding;

public class Main extends AppCompatActivity {

    ActivityMainBinding binding;
//
    Boolean isDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnMain.setOnClickListener(view -> {
            startActivity((new Intent(this, Introduction.class)));
        });
    }

//    TODO: After analysis of inputs
    public void setIsDone(boolean isDone){
        this.isDone = isDone;
    }
}