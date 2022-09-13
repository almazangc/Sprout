package com.example.sprout.activity.startup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.R;
import com.example.sprout.activity.Main;
import com.example.sprout.databinding.ActivityStartupAnalysisBinding;

public class Analysis extends AppCompatActivity {

    ActivityStartupAnalysisBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartupAnalysisBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

//        TODO: CREATE ROOM DATABASE
        String[] items = new String[]{"YES, AN UPGRADE", "YES, GONNA RESELL IT", "NAH NO NEED", "...... Im speechless"};
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, R.layout.list_item, items);
        binding.dropItems.setAdapter(adapterItems);

        binding.btnContinue.setOnClickListener(view -> {

        });
    }


}
