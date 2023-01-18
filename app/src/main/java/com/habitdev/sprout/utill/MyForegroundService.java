package com.habitdev.sprout.utill;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.habitdev.sprout.R;
import com.habitdev.sprout.activity.startup.Main;

import java.util.Calendar;

/**
 * MyForegroundService is a service that runs in the foreground and displays a notification to the user.
 * It schedules a notification at a specific time set by the user, even if the app is closed.
 * The service uses a foreground service and a notification channel to display the notification.
 * The service also uses shared preferences to store the time of the last scheduled notification,
 * so that it can be deleted if the user sets a new notification time before the previous one was triggered.
 */
public class MyForegroundService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "notification";
    private static final int MORNING_NOTIFICATION_ID = 10;
    private static final int EVENING_NOTIFICATION_ID = 11;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    /**
     * Handles the intent passed from the MainActivity and starts the foreground service
     * @param intent the intent passed from the MainActivity
     * @param flags additional data about this start request
     * @param startId a unique integer representing this specific request to start
     * @return the system will try to keep the service running as long as the service returns `START_STICKY`
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int hour = intent.getIntExtra("hour", 0);
        int minute = intent.getIntExtra("minute", 0);

        if (checkPermission()) {
            scheduleMorningNotification(6, 30);
            scheduleEveningNotification(16, 10);
        }

        scheduleNotification(hour, minute);
        startForeground(NOTIFICATION_ID, createNotification());
        return START_STICKY;
    }

    /**
     * Schedules a morning and evening notification to notify the user at a specific time set by the user, even if the app is closed.
     * @param hour the hour of the notification
     * @param minute the minute of the notification
     */
    private void scheduleNotification(int hour, int minute) {
        if (hour == 0 && minute == 0) {
            return;
        }

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, MyReceiver.class);
        notificationIntent.putExtra("hour", hour);
        notificationIntent.putExtra("minute", minute);
        pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Check if the scheduled time is in the past
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * Schedules a notification for a specific time
     * @param calendar the calendar instance that holds the time for the notification
     * @param NOTIFICATION_ID the id of the notification
     */
    private void scheduleNotification(Calendar calendar, int NOTIFICATION_ID) {
        // Check if the alarm is set in the past
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        // Set the alarm to fire on Monday, Tuesday, Wednesday, Thursday, Friday at the specified time
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            // Create a PendingIntent to trigger the alarm
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, new Intent(this, MyReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
            // Get an instance of the AlarmManager
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            // Set the alarm to fire on Monday, Tuesday, Wednesday, Thursday, Friday at the specified time
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 5, pendingIntent);
        }
    }

    /**
     * Schedules a morning notification with the given hour and minute.
     * @param hour the hour of the day (24-hour format)
     * @param minute the minute of the hour
     */
    private void scheduleMorningNotification(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        scheduleNotification(calendar, MORNING_NOTIFICATION_ID);
    }

    /**
     * Schedules an evening notification with the given hour and minute.
     * @param hour the hour of the day (24-hour format)
     * @param minute the minute of the hour
     */
    private void scheduleEveningNotification(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        scheduleNotification(calendar, EVENING_NOTIFICATION_ID);
    }

    /**
     * Creates a notification channel for the notifications to be shown in
     */
    @SuppressLint("ObsoleteSdkInt")
    private void createNotificationChannel() {
        // Create the notification channel for devices running Android Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Notification Service", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notification channel for scheduled notifications");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_round_notification)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
        }
    }

    /**
     * Starts the service and sets it as a foreground service with a notification
     * @param hour the hour of the notification
     * @param minute the minute of the notification
     */
    public void startForegroundService(int hour, int minute) {
        createNotificationChannel();
        scheduleNotification(hour, minute);
        Intent notificationIntent = new Intent(this, Main.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Notification Service")
                .setContentText("Running...")
                .setSmallIcon(R.drawable.ic_round_notification)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    /**
     * Stops the service and cancels all scheduled notifications
     */
    public void stopForegroundService() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        stopForeground(true);
        stopSelf();
    }

    /**
     * Creates a notification with a title, text, and an icon, as well as a PendingIntent that opens the MainActivity when the notification is tapped.
     * It also sets the notification to be auto-cancelled when tapped.
     *
     * @return A Notification object
     */
    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, Main.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notify")
                .setSmallIcon(R.drawable.ic_profile)
                .setContentTitle("My notification")
                .setContentText("Hello")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        return builder.build();
    }

    /**
     * Check if the permission is granted
     * @return true if permission is granted, false otherwise
     */
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SET_ALARM) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Stops the service and cancels all scheduled notifications
     */
    @Override
    public void onDestroy() {
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
        super.onDestroy();
    }
}
