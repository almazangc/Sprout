package com.example.sprout.activity.startup;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.databinding.ActivityActivityStartupGetStartedBinding;

public class GetStarted extends AppCompatActivity {

    ActivityActivityStartupGetStartedBinding getStartedBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getStartedBinding = ActivityActivityStartupGetStartedBinding.inflate(getLayoutInflater());
        View getStartedBindingRoot = getStartedBinding.getRoot();
        setContentView(getStartedBindingRoot);
        getStartedBinding.lblWelcome.setText(String.format("Warm Welcome %s", getIntent().getStringExtra("Nickname")));
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"I had disabled it! no take backs!!!!", Toast.LENGTH_SHORT).show();
//        super.onBackPressed();
    }
}