package com.example.sprout.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.Database.AppDatabase;
import com.example.sprout.Database.User;
import com.example.sprout.activity.startup.Introduction;
import com.example.sprout.activity.startup.get.Personalization;
import com.example.sprout.databinding.ActivityMainBinding;

import java.util.List;

public class Main extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnMain.setOnClickListener(view -> {
            List<User> userList = AppDatabase.getDbInstance(getApplicationContext()).userDao().getAllUser();
            if (!userList.isEmpty()) {
                startActivity((new Intent(this, Personalization.class)));
            } else {
                startActivity((new Intent(this, Introduction.class)));
            }
        });
    }
}