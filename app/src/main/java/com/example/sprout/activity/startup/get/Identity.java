package com.example.sprout.activity.startup.get;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprout.database.AppDatabase;
import com.example.sprout.database.User.User;
import com.example.sprout.activity.startup.GetStarted;
import com.example.sprout.databinding.ActivityStartupGetIdentityBinding;
import com.example.sprout.model.BundleKey;

public class Identity extends AppCompatActivity {

    ActivityStartupGetIdentityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BundleKey bundleKey = new BundleKey();

        Bundle bundle = getIntent().getBundleExtra(bundleKey.getKEY_BUNDLE());
        boolean eula = bundle.getBoolean(bundleKey.getKEY_EULA());
        int wakeHour = bundle.getInt(bundleKey.getKEY_WAKEHOUR());
        int wakeMinute = bundle.getInt(bundleKey.getKEY_WAKEMINUTE());
        int sleepHour = bundle.getInt(bundleKey.getKEY_SLEEPHOUR());
        int sleepMinute = bundle.getInt(bundleKey.getKEY_SLEEPMINUTE());
        String nickname = bundle.getString(bundleKey.getKEY_NICKNAME());

        binding = ActivityStartupGetIdentityBinding.inflate(getLayoutInflater());
        View bindingRoot = binding.getRoot();
        setContentView(bindingRoot);

        binding.btnContinue.setOnClickListener(view -> {

            String identity = addListenerOnButton();

            new AlertDialog.Builder(this)
                    .setMessage("Please Confirm!\n" +
                            String.format("\n%-15s%d:%d", "Wake Time: ", wakeHour, wakeMinute) +
                            String.format("\n%-15s%d:%d", "Sleep Time:", sleepHour, sleepMinute) +
                            String.format("\n%-15s%s", "Nickname:", nickname) +
                            String.format("\n%-20s%s", "Identity:", identity))
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        saveUserData(nickname, identity, wakeHour, wakeMinute, sleepHour, sleepMinute, eula);
                        startActivity((new Intent(this, GetStarted.class)).putExtra(bundleKey.getKEY_NICKNAME(), nickname));
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    //    Radio Selection Button
    private String addListenerOnButton() {
        int selectedId = binding.identitySelection.getCheckedRadioButtonId();
        RadioButton radioButton = (binding.getRoot().findViewById(selectedId));
        return radioButton.getText().toString();
    }

    private void saveUserData(String nickname, String identity, int wakeHour, int wakeMinute, int sleepHour, int sleepMinute, boolean eula){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        User user = new User();
        user.nickname = nickname;
        user.identity = identity;
        user.wake_hour = wakeHour;
        user.wake_minute = wakeMinute;
        user.sleep_hour = sleepHour;
        user.sleep_minute = sleepMinute;
        user.agreed = eula;
        db.userDao().insert(user);
    }
}