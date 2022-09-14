package com.example.sprout.activity.startup.get;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.databinding.ActivityStartupGetNicknameBinding;
import com.example.sprout.model.BundleKey;

public class Nickname extends AppCompatActivity {

    ActivityStartupGetNicknameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartupGetNicknameBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnContinue.setOnClickListener(view -> {

            String nickname = binding.editTextTextPersonName.getText().toString();

            if (nickname.equals("")) {
                Toast.makeText(this, "Please enter a nickname", Toast.LENGTH_SHORT).show();
            } else {
                Bundle bundle = getIntent().getBundleExtra(new BundleKey().getKEY_BUNDLE());
                bundle.putString(new BundleKey().getKEY_NICKNAME(), nickname);
                startActivity((new Intent(this, Identity.class)).putExtra(new BundleKey().getKEY_BUNDLE(), bundle));
            }
        });
    }
}
