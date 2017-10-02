package com.example.zakhariystasenko.exchangerate.data_management;

import com.example.zakhariystasenko.exchangerate.data_management.data_models.CurrencyId;
import com.example.zakhariystasenko.exchangerate.data_management.data_models.DailyExchangeRate;
import com.example.zakhariystasenko.exchangerate.data_management.data_models.PeriodExchangeRate;
import com.example.zakhariystasenko.exchangerate.utils.MyDate;

import io.reactivex.Single;

interface IDataManger {
    Single<DailyExchangeRate> exchangeRateForDate(MyDate date);
    Single<PeriodExchangeRate> exchangeRateForPeriod(CurrencyId currencyId, MyDate startDate, MyDate endDate);
}
