package com.example.sprout.activity.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sprout.R;
import com.example.sprout.activity.startup.get.CommonWakeUp;

public class Introduction extends AppCompatActivity {

    Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_introduction);

        btn_next = (Button) findViewById(R.id.btn_continue);

        btn_next.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CommonWakeUp.class);
            startActivity(intent);
        });
    }
}
