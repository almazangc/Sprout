package com.example.sprout.activity.startup.get;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.sprout.R;

public class CommonWakeUp extends AppCompatActivity {

    Button btn_setWakeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_get_common_wake_up);

        btn_setWakeTime = (Button) findViewById(R.id.btn_continue);

        btn_setWakeTime.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CommonSleepTime.class);
            startActivity(intent);
        });
    }
}