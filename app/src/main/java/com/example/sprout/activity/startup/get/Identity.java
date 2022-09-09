package com.example.sprout.activity.startup.get;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.DbManager;
import com.example.sprout.activity.startup.GetStarted;
import com.example.sprout.databinding.ActivityStartupGetIdentityBinding;

public class Identity extends AppCompatActivity {

    ActivityStartupGetIdentityBinding binding;
    DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DbManager(this);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        int wakeHour = bundle.getInt("wakeHour");
        int wakeMinute = bundle.getInt("wakeMinute");
        int sleepHour = bundle.getInt("sleepHour");
        int sleepMinute = bundle.getInt("sleepMinute");
        String nickname = bundle.getString("nickname");

        Log.d("01", "Identity:  WakeTime: " + wakeHour + ":" + wakeMinute);
        Log.d("01", "Identity:  SleepTime: " + sleepHour + ":" + sleepMinute);
        Log.d("01", "Identity:  Nickname: " + nickname);

        binding = ActivityStartupGetIdentityBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnContinue.setOnClickListener(view -> {

            String identity = addListenerOnButton();

            new AlertDialog.Builder(this)
                    .setMessage("Please Confirm!\n" +
                            String.format("\n%-15s%d:%d", "Wake Time:", wakeHour, wakeMinute) +
                            String.format("\n%-15s%d:%d", "Sleep Time:", sleepHour, sleepMinute) +
                            String.format("\n%-15s%s", "Nickname:", nickname) +
                            String.format("\n%-20s%s", "Identity:", identity))
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        writeOnDB(nickname, identity, wakeHour, wakeMinute, sleepHour, sleepMinute);
                        startActivity((new Intent(this, GetStarted.class)).putExtra("Nickname", nickname));
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    //    Radio Selection Button
    public String addListenerOnButton() {
        int selectedId = binding.identitySelection.getCheckedRadioButtonId();
        RadioButton radioButton = (binding.getRoot().findViewById(selectedId));
        return radioButton.getText().toString();
    }

    private void writeOnDB(String nickname, String identity, int wakeHour, int wakeMinute, int sleepHour, int sleepMinute) {
        dbManager.open();
        dbManager.insert(nickname, identity, wakeHour, wakeMinute, sleepHour, sleepMinute);
        dbManager.close();
    }
}