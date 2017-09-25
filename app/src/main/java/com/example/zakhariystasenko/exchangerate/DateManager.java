package com.example.zakhariystasenko.exchangerate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

class DateManager {
    static String getDateYYYYMMDD() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.YEAR) + formatDate(calendar.get(Calendar.MONTH) + 1)
                + formatDate(calendar.get(Calendar.DAY_OF_MONTH));
    }

    static String getDateYYYYMMDD(String dateDD_MM_YYYY) {
        String[] dateParts = dateDD_MM_YYYY.split("\\.");
        return dateParts[2] + dateParts[1] + dateParts[0];
    }

    private static String formatDate(int date) {
        if (date < 10) {
            return "0" + date;
        } else {
            return "" + date;
        }
    }

    static String getDateDD_MM_YYYY() {
        Calendar calendar = Calendar.getInstance();

        return formatDate(calendar.get(Calendar.DAY_OF_MONTH)) + "." + formatDate(calendar.get(Calendar.MONTH) + 1)
                + "." + calendar.get(Calendar.YEAR);
    }

    static String getDateDD_MM_YYYY(int day, int month, int year) {
        return formatDate(day) + "." + formatDate(month) + "." + formatDate(year);
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
