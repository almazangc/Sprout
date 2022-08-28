package com.example.sprout.activity.startup.get;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.sprout.R;
import com.example.sprout.activity.startup.Greetings;

public class CommonSleepTime extends AppCompatActivity {

    Button btn_greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_get_common_sleep_time);

        btn_greeting = (Button) findViewById(R.id.btn_continue);

        btn_greeting.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Greetings.class);
            startActivity(intent);
        });
    }
}