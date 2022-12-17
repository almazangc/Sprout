package com.habitdev.sprout.utill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateTimeElapsedUtil {

    private String result;
    private String date_started;
    private  int patternType;
    private long elapsed_day;
    private Date start_date, current_date;

    public DateTimeElapsedUtil(String date_started) {
        this.date_started = date_started;
        patternType = 0;
    }

    public DateTimeElapsedUtil(String date_started, int patternType) {
        this.date_started = date_started;
        this.patternType = patternType;
    }

    public void setDate_started(String date_started) {
        this.date_started = date_started;
    }

    public void setPatternType(int patternType) {
        this.patternType = patternType;
    }

    public void calculateElapsedDateTime() {

        SimpleDateFormat[] simpleDateFormatPattern = new SimpleDateFormat[2];
        simpleDateFormatPattern [0] = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault());
        simpleDateFormatPattern [1] = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        try {
            start_date = simpleDateFormatPattern[patternType].parse(date_started);
            current_date = simpleDateFormatPattern[patternType].parse((simpleDateFormatPattern[patternType].format(System.currentTimeMillis())));

            long elapsed_time = current_date.getTime() - start_date.getTime(); //assert null

            long elapsed_seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed_time) % 60;
            long elapsed_minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed_time) % 60;
            long elapsed_hours = TimeUnit.MILLISECONDS.toHours(elapsed_time) % 24;
            long elapsed_days = TimeUnit.MILLISECONDS.toDays(elapsed_time) % 365;
            long elapsed_years = TimeUnit.MILLISECONDS.toDays(elapsed_time) / 365L;

            elapsed_day = elapsed_days;

            formatResult(elapsed_years, elapsed_days, elapsed_hours, elapsed_minutes, elapsed_seconds);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void convertToDate(){
        final SimpleDateFormat[] simpleDateFormatPattern = new SimpleDateFormat[2];
        simpleDateFormatPattern [0] = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss a", Locale.getDefault());
        simpleDateFormatPattern [1] = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        try {
            start_date = simpleDateFormatPattern[patternType].parse(date_started);
            current_date = simpleDateFormatPattern[patternType].parse((simpleDateFormatPattern[patternType].format(System.currentTimeMillis())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void formatResult(long y, long d, long h, long m, long s) {
        result = ((y == 0 ? "" : y + ((y > 1) ? y + " Year's " : y + " Years "))
                + (d == 0 ? "" : (d > 1) ? d + " Days " : d + " Day ")
                + (h == 0 ? "" : (h > 1 )? h + " Hours " : h + "Hour ")
                + (m == 0 ? "" : (m > 1) ? m + " Minutes " : m + " Minute ")
                + (s == 0 ? "" : (s > 1) ? s + " Seconds" : s + " Second")
        ).trim();
    }

    public Date getStart_date() {
        return start_date;
    }

    public Date getCurrent_date() {
        return current_date;
    }

    public long getElapsed_day() {
        return elapsed_day;
    }

    public String getResult() {
        return result;
    }
}
