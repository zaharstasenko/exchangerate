package com.example.zakhariystasenko.exchangerate.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class MyDate implements Serializable {
    private Date mDate;

    private static final SimpleDateFormat RETROFIT_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat DATABASE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public static Iterable<MyDate> range(MyDate startDate, MyDate endDate) {
        int period = countDaysBetween(startDate,endDate);
        ArrayList<MyDate> requiredData = new ArrayList<>();

        LocalDate localDate = new LocalDate(endDate.getDate());

        while (period-- > 0){
            requiredData.add(new MyDate(localDate.minusDays(period).toDate()));
        }

        return requiredData;
    }

    private static int countDaysBetween(MyDate startDate, MyDate endDate){
        return Days.daysBetween(new LocalDate(startDate.getDate()), new LocalDate(endDate.getDate())).getDays();
    }

    @Override
    public String toString() {
        return mDate.toString();
    }

    public MyDate(Date date) {
        mDate = date;
    }

    public String getDateForRetrofit() {
        return RETROFIT_FORMAT.format(mDate);
    }

    public String getDateForDatabase() {
        return DATABASE_FORMAT.format(mDate);
    }

    public Date getDate() {
        return mDate;
    }
}
