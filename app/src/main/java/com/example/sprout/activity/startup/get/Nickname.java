package com.example.sprout.activity.startup.get;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sprout.DBHelper;
import com.example.sprout.DBManager;
import com.example.sprout.databinding.ActivityStartupGetNicknameBinding;

public class Nickname extends AppCompatActivity {

    ActivityStartupGetNicknameBinding binding;

    DBHelper databaseHelper;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartupGetNicknameBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        databaseHelper = new DBHelper(this);
        dbManager = new DBManager(this);

        binding.btnContinue.setOnClickListener(view -> {

            String nickname = binding.editTextTextPersonName.getText().toString();

            if (nickname.equals("")) {
                Toast.makeText(this, "Please enter a nickname", Toast.LENGTH_SHORT).show();
            } else {
                dbManager.open();
                dbManager.insert(1, "TEST", "Undefined");
                dbManager.close();
                startActivity((new Intent(this, Identity.class)));
            }
        });
    }
}
