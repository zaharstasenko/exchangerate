package com.example.zakhariystasenko.exchangerate.data_management;

import com.example.zakhariystasenko.exchangerate.data_management.data_models.CurrencyId;
import com.example.zakhariystasenko.exchangerate.data_management.data_models.DailyExchangeRate;
import com.example.zakhariystasenko.exchangerate.data_management.data_models.PeriodExchangeRate;
import com.example.zakhariystasenko.exchangerate.data_management.data_models.Currency;
import com.example.zakhariystasenko.exchangerate.utils.MyDate;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class DataManager implements IDataManger {
    private DataBaseHelper mDataBaseHelper;
    private ExchangeRateDownloader mDownloader;

    public DataManager(DataBaseHelper dataBaseHelper, ExchangeRateDownloader downloader) {
        mDataBaseHelper = dataBaseHelper;
        mDownloader = downloader;
    }

    @Override
    public Single<DailyExchangeRate> exchangeRateForDate(final MyDate date) {
        return mDataBaseHelper
                .exchangeRateForDate(date)
                .onErrorResumeNext(mDownloader
                                .getCurrentDataNormal(date.getDateYYYYMMDD())
                                .map(new Function<ArrayList<Currency>, DailyExchangeRate>() {
                                    @Override
                                    public DailyExchangeRate apply(ArrayList<Currency> currencies) throws Exception {
                                        DailyExchangeRate dailyExchangeRate = new DailyExchangeRate(date, currencies);

                                        mDataBaseHelper.writeExchangeRateForDate(date, dailyExchangeRate);

                                        return dailyExchangeRate;
                                    }
                                }));
    }

    @Override
    public Single<PeriodExchangeRate> exchangeRateForPeriod(final CurrencyId currencyId, final MyDate startDate, final MyDate endDate) {
        return Observable
                .fromIterable(MyDate.range(startDate, endDate))
                .flatMap(new Function<MyDate, ObservableSource<DailyExchangeRate>>() {
                    @Override
                    public ObservableSource<DailyExchangeRate> apply(MyDate myDate) throws Exception {
                        return exchangeRateForDate(myDate).toObservable();
                    }
                })
                .toList()
                .map(new Function<List<DailyExchangeRate>, PeriodExchangeRate>() {
                    @Override
                    public PeriodExchangeRate apply(List<DailyExchangeRate> dailyExchangeRates) throws Exception {
                        return new PeriodExchangeRate(currencyId, startDate, endDate, dailyExchangeRates);
                    }
                });
    }
}
