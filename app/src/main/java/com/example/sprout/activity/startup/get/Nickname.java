package com.example.sprout.activity.startup.get;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.sprout.R;

public class Nickname extends AppCompatActivity{

    Button btn_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_get_nickname);

        btn_continue = (Button) findViewById(R.id.btn_continue);

        btn_continue.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Identity.class);
            startActivity(intent);
        });
    }

}
