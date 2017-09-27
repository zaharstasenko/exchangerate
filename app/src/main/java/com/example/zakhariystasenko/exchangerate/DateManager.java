package com.example.zakhariystasenko.exchangerate;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

class DateManager {
    private static SimpleDateFormat yyyyMMddFormat = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat dd_MM_yyyyFormat = new SimpleDateFormat("dd.MM.yyyy");

    static String getDateYYYYMMDD() {
        return yyyyMMddFormat.format(new Date());
    }

    static String getDateYYYYMMDD(String dateDD_MM_YYYY) {
        try {
            return yyyyMMddFormat.format(dd_MM_yyyyFormat.parse(dateDD_MM_YYYY));
        } catch (ParseException e){
            Log.d("DateManager", e.toString());
            return null;
        }
    }

    private static String formatDate(int date) {
        if (date < 10) {
            return "0" + date;
        } else {
            return "" + date;
        }
    }

    static String getDateDD_MM_YYYY() {
        return dd_MM_yyyyFormat.format(new Date());
    }

    private static String getDateDD_MM_YYYY(int day, int month, int year) {
        return formatDate(day) + "." + formatDate(month) + "." + year;
    }

    static List<String> getMissingDates(Set<String> dates) {
        List<String> period = getLast30DatesDD_MM_YYYY();
        List<String> res = new ArrayList<>();

        for (String date : dates) {
            period.remove(date);
        }

        for (String date : period) {
            res.add(getDateYYYYMMDD(date));
        }

        return res;
    }

    static ArrayList<String> getLast30DatesDD_MM_YYYY() {
        ArrayList<String> requiredData = new ArrayList<>();

        GregorianCalendar calendar =
                (GregorianCalendar) GregorianCalendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        while (requiredData.size() < 30 && day > 0) {
            requiredData.add(getDateDD_MM_YYYY(day--, month, year));
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

        while (requiredData.size() < 30 && day > 0) {
            requiredData.add(getDateDD_MM_YYYY(day--, month, year));
        }

        return requiredData;
    }
}
