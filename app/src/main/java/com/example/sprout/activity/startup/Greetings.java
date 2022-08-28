package com.example.sprout.activity.startup;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sprout.R;
import com.example.sprout.activity.startup.get.Nickname;

public class Greetings extends AppCompatActivity {

    Button btn_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_greetings);

        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Nickname.class);
            startActivity(intent);
        });
    }
}


