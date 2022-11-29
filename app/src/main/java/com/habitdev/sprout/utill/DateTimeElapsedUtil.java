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
    private String date_started;

    public DateTimeElapsedUtil(String date_started) {
        this.date_started = date_started;
    }

    public void calculateElapsedDateTime() {

        SimpleDateFormat simpleDateFormatPattern = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault());

        try {
            Date start_date = simpleDateFormatPattern.parse(date_started);
            Date current_date = simpleDateFormatPattern.parse((simpleDateFormatPattern.format(System.currentTimeMillis())));

            long elapsed_time = current_date.getTime() - start_date.getTime();

            long elapsed_seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed_time) % 60;
            long elapsed_minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed_time) % 60;
            long elapsed_hours = TimeUnit.MILLISECONDS.toHours(elapsed_time) % 24;
            long elapsed_days = TimeUnit.MILLISECONDS.toDays(elapsed_time) % 365;
            long elapsed_years = TimeUnit.MILLISECONDS.toDays(elapsed_time) / 365L;

            formatResult(elapsed_years, elapsed_days, elapsed_hours, elapsed_minutes, elapsed_seconds);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void formatResult(long y, long d, long h, long m, long s) {
        result = ((y == 0 ? "" : y + ((y > 1) ? y + " year's " : y + " year "))
                + (d == 0 ? "" : (d > 1) ? d + " Days, " : d + " Day, ")
                + (h == 0 ? "00:" : (h > 9) ? h + ":" : "0" + h + ":")
                + (m == 0 ? "00:" : (m > 9) ? m + ":" : "0" + m + ":")
                + (s == 0 ? "00" : (s > 9) ? s + "" : "0" + s + "")
        ).trim();
    }

    public String getResult() {
        return result;
    }
}
