package com.example.sprout.activity.startup.get;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sprout.R;
import com.example.sprout.activity.startup.Greetings;
import com.example.sprout.databinding.ActivityStartupGetNicknameBinding;
import com.example.sprout.databinding.ActivityStartupIntroductionBinding;

public class Nickname extends AppCompatActivity {

    ActivityStartupGetNicknameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartupGetNicknameBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnContinue.setOnClickListener(view -> {
            startActivity((new Intent(this, Identity.class)));
        });
    }

}
