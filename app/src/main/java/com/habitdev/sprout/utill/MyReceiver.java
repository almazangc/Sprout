package com.habitdev.sprout.utill;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.habitdev.sprout.R;
import com.habitdev.sprout.activity.startup.Main;

public class MyReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        int hour = intent.getIntExtra("hour", 0);
        int minute = intent.getIntExtra("minute", 0);
        showNotification(context, hour, minute);
    }

    /**
     * Shows a notification to the user at the scheduled time set by the user
     * @param context the context of the app
     * @param hour the hour of the scheduled notification
     * @param minute the minute of the scheduled notification
     */
    private void showNotification(Context context, int hour, int minute) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                .setSmallIcon(R.drawable.ic_profile)
                .setContentTitle("Scheduled Notification")
                .setContentText("Notification scheduled for " + hour + ":" + minute)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
