package com.habitdev.sprout.utill;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;

import com.habitdev.sprout.R;

import java.text.SimpleDateFormat;

public class AlarmScheduler {
    private Context context;
    private AlarmManager alarmMgrMorning, alarmMgrEvening;
    private PendingIntent alarmIntentMorning, alarmIntentEvening;
    private static final String PREFS_NAME = "ALARM_SHARED_PREF";

    public AlarmScheduler(Context context) {
        this.context = context;
    }

    public AlarmScheduler() {
        //no args
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void scheduleMorningAlarm(Calendar calendar, String message) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedHour = prefs.getInt("morning_hour", -1);
        int savedMinute = prefs.getInt("morning_minute", -1);
        long savedTimeInMillis = prefs.getLong("morning_time_in_millis", -1);

        if (savedHour == calendar.get(Calendar.HOUR_OF_DAY)
                && savedMinute == calendar.get(Calendar.MINUTE)
                && isSameDay(savedTimeInMillis, calendar.getTimeInMillis())) {
            Log.d("tag", "Schedule Morning: Alarm already set for this time");
            return;
        }

        scheduleAlarm(calendar, "morning", message);
        prefs.edit()
                .putInt("morning_hour", calendar.get(Calendar.HOUR_OF_DAY))
                .putInt("morning_minute", calendar.get(Calendar.MINUTE))
                .putLong("morning_time_in_millis", calendar.getTimeInMillis())
                .apply();
        Log.d("tag", "Schedule Morning: Alarm Set");
    }

    public void scheduleEveningAlarm(Calendar calendar, String message) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedHour = prefs.getInt("evening_hour", -1);
        int savedMinute = prefs.getInt("evening_minute", -1);
        long savedTimeInMillis = prefs.getLong("evening_time_in_millis", -1);

        if (savedHour == calendar.get(Calendar.HOUR_OF_DAY)
                && savedMinute == calendar.get(Calendar.MINUTE)
                && isSameDay(savedTimeInMillis, calendar.getTimeInMillis())) {
            Log.d("tag", "Schedule Evening: Alarm already set for this time");
            return;
        }

        scheduleAlarm(calendar, "evening", message);
        prefs.edit()
                .putInt("evening_hour", calendar.get(Calendar.HOUR_OF_DAY))
                .putInt("evening_minute", calendar.get(Calendar.MINUTE))
                .putLong("evening_time_in_millis", calendar.getTimeInMillis())
                .apply();
        Log.d("tag", "Schedule Evening: Alarm Set");
    }

    private boolean isSameDay(long timeInMillis1, long timeInMillis2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTimeInMillis(timeInMillis1);
        calendar2.setTimeInMillis(timeInMillis2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

    @NonNull
    public Calendar setCaledendar(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public void updateMorningAlarm(Calendar calendar, String message) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int morningHour = prefs.getInt("morning_hour", -1);
        int morningMinute = prefs.getInt("morning_minute", -1);
        long savedTimeInMillis = prefs.getLong("morning_time_in_millis", -1);

        if (morningHour == calendar.get(Calendar.HOUR_OF_DAY) && morningMinute == calendar.get(Calendar.MINUTE) && savedTimeInMillis == calendar.getTimeInMillis()) {
            Log.d("tag", "updateMorningAlarm: already set for " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
            return;
        }

        cancelMorningAlarm();
        scheduleMorningAlarm(calendar, message);
        Log.d("tag", "updateMorningAlarm: alarm updated to " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
    }

    public void updateEveningAlarm(Calendar calendar, String message) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int eveningHour = prefs.getInt("evening_hour", -1);
        int eveningMinute = prefs.getInt("evening_minute", -1);
        long savedTimeInMillis = prefs.getLong("evening_time_in_millis", -1);

        if (eveningHour == calendar.get(Calendar.HOUR_OF_DAY) && eveningMinute == calendar.get(Calendar.MINUTE) && savedTimeInMillis == calendar.getTimeInMillis()) {
            Log.d("tag", "updateEveningAlarm: already set for " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
            return;
        }

        cancelEveningAlarm();
        scheduleEveningAlarm(calendar, message);
        Log.d("tag", "updateEveningAlarm: alarm updated to " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
    }

    public void cancelMorningAlarm() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (alarmMgrMorning != null) {
            alarmMgrMorning.cancel(alarmIntentMorning);
            alarmMgrMorning = null;

            prefs.edit().remove("morning_hour").remove("morning_minute").apply();
            Log.d("tag", "cancelledMorningAlarm: success");
            return;
        }

        if (alarmMgrMorning == null) {
            //Get existing morning alarm manager
            int morningHour = prefs.getInt("morning_hour", -1);
            int morningMinute = prefs.getInt("morning_minute", -1);

            if (morningHour != -1 && morningMinute != -1) {
                Intent intent = new Intent(context, AlarmReceiver.class);
                intent.putExtra("message", "message");
                PendingIntent morningPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmMgr.cancel(morningPendingIntent);

                prefs.edit().remove("morning_hour").remove("morning_minute").apply();
                Log.d("tag", "cancelMorningAlarm: success");
            } else {
                Log.d("tag", "cancelMorningAlarm: no alarm scheduled");
            }
        }
    }

    public void cancelEveningAlarm() {

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (alarmMgrEvening != null) {
            alarmMgrEvening.cancel(alarmIntentEvening);
            alarmMgrEvening = null;

            prefs.edit().remove("evening_hour").remove("evening_minute").apply();
            Log.d("tag", "cancelledEveningAlarm: success");
            return;
        }

        if (alarmMgrMorning == null) {
            //Get existing evening alarm manager
            int eveningHour = prefs.getInt("evening_hour", -1);
            int eveningMinute = prefs.getInt("evening_minute", -1);

            if (eveningHour != -1 && eveningMinute != -1) {
                Intent intent = new Intent(context, AlarmReceiver.class);
                intent.putExtra("type", "evening");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmMgr.cancel(pendingIntent);

                prefs.edit().remove("evening_hour").remove("evening_minute").apply();
                Log.d("tag", "cancelEveningAlarm: success");
            } else {
                Log.d("tag", "cancelEveningAlarm: no alarm scheduled");
            }
        }
    }

    public void scheduleAlarm(Calendar calendar, String type, String message) {

        final String SDFPattern = "yyyy-MM-dd HH:mm:ss"; //2000-01-01 00:00:00
        SimpleDateFormat dateFormat = new SimpleDateFormat(SDFPattern);

        String formattedDate = dateFormat.format(calendar.getTime());
        Log.d("tag", "scheduleAlarm: " + formattedDate);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(type);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Check if the app has the necessary permission to use setExactAndAllowWhileIdle
        if (!isIgnoringBatteryOptimizations()) {
            new AlertDialog.Builder(context)
                    .setMessage("In order to be notified daily, please disable battery optimization for this app in order to ensure timely delivery of notifications.")
                    .setCancelable(false)
                    .setPositiveButton("Go to settings", (dialogInterface, i) -> {
                        requestBatteryOptimizationsIgnored();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {

            // Schedule the alarm with setExactAndAllowWhileIdle
            Log.d("tag", "scheduleAlarm: scheduling alarm");
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmMgr.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );

            if (type.equals("morning")) {
                alarmMgrMorning = alarmMgr;
                alarmIntentMorning = pendingIntent;
            } else if (type.equals("evening")) {
                alarmMgrEvening = alarmMgr;
                alarmIntentEvening = pendingIntent;
            }
        }
    }

    private boolean isIgnoringBatteryOptimizations() {
        Log.d("tag", "isIgnoringBatteryOptimizations: checking battery optimization");
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
    }

    private void requestBatteryOptimizationsIgnored() {
        Log.d("tag", "scheduleAlarm: requesting battery optimization");
        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    public void turnOffDailyNotification() {
        cancelMorningAlarm();
        cancelEveningAlarm();
    }

    public void turnOnDailyNotifcation(Calendar morningCalendar, Calendar eveningCalendar) {
        scheduleMorningAlarm(morningCalendar, "Keep going, You can do it!");
        scheduleEveningAlarm(eveningCalendar, "Dont forget to update your progress!");
    }
}
