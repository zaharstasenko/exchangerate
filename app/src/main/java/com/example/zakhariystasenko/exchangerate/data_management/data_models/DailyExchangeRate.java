package com.example.zakhariystasenko.exchangerate.data_management.data_models;

import com.example.zakhariystasenko.exchangerate.utils.MyDate;

import java.util.ArrayList;

public class DailyExchangeRate {
    private MyDate mDate;
    private ArrayList<Currency> mCurrencies;

    MyDate getDate() {
        return mDate;
    }

    public ArrayList<Currency> getCurrencies() {
        return mCurrencies;
    }

    public DailyExchangeRate(MyDate date, ArrayList<Currency> currencies) {
        mDate = date;
        mCurrencies = currencies;
    }

    Double getRateById(String id){
        for (Currency currency : mCurrencies){
            if (currency.getCurrencyId().equals(id)){
                return currency.getCurrencyRate();
            }
        }

        return null;
    }
}
