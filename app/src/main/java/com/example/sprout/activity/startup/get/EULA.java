package com.example.sprout.activity.startup.get;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.activity.Main;
import com.example.sprout.databinding.ActivityStartupEulaBinding;
import com.example.sprout.model.BundleKey;

public class EULA extends AppCompatActivity {

    ActivityStartupEulaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartupEulaBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnAgree.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(new BundleKey().getKEY_EULA(), true);
            EULA.this.startActivity((new Intent(EULA.this, CommonWakeUp.class).putExtra(new BundleKey().getKEY_BUNDLE(), bundle)));
        });

        binding.btnDisagree.setOnClickListener(view -> startActivity((new Intent(this, Main.class))));
    }
}