package com.example.zakhariystasenko.exchangerate.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyDate {
    private Date mDate;

    private static SimpleDateFormat yyyyMMddFormat = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat dd_MM_yyyyFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static Iterable<MyDate> range(MyDate startDate, MyDate endDate) {
        int period = countDays(startDate,endDate);

        ArrayList<MyDate> requiredData = new ArrayList<>();

        GregorianCalendar calendar =
                (GregorianCalendar) GregorianCalendar.getInstance();

        String[] dateData = endDate.getDateDD_MM_YYYY().split("\\.");

        int day = Integer.parseInt(dateData[0]);
        int month = Integer.parseInt(dateData[1]);
        int year = Integer.parseInt(dateData[2]);

        while (requiredData.size() < period) {
            while (requiredData.size() < period && day > 0) {
                try{
                requiredData.add(new MyDate(dd_MM_yyyyFormat.parse(day-- + "." + month + "." + year)));
                } catch (ParseException e){
                    Log.d("Error", e.toString());
                }
            }

            if (month == Calendar.JANUARY + 1) {
                month = Calendar.DECEMBER + 1;
                --year;
            } else {
                --month;
            }

            if (month == Calendar.APRIL + 1 || month == Calendar.JUNE + 1
                    || month == Calendar.SEPTEMBER + 1 || month == Calendar.NOVEMBER + 1) {
                day = 30;
            } else if (month == Calendar.FEBRUARY) {
                if (calendar.isLeapYear(year)) {
                    day = 29;
                } else {
                    day = 28;
                }
            } else {
                day = 31;
            }
        }

        return requiredData;
    }

    private static int countDays(MyDate startDate, MyDate endDate){
        final long MILLISECONDS_PER_DAY = 86400000;

        return (int)((endDate.mDate.getTime() - startDate.mDate.getTime()) / MILLISECONDS_PER_DAY);
    }

    @Override
    public String toString() {
        return mDate.toString();
    }

    public MyDate(Date date) {
        mDate = date;
    }

    public String getDateYYYYMMDD() {
        return yyyyMMddFormat.format(mDate);
    }

    public String getDateDD_MM_YYYY() {
        return dd_MM_yyyyFormat.format(mDate);
    }

}
