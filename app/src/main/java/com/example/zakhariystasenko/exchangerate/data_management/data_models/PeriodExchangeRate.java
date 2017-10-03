package com.example.zakhariystasenko.exchangerate.data_management.data_models;

import com.example.zakhariystasenko.exchangerate.utils.MyDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PeriodExchangeRate implements Serializable {
    private CurrencyId mCurrencyId;
    private List<Double> mData = new ArrayList<>();
    private MyDate mStartDate;
    private MyDate mEndDate;

    public PeriodExchangeRate(CurrencyId currencyId, MyDate startDate, MyDate endDate, List<DailyExchangeRate> dailyExchangeRates) {
        mCurrencyId = currencyId;
        mStartDate = startDate;
        mEndDate = endDate;

        Map<Integer, Double> sortingBuffer = new TreeMap<>();

        for (DailyExchangeRate dailyExchangeRate : dailyExchangeRates) {
            sortingBuffer.put(Integer.parseInt(dailyExchangeRate.getDate().getDateForRetrofit()),
                    dailyExchangeRate.getRateById(mCurrencyId.getId()));
        }

        for (Double value : sortingBuffer.values()) {
            mData.add(value);
        }
    }

    public List<Double> getData() {
        return mData;
    }

    public String getPeriod() {
        return mStartDate.getDateForDatabase() + " - " + mEndDate.getDateForDatabase();
    }

    public String getCurrencyId() {
        return mCurrencyId.getId();
    }
}
