package com.example.sprout.activity.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.sprout.databinding.ActivityActivityStartupGetStartedBinding;

public class GetStarted extends AppCompatActivity {

    ActivityActivityStartupGetStartedBinding getStartedBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getStartedBinding = ActivityActivityStartupGetStartedBinding.inflate(getLayoutInflater());
        View getStartedBindingRoot = getStartedBinding.getRoot();
        setContentView(getStartedBindingRoot);
    }
}