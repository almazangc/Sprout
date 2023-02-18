package com.habitdev.sprout.utill;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.habitdev.sprout.R;
import com.habitdev.sprout.activity.startup.Main;

public class AlarmReciever extends BroadcastReceiver {

    private static final String CHANNEL_ID = "alarm_notfication_channel_id";
    private static final String CHANNEL_NAME = "daily_notification_channel";
    private static final String CHANNEL_DESC = "Daily Notificaiton";
    
    private static final String TAG = "tag";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: alarm");

        if (intent.getAction().equals("morning")) {
            Log.d(TAG, "onReceive: morning notif");
            showMorningNotification(context, intent);

        } else if (intent.getAction().equals("evening")) {
            Log.d(TAG, "onReceive: evening notif");
            showEveningNotification(context, intent);
        } else {
            Log.d(TAG, "onReceive: no notif");
        }
    }

    private void showMorningNotification(Context context, Intent intent) {
        String message = intent.getStringExtra("message");

        Intent activityIntent = new Intent(context, Main.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_round_notification)
                .setContentTitle("Good morning!")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(3, builder.build());

        // Reschedule the alarm for the next day
        AlarmScheduler alarmScheduler = new AlarmScheduler(context);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        alarmScheduler.scheduleMorningAlarm(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), message);
    }

    private void showEveningNotification(Context context, Intent intent) {
        String message = intent.getStringExtra("message");

        Intent activityIntent = new Intent(context, Main.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_round_notification)
                .setContentTitle("Good evening!")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(4, builder.build());

        // Reschedule the alarm for the next day
        AlarmScheduler alarmScheduler = new AlarmScheduler(context);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        alarmScheduler.scheduleEveningAlarm(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), message);
    }
}

