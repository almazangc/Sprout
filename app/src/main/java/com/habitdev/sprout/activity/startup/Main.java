package com.habitdev.sprout.activity.startup;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.habitdev.sprout.R;
import com.habitdev.sprout.database.achievement.AchievementViewModel;
import com.habitdev.sprout.database.achievement.model.Achievement;
import com.habitdev.sprout.database.habit.model.room.Habits;
import com.habitdev.sprout.database.habit.model.room.Subroutines;
import com.habitdev.sprout.database.habit.room.HabitWithSubroutinesViewModel;
import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.database.user.model.User;
import com.habitdev.sprout.databinding.ActivityMainBinding;
import com.habitdev.sprout.enums.AnalyticConfigurationKeys;
import com.habitdev.sprout.enums.HomeConfigurationKeys;
import com.habitdev.sprout.enums.TimeMilestone;
import com.habitdev.sprout.utill.dialog.CompletedAchievementDialogFragment;
import com.habitdev.sprout.utill.diffutils.DateTimeElapsedUtil;
import com.habitdev.sprout.utill.network.NetworkMonitoringUtil;
import com.habitdev.sprout.utill.network.NetworkStateManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p><b>Sprout:  HABIT BUSTER REFORM WITH BITE-SIZE SUBROUTINES</b></p>
 * <br>
 * <p>The study was conducted to design and develop an android mobile-based habit bite-size routine recommender to assist in monitoring, breaking, or reforming bad habits.</p>
 * <p>The proposed system is expected to solve and experiment with a fresh concept by integrating bite-size subroutines as support for breaking and habilitating bad habits.</p>
 * <p>The researchers used the general Agile software process model to develop the proposed Sprout.</p>
 * <p>The acceptability of the developed system will be determined using the ISO 25010 software quality standard tool along with:</p>
 * <br>
 * <p>a. functional suitability</p>
 * <p>b. performance efficiency</p>
 * <p>c. compatibility</p>
 * <p>d. usability</p>
 * <p>e. reliability</p>
 * <p>f. security</p>
 * <br>
 * <p><b>Development Started:</b> August 27</p>
 * <p><b>Min SDK:</b> 28 Android 9 Pie 98% Commutative Usage</p>
 * <p><b>Target SDK:</b> 32 Android 12L Snowcone</p>
 * <p>Status: 72% Complete</p>
 * <br>
 *
 * @author Almazan, Gilbert C.
 * @version 1.0
 * @since 07-27-2022
 */
public class Main extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static SharedPreferences sharedPreferences;
    private static boolean isDataAvailableOnLocal;
    private static final int REQUEST_CODE_PERMISSION = 1;

    private enum MAIN_ENUMS {
        SHARED_PREF_KEY("MAINSHARED.PREF"),
        DAILY_DATE_KEY("DAILY_DATE_KEY.STRING"),
        WEEKLY_DATE_KEY("WEEKLY_DATE_KEY.STRING"),
        SDF_PATTERN("dd MMMM yyyy"),
        DB_LOADED("DB_LOADED.BOOL"),
        IS_DATA_AVAILABLE_ON_LOCAL("IS_DATA_AVAILABLE_ON_LOCAL.BOOL"),
        NOTIFICATION_CHANNEL_1("NOTIFY.CHANNEL_1"),
        NOTIFICATION_CHANNEL_2("NOTIFY.CHANNEL_2");

        final String value;

        MAIN_ENUMS(String value) {
            this.value = value;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            isDataAvailableOnLocal = savedInstanceState.getBoolean(MAIN_ENUMS.IS_DATA_AVAILABLE_ON_LOCAL.value);
        }
        clearSharedPref();
        fetchFirestoreDB();
        setDailyDateTracker();
        if (!checkPermission()) {
            requestPermissions();
        }
        setTimeElapsedSinceInstalled();
        setContentView(binding.getRoot());
    }

    private void setTimeElapsedSinceInstalled() {
        UserViewModel userViewModel = new ViewModelProvider(Main.this).get(UserViewModel.class);
        Log.d("tag", "setTimeElapsedSinceInstalled: " + userViewModel.getUserCount());
        if (userViewModel.getUserCount() > 0) {
            User user = userViewModel.getUserByUID(1);
            String date = user.getDateInstalled();

            DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(date);
            final HabitWithSubroutinesViewModel habitWithSubroutinesViewModel = new ViewModelProvider(Main.this).get(HabitWithSubroutinesViewModel.class);
            final AchievementViewModel achievementViewModel = new ViewModelProvider(Main.this).get(AchievementViewModel.class);
            //TODO: UPDATE UID WHEN APPDATABASE CHANGE
            Achievement YEAR = achievementViewModel.getAchievementByUID(23);
            Achievement WEEK = achievementViewModel.getAchievementByUID(20);
            Achievement MONTH = achievementViewModel.getAchievementByUID(21);
            Achievement THREE_MONTH = achievementViewModel.getAchievementByUID(22);

            Achievement SubroutineVI = achievementViewModel.getAchievementByUID(12);
            Achievement SubroutineV = achievementViewModel.getAchievementByUID(SubroutineVI.getPrerequisite_uid());
            Achievement SubroutineIV = achievementViewModel.getAchievementByUID(SubroutineV.getPrerequisite_uid());
            Achievement SubroutineIII = achievementViewModel.getAchievementByUID(SubroutineIV.getPrerequisite_uid());
            Achievement SubroutineII = achievementViewModel.getAchievementByUID(SubroutineIII.getPrerequisite_uid());
            Achievement SubroutineI = achievementViewModel.getAchievementByUID(SubroutineII.getPrerequisite_uid());

            final int[] totalCompletedSubroutine = {0};
            habitWithSubroutinesViewModel.getTotalCompletedSubroutineCountLiveData().observe(Main.this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer totalCompletedSubroutineCount) {
                    totalCompletedSubroutine[0] = totalCompletedSubroutineCount;
                    if (!SubroutineI.is_completed() && totalCompletedSubroutine[0] >= 1) {
                        updateUnlockedAchievement(SubroutineI);
                    } else if (SubroutineI.is_completed() && !SubroutineII.is_completed() && SubroutineII.getGoal_progress() == totalCompletedSubroutineCount) {
                        updateUnlockedAchievement(SubroutineII);
                    } else if (SubroutineI.is_completed() && !SubroutineII.is_completed() && SubroutineII.getGoal_progress() - 1 >= totalCompletedSubroutineCount) {
                        setAchievementCurrentProgress(SubroutineII);
                    } else if (SubroutineII.is_completed() && !SubroutineIII.is_completed() && SubroutineIII.getGoal_progress() == totalCompletedSubroutineCount) {
                        updateUnlockedAchievement(SubroutineIII);
                    } else if (SubroutineII.is_completed() && !SubroutineIII.is_completed() && SubroutineIII.getGoal_progress() - 1 >= totalCompletedSubroutineCount) {
                        setAchievementCurrentProgress(SubroutineIII);
                    } else if (SubroutineIII.is_completed() && !SubroutineIV.is_completed() && SubroutineIV.getGoal_progress() == totalCompletedSubroutineCount) {
                        updateUnlockedAchievement(SubroutineIV);
                    } else if (SubroutineIII.is_completed() && !SubroutineIV.is_completed() && SubroutineIV.getGoal_progress() - 1 >= totalCompletedSubroutineCount) {
                        setAchievementCurrentProgress(SubroutineIV);
                    } else if (SubroutineIV.is_completed() && !SubroutineV.is_completed() && SubroutineV.getGoal_progress() == totalCompletedSubroutineCount) {
                        updateUnlockedAchievement(SubroutineV);
                    } else if (SubroutineIV.is_completed() && !SubroutineV.is_completed() && SubroutineV.getGoal_progress() - 1 >= totalCompletedSubroutineCount) {
                        setAchievementCurrentProgress(SubroutineV);
                    } else if (SubroutineV.is_completed() && !SubroutineVI.is_completed() && SubroutineVI.getGoal_progress() == totalCompletedSubroutineCount) {
                        updateUnlockedAchievement(SubroutineVI);
                        habitWithSubroutinesViewModel.getTotalCompletedSubroutineCountLiveData().removeObservers(Main.this);
                    } else if (SubroutineV.is_completed() && !SubroutineVI.is_completed() && SubroutineVI.getGoal_progress() - 1 >= totalCompletedSubroutineCount) {
                        setAchievementCurrentProgress(SubroutineVI);
                    }
                }

                private void setAchievementCurrentProgress(Achievement subroutineAchievement) {
                    subroutineAchievement.setCurrent_progress(totalCompletedSubroutine[0]);
                    achievementViewModel.updateAchievement(subroutineAchievement);
                }

                private void updateUnlockedAchievement(Achievement subroutineAchievement) {
                    subroutineAchievement.setIs_completed(true);
                    setAchievementCurrentProgress(subroutineAchievement);
                    subroutineAchievement.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                    CompletedAchievementDialogFragment dialog = new CompletedAchievementDialogFragment(subroutineAchievement.getTitle());
                    dialog.show(getSupportFragmentManager(), "CompletedAchievementDialog");
                }
            });

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (binding != null) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            dateTimeElapsedUtil.calculateElapsedDateTime();
                            try {
                                long elapsedDay = dateTimeElapsedUtil.getElapsed_day();
                                if (elapsedDay == 7 && !WEEK.is_completed()) {
                                    WEEK.setIs_completed(true);
                                    WEEK.setCurrent_progress(WEEK.getCurrent_progress() + 1);
                                    WEEK.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                                    achievementViewModel.updateAchievement(WEEK);
                                    CompletedAchievementDialogFragment dialog = new CompletedAchievementDialogFragment(WEEK.getTitle());
                                    dialog.show(getSupportFragmentManager(), "CompletedAchievementDialog");
                                }

                                if (elapsedDay == 30 && !MONTH.is_completed()) {
                                    MONTH.setIs_completed(true);
                                    MONTH.setCurrent_progress(MONTH.getCurrent_progress() + 1);
                                    MONTH.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                                    achievementViewModel.updateAchievement(MONTH);
                                    CompletedAchievementDialogFragment dialog = new CompletedAchievementDialogFragment(MONTH.getTitle());
                                    dialog.show(getSupportFragmentManager(), "CompletedAchievementDialog");
                                }

                                if (elapsedDay == 90 && !THREE_MONTH.is_completed()) {
                                    THREE_MONTH.setIs_completed(true);
                                    THREE_MONTH.setCurrent_progress(THREE_MONTH.getCurrent_progress() + 1);
                                    THREE_MONTH.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                                    achievementViewModel.updateAchievement(THREE_MONTH);
                                    CompletedAchievementDialogFragment dialog = new CompletedAchievementDialogFragment(THREE_MONTH.getTitle());
                                    dialog.show(getSupportFragmentManager(), "CompletedAchievementDialog");
                                }

                                if (elapsedDay == 365 && !YEAR.is_completed()) {
                                    YEAR.setIs_completed(true);
                                    YEAR.setCurrent_progress(YEAR.getCurrent_progress() + 1);
                                    YEAR.setDate_achieved(new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date()));
                                    achievementViewModel.updateAchievement(YEAR);
                                    CompletedAchievementDialogFragment dialog = new CompletedAchievementDialogFragment(YEAR.getTitle());
                                    dialog.show(getSupportFragmentManager(), "CompletedAchievementDialog");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        Log.d("tag", "run: main purging timer");
                        timer.cancel();
                        timer.purge();
                    }
                }
            }, 0, 1000);
        }
    }

    private void requestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.SET_ALARM,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED
        };
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    break;
                }
            }
        }
    }

    /**
     * Check if the permission is granted
     *
     * @return true if permission is granted, false otherwise
     */
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SET_ALARM) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * <p>Checks for available network connection and fetch latest data from firestore database</p>
     */
    private void fetchFirestoreDB() {

        sharedPreferences = getSharedPreferences(MAIN_ENUMS.SHARED_PREF_KEY.value, Main.MODE_PRIVATE);

        NetworkMonitoringUtil mNetworkMonitoringUtil = new NetworkMonitoringUtil(getApplicationContext());
        mNetworkMonitoringUtil.checkNetworkState();
        mNetworkMonitoringUtil.registerNetworkCallbackEvents();

        NetworkStateManager networkStateManager = NetworkStateManager.getInstance();

        if (sharedPreferences.contains(MAIN_ENUMS.WEEKLY_DATE_KEY.value)) {
            String date = sharedPreferences.getString(MAIN_ENUMS.WEEKLY_DATE_KEY.value, null);
            final DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(date, 1);
            dateTimeElapsedUtil.calculateElapsedDateTime();

            if (dateTimeElapsedUtil.getElapsed_day() >= TimeMilestone.WEEKLY.getDays() && date != null) {
                networkStateManager.getNetworkConnectivityStatus().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isConnected) {
                        if (isConnected) {
                            FirebaseFirestore.getInstance().collection("quotes").get(Source.SERVER);
                            updateNewWeeklyDay();
                            networkStateManager.getNetworkConnectivityStatus().removeObserver(this);
                        } else {
                            NotificationChannel notificationChannel = new NotificationChannel(
                                    MAIN_ENUMS.NOTIFICATION_CHANNEL_1.value,
                                    "No Network Available",
                                    NotificationManager.IMPORTANCE_HIGH
                            );

                            notificationChannel.setDescription("No Network Connection Available To Fetch or Update Data From FireStore");
                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(notificationChannel);

                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Main.this);

                            Notification notification = new NotificationCompat.Builder(Main.this, MAIN_ENUMS.NOTIFICATION_CHANNEL_1.value)
                                    .setSmallIcon(R.drawable.ic_no_network)
                                    .setContentTitle("No Network Available")
                                    .setContentText("Please Connect to the Internet")
                                    .setSubText("Internet Connection")
                                    .setChannelId(MAIN_ENUMS.NOTIFICATION_CHANNEL_1.value)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                                    .build();
                            notificationManagerCompat.notify(1, notification);
                        }
                    }
                });
            } else {
                firstInstallDataFetch();
            }
        } else {
            updateNewWeeklyDay();
        }
    }

    /**
     * Logic for fetching only once from tha server
     */
    private void firstInstallDataFetch() {
        if (!isDataAvailableOnLocal) {
            if (!sharedPreferences.contains(MAIN_ENUMS.DB_LOADED.value) ||
                    (sharedPreferences.contains(MAIN_ENUMS.DB_LOADED.value) && sharedPreferences.getBoolean(MAIN_ENUMS.DB_LOADED.value, false))) {

                if (!sharedPreferences.getBoolean(MAIN_ENUMS.DB_LOADED.value, false)) {
                    FirebaseFirestore.getInstance().collection("quotes").get(Source.SERVER); //limits fetching of data
                    Log.d("tag", "Main isConnected() called: data is being fetch from server");
                }

                boolean isFetched = FirebaseFirestore.getInstance().collection("quotes").get(Source.CACHE).isSuccessful();
                sharedPreferences.edit().putBoolean(MAIN_ENUMS.DB_LOADED.value, isFetched).apply();
            } else {
                isDataAvailableOnLocal = true;
            }
        }
    }

    /**
     * Fetch latest data from firestore db on weekly basis only if network is available
     */
    private void updateNewWeeklyDay() {
        String date = new SimpleDateFormat(MAIN_ENUMS.SDF_PATTERN.value, Locale.getDefault()).format(new Date());
        sharedPreferences.edit().putString(MAIN_ENUMS.WEEKLY_DATE_KEY.value, date).apply();
    }

    /**
     * <p>Updates Subroutine Max Streak, Longest Streak, Total Skip and Resets MarkAsDone Status
     * <p>Daily after 12:00 AM on user first launch of app on the current day.
     * <p>This can also be used to notify user how long since the app was last opened and visited.
     */
    private void setDailyDateTracker() {

        if (sharedPreferences.contains(MAIN_ENUMS.DAILY_DATE_KEY.value)) {

            String date = sharedPreferences.getString(MAIN_ENUMS.DAILY_DATE_KEY.value, null);
            final DateTimeElapsedUtil dateTimeElapsedUtil = new DateTimeElapsedUtil(date, 1);
            dateTimeElapsedUtil.calculateElapsedDateTime();

            if (dateTimeElapsedUtil.getElapsed_day() >= TimeMilestone.DAILY.getDays() && date != null) {

                updateNewDailyDate();

                final HabitWithSubroutinesViewModel habitWithSubroutinesViewModel = new ViewModelProvider(this).get(HabitWithSubroutinesViewModel.class);
                final List<Habits> habitsList = habitWithSubroutinesViewModel.getAllHabitOnReform();

                long days = dateTimeElapsedUtil.getElapsed_day();

                for (Habits habit : habitsList) {
                    final List<Subroutines> subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());
                    for (Subroutines subroutine : subroutinesList) {
                        if (subroutine.isMarkDone()) {
                            subroutine.setMarkDone(false);
                            subroutine.setMax_streak(subroutine.getMax_streak() + 1);
                        } else {
                            subroutine.setMax_streak(0);
                            subroutine.setTotal_skips(subroutine.getTotal_skips() + 1);
                        }
                        if (subroutine.getLongest_streak() < subroutine.getMax_streak()) {
                            subroutine.setLongest_streak(subroutine.getMax_streak());
                        }
                        habitWithSubroutinesViewModel.updateSubroutine(subroutine);
                    }
                }
                days -= 1;

                if (days > 0) {
                    for (long i = 0; i <= days; days--) {
                        for (Habits habit : habitsList) {
                            final List<Subroutines> subroutinesList = habitWithSubroutinesViewModel.getAllSubroutinesOfHabit(habit.getPk_habit_uid());
                            for (Subroutines subroutine : subroutinesList) {
                                subroutine.setMax_streak(0);
                                subroutine.setTotal_skips(subroutine.getTotal_skips() + 1);
                                habitWithSubroutinesViewModel.updateSubroutine(subroutine);
                            }
                        }
                    }
                }

                String[] msg_greet = {
                        "Today is a new day.",
                        "Life is beautiful right?",
                        "Today is the perfect day for another progress.",
                        "Don’t forget to show gratitude today.",
                        "Today is going to be a lovely day.",
                        "Stay positive.",
                        "Don't let bad memories get you.",
                        "I hope you have a blessed day.",
                        "Life is blissful",
                        "Take a break once in a while",
                        "Have a good day and face life with courage!",
                        "There are so many ways to make today special!",
                        "It’s time to face the day with a smile on your face and hope in your heart.",
                        "A bright new day is here.",
                        "Stop worrying about tomorrow.",
                        "Focus on your blessings today.",
                        "Every single day is like a blank canvas. You can paint it however you wish.",
                        "Today is whatever you make it. You get to decide whether it’s a good day or a bad day."
                };

                NotificationChannel notificationChannel = new NotificationChannel(
                        MAIN_ENUMS.NOTIFICATION_CHANNEL_2.value,
                        "Daily Greeting",
                        NotificationManager.IMPORTANCE_HIGH
                );

                notificationChannel.setDescription(
                        dateTimeElapsedUtil.getElapsed_day() == TimeMilestone.DAILY.getDays() ?
                                "Updates Daily Reset" :
                                dateTimeElapsedUtil.getElapsed_day() + " days has passed since then"
                );

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(notificationChannel);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Main.this);

                Notification notification = new NotificationCompat.Builder(Main.this, MAIN_ENUMS.NOTIFICATION_CHANNEL_2.value)
                        .setSmallIcon(R.drawable.ic_smile)
                        .setContentText(msg_greet[new Random().nextInt(msg_greet.length)])
                        .setSubText("Welcome back")
                        .setChannelId(MAIN_ENUMS.NOTIFICATION_CHANNEL_2.value)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                        .build();
                notificationManagerCompat.notify(2, notification);
            }
        } else {
            updateNewDailyDate();
        }
    }

    /**
     * <p>Updates Shared Preference Daily Date</p>
     */
    private void updateNewDailyDate() {
        final String date = new SimpleDateFormat(MAIN_ENUMS.SDF_PATTERN.value, Locale.getDefault()).format(new Date());
        sharedPreferences.edit().putString(MAIN_ENUMS.DAILY_DATE_KEY.value, date).apply();
    }

    /**
     * <p>Clears Recent Shared Pref when Main Class lifecycle end and starts</p>
     * <p>By Clearing Stored Shared, Restore App on Default State on Restart</p>
     */
    private void clearSharedPref() {
        getSharedPreferences(HomeConfigurationKeys.HOME_SHAREDPREF.getKey(), Context.MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(HomeConfigurationKeys.HOME_ADD_DEFAULT_SHAREDPREF.getKey(), MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(HomeConfigurationKeys.HOME_ADD_NEW_SHAREDPREF.getKey(), Context.MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(HomeConfigurationKeys.HOME_HABIT_ON_MODIFY_SHARED_PREF.getKey(), MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(AnalyticConfigurationKeys.ANALYTIC_SHAREDPREF.getKey(), Context.MODE_PRIVATE).edit().clear().apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(MAIN_ENUMS.IS_DATA_AVAILABLE_ON_LOCAL.value, isDataAvailableOnLocal);
    }

    /**
     * <p>Freeing binding and clearing binding</p>
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        clearSharedPref();
    }
}
