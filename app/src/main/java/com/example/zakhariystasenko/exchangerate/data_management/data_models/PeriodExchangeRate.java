package com.example.zakhariystasenko.exchangerate.data_management.data_models;

import com.example.zakhariystasenko.exchangerate.utils.MyDate;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PeriodExchangeRate {
    private CurrencyId mCurrencyId;
    private Map<Integer, Double> mData = new TreeMap<>();
    private MyDate mStartDate;
    private MyDate mEndDate;

    public PeriodExchangeRate(CurrencyId currencyId, MyDate startDate, MyDate endDate, List<DailyExchangeRate> dailyExchangeRates) {
        mCurrencyId = currencyId;
        mStartDate = startDate;
        mEndDate = endDate;

        for (DailyExchangeRate dailyExchangeRate : dailyExchangeRates){
            mData.put(Integer.parseInt(dailyExchangeRate.getDate().getDateYYYYMMDD()),
                    dailyExchangeRate.getRateById(mCurrencyId.getId()));
        }
    }

    public Map<Integer, Double> getData() {
        return mData;
    }
}
