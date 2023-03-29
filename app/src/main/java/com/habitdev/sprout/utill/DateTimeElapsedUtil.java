package com.habitdev.sprout.utill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * DateTimeElapsedUtil is a utility class that calculates the elapsed time between a given start date and the current date.
 * The class can be instantiated with or without a pattern type. The pattern type is used to specify the format of the start date.
 */
public class DateTimeElapsedUtil {

    /**
     * A string that holds the final result of the elapsed time calculation.
     */
    private String result;

    /**
     * A string that holds the start date.
     */
    private String date_started;

    /**
     * An int that holds the pattern type of the date format.
     */
    private  int patternType;

    /**
     * A long that holds the elapsed days use to identify days elapsed.
     */
    private long elapsed_day;

    /**
     * A Date object that holds the start date, current_date (prior).
     */
    private Date start_date, current_date;

    /**
     * Constructs a new DateTimeElapsedUtil with a given start date and a pattern type of 0.
     *
     * @param date_started the start date: pattern EEEE, dd MMMM yyyy hh:mm:ss a
     */
    public DateTimeElapsedUtil(String date_started) {
        this.date_started = date_started;
        patternType = 0;
    }

    /**
     * Constructs a new DateTimeElapsedUtil with a given start date and pattern type.
     * @param date_started the start date
     * @param patternType the pattern type, 0 | 1
     */
    public DateTimeElapsedUtil(String date_started, int patternType) {
        this.date_started = date_started;
        this.patternType = patternType;
    }

    /**
     * Calculates the elapsed time between the start date and the current date.
     */
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

    /**
     * Converts the start date and current date to date objects.
     */
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

    /**
     * Formats the result of the elapsed time calculation.
     *
     * @param y the number of elapsed years
     * @param d the number of elapsed days
     * @param h the number of elapsed hours
     * @param m the number of elapsed minutes
     * @param s the number of elapsed seconds
     */
    private void formatResult(long y, long d, long h, long m, long s) {
        result = ((y == 0 ? "" : y + ((y > 1) ? y + " Year's " : y + " Years "))
                + (d == 0 ? "" : (d > 1) ? d + " Days " : d + " Day ")
                + (h == 0 ? "" : (h > 1 )? h + " Hours " : h + "Hour ")
                + (m == 0 ? "" : (m > 1) ? m + " Minutes " : m + " Minute ")
                + (s == 0 ? "" : (s > 1) ? s + " Seconds" : s + " Second")
        ).trim();
    }

    public void setDate_started(String date_started) {
        this.date_started = date_started;
    }

    public Date getStart_date() {
        return start_date;
    }

    public long getElapsed_day() {
        return elapsed_day;
    }

    public String getResult() {
        return result;
    }
}
