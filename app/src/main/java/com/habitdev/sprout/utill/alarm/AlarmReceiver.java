package com.habitdev.sprout.utill.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.habitdev.sprout.R;
import com.habitdev.sprout.activity.startup.Main;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "alarm_notification_channel_id";
    private static final String CHANNEL_NAME = "Daily Reminder";
    private static final String CHANNEL_DESC = "Daily Notification";
    private static final String PREFS_NAME = "ALARM_SHARED_PREF";
    
    private static final String TAG = "tag";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: alarm");

        if (intent.getAction().equals("morning")) {
            Log.d(TAG, "onReceive: show morning notify");
            showMorningNotification(context, intent);

        } else if (intent.getAction().equals("evening")) {
            Log.d(TAG, "onReceive: show evening notify");
            showEveningNotification(context, intent);

        } else {
            Log.d(TAG, "onReceive action: " + intent.getAction());
        }
    }

    private void showMorningNotification(Context context, Intent intent) {
        String message = intent.getStringExtra("message");

        Intent activityIntent = new Intent(context, Main.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_round_notification_on)
                .setContentTitle("Rise and shine!")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Log.d(TAG, "showMorningNotification: building notify");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(3, builder.build());

        // Reschedule the alarm for the next day

        AlarmScheduler alarmScheduler = new AlarmScheduler(context);

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedHour = prefs.getInt("morning_hour", -1);
        int savedMinute = prefs.getInt("morning_minute", -1);

        Calendar calendar = Calendar.getInstance();

        if (savedHour != -1 && savedMinute != -1) {
            calendar.set(Calendar.HOUR_OF_DAY, savedHour);
            calendar.set(Calendar.MINUTE, savedMinute);
            calendar.set(Calendar.SECOND, 0);
        }

        calendar.add(Calendar.DAY_OF_YEAR, 1);

        alarmScheduler.scheduleMorningAlarm(calendar, message);
    }

    private void showEveningNotification(Context context, Intent intent) {
        String message = intent.getStringExtra("message");

        Intent activityIntent = new Intent(context, Main.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_round_notification_on)
                .setContentTitle("It's been a long day, hasn't it?")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Log.d(TAG, "showEveningNotification: building notify");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(4, builder.build());

        // Reschedule the alarm for the next day
        AlarmScheduler alarmScheduler = new AlarmScheduler(context);

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedHour = prefs.getInt("evening_hour", -1);
        int savedMinute = prefs.getInt("evening_minute", -1);

        Calendar calendar = Calendar.getInstance();

        if (savedHour != -1 && savedMinute != -1) {
            calendar.set(Calendar.HOUR_OF_DAY, savedHour);
            calendar.set(Calendar.MINUTE, savedMinute);
            calendar.set(Calendar.SECOND, 0);
        }

        calendar.add(Calendar.DAY_OF_YEAR, 1);

        alarmScheduler.scheduleEveningAlarm(calendar, message);
    }
}

