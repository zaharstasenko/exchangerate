package com.example.zakhariystasenko.exchangerate.data_management;

import com.example.zakhariystasenko.exchangerate.data_management.data_models.DailyExchangeRate;
import com.example.zakhariystasenko.exchangerate.utils.MyDate;

import io.reactivex.Single;

interface IDatabase {
    Single<DailyExchangeRate> exchangeRateForDate(MyDate date);
    void writeExchangeRateForDate(MyDate date, DailyExchangeRate model);
}
