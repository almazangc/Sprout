package com.habitdev.sprout.utill;

import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateTimeElapsedUtil {

    private String result;
    private final String date_started;
    private final int patternType;
    private long elapsed_day;

    public DateTimeElapsedUtil(String date_started) {
        this.date_started = date_started;
        this.patternType = 0;
    }

    public DateTimeElapsedUtil(String date_started, int patternType) {
        this.date_started = date_started;
        this.patternType = patternType;
    }

    public void calculateElapsedDateTime() {

        SimpleDateFormat[] simpleDateFormatPattern = new SimpleDateFormat[2];
        simpleDateFormatPattern [0] = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault());
        simpleDateFormatPattern [1] = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        try {
            Date start_date = simpleDateFormatPattern[patternType].parse(date_started);
            Date current_date = simpleDateFormatPattern[patternType].parse((simpleDateFormatPattern[patternType].format(System.currentTimeMillis())));

            long elapsed_time = current_date.getTime() - start_date.getTime(); //assert null

            long elapsed_seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed_time) % 60;
            long elapsed_minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed_time) % 60;
            long elapsed_hours = TimeUnit.MILLISECONDS.toHours(elapsed_time) % 24;
            long elapsed_days = TimeUnit.MILLISECONDS.toDays(elapsed_time) % 365;
            long elapsed_years = TimeUnit.MILLISECONDS.toDays(elapsed_time) / 365L;

            this.elapsed_day = elapsed_days;

            formatResult(elapsed_years, elapsed_days, elapsed_hours, elapsed_minutes, elapsed_seconds);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void formatResult(long y, long d, long h, long m, long s) {
        result = ((y == 0 ? "" : y + ((y > 1) ? y + " Year's " : y + " Years "))
                + (d == 0 ? "" : (d > 1) ? d + " Days " : d + " Day ")
                + (h == 0 ? "" : (h > 9) ? h + " Hours " : "0" + h + " Hours ")
                + (m == 0 ? "" : (m > 9) ? m + " Minutes " : "0" + m + " Minutes ")
                + (s == 0 ? "" : (s > 9) ? s + " Seconds " : "0" + s + " Seconds")
        ).trim();
    }

    public long getElapsed_day() {
        return elapsed_day;
    }

    public String getResult() {
        return result;
    }
}
