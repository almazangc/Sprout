package com.example.sprout.activity.startup;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.databinding.ActivityStartupAnalysisBinding;

public class Analysis extends AppCompatActivity {

    ActivityStartupAnalysisBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartupAnalysisBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        String[] items = new String[]{"Positive", "Negative", "Alive", "Dead", "YOKOSO", "JAK AMMOOOOO", "TODO: USE DAO ROOM"};
//        binding.dropdown.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items));
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, android.R.layout.activity_list_item, items);
        binding.dropItems.setAdapter(adapterItems);
    }
}