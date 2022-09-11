package com.example.sprout.activity.startup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.Database.AppDatabase;
import com.example.sprout.Database.Assestment;
import com.example.sprout.Database.PopulateAssestmentDatabase;
import com.example.sprout.activity.startup.get.Personalization;
import com.example.sprout.databinding.ActivityActivityStartupGetStartedBinding;

public class GetStarted extends AppCompatActivity {

    ActivityActivityStartupGetStartedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityActivityStartupGetStartedBinding.inflate(getLayoutInflater());
        View getStartedBindingRoot = binding.getRoot();
        setContentView(getStartedBindingRoot);
        binding.lblWelcome.setText(String.format("Warm Welcome\n %s", getIntent().getStringExtra("Nickname")));

        binding.btnContinue.setOnClickListener(view -> {
            PopulateAssestmentDatabase populateAssestmentDatabase = new PopulateAssestmentDatabase(this.getApplicationContext());
            populateAssestmentDatabase.populateAssestmentDatabase();
            startActivity((new Intent(this, Personalization.class)));
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"I had disabled it! no take backs!!!!", Toast.LENGTH_SHORT).show();
    }
}