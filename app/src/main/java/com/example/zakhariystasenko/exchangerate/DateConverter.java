package com.example.zakhariystasenko.exchangerate;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

class DateConverter {
    static String getDateYYYYMMDD() {
        Calendar calendar = Calendar.getInstance();

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        String dayDD;
        String monthMM;

        if (currentMonth < 10) {
            monthMM = "0" + currentMonth;
        } else {
            monthMM = "" + currentMonth;
        }

        if (currentDay < 10) {
            dayDD = "0" + currentDay;
        } else {
            dayDD = "" + currentDay;
        }

        return calendar.get(Calendar.YEAR) + monthMM + dayDD;
    }

    static String getDateYYYYMMDD(String dateDD_MM_YYYY){
        String[] dateParts = dateDD_MM_YYYY.split(".");
        return dateParts[2] + dateParts[1] + dateParts[0];
    }

    static String getDateDD_MM_YYYY() {
        Calendar calendar = Calendar.getInstance();

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        String dayDD;
        String monthMM;

        if (currentMonth < 10) {
            monthMM = "0" + currentMonth;
        } else {
            monthMM = "" + currentMonth;
        }

        if (currentDay < 10) {
            dayDD = "0" + currentDay;
        } else {
            dayDD = "" + currentDay;
        }


        return dayDD + "." + monthMM
                + "." + calendar.get(Calendar.YEAR);
    }

    static ArrayList<String> getLast30DatesDD_MM_YYYY(){
        ArrayList<String> requiredData = new ArrayList<>();

        GregorianCalendar calendar =
                (GregorianCalendar) GregorianCalendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        while (requiredData.size() < 30 && day > 0){
            requiredData.add(day-- + "." + month + "." + year);
        }

        if (month == Calendar.JANUARY + 1){
            month = Calendar.DECEMBER + 1;
            --year;
        } else {
            --month;
        }

        if (month == Calendar.APRIL + 1 || month == Calendar.JUNE + 1
                || month == Calendar.SEPTEMBER + 1 || month == Calendar.NOVEMBER + 1){
            day = 30;
        } else if (month == Calendar.FEBRUARY){
            if (calendar.isLeapYear(year)){
                day = 29;
            } else {
                day = 28;
            }
        } else {
            day = 31;
        }

        while (requiredData.size() < 30 && day > 0){
            requiredData.add(day-- + "." + month + "." + year);
        }

        return requiredData;
    }
}
