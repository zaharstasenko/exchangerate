package com.example.zakhariystasenko.exchangerate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RateViewActivity extends Activity {
    private static final String BANK_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/";
    private CurrencyListAdapter mAdapter = new CurrencyListAdapter();
    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_view_layout);

        initializeList();

        ArrayList<Currency> data = dataBaseHelper.getCurrentRateFromDataBase();

        if (data != null) {
            mAdapter.setCurrencyData(data);
            mAdapter.notifyDataSetChanged();
        } else {
            downloadCurrentExchangeRate();
        }
    }

    private void initializeList() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(mAdapter);
    }

    private ExchangeRateDownloader createDownloader() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BANK_URL)
                .build()
                .create(ExchangeRateDownloader.class);
    }

    private void downloadExchangeRateData(ArrayList<String> dates) {
        for (int i = 0; i < dates.size(); ++i) {
            createDownloader().getCurrentData("exchange?" + "date=" + dates.get(i) + "&json")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createObserver());
        }
    }

    private Observer<ArrayList<Map<String, Object>>> createObserver() {
        return new Observer<ArrayList<Map<String, Object>>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(ArrayList<Map<String, Object>> value) {
                dataBaseHelper.writeToDatabase(value);

                if (mAdapter.getItemCount() == 0) {
                    mAdapter.setCurrencyData(convertDownloadedData(value));
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private ArrayList<Currency> convertDownloadedData(ArrayList<Map<String, Object>> data) {
        ArrayList<Currency> currencies = new ArrayList<>();

        for (Map<String, Object> item : data) {
            currencies.add(convertDownloadedItem(item));
        }

        return currencies;
    }

    private Currency convertDownloadedItem(Map<String, Object> item) {
        Map<String, Integer> currencyImages = initializeImageMap();
        Currency currentCurrency = new Currency();

        for (String key : item.keySet()) {
            String value = item.get(key).toString();

            switch (key) {
                case "cc":
                    currentCurrency.setCurrencyId(value);
                    if (currencyImages.get(value) != null) {
                        currentCurrency.setCurrencyImageId(currencyImages.get(value));
                    }
                    break;
                case "rate":
                    currentCurrency.setCurrencyRate(Double.parseDouble(value));
                    break;
                case "txt":
                    currentCurrency.setCurrencyName(value);
                    break;

                default:
                    break;
            }
        }

        return currentCurrency;
    }

    private Map<String, Integer> initializeImageMap() {
        Map<String, Integer> currencyImages = new HashMap<>();

        currencyImages.put("USD", R.drawable.dollar_image);
        currencyImages.put("EUR", R.drawable.euro_image);
        currencyImages.put("RUB", R.drawable.ruble_image);

        return currencyImages;
    }

    private String dateToYYYYMMDD(int currentDay, int currentMonth, int currentYear) {
        String day;
        String month;

        if (currentMonth < 10) {
            month = "0" + currentMonth;
        } else {
            month = "" + currentMonth;
        }

        if (currentDay < 10) {
            day = "0" + currentDay;
        } else {
            day = "" + currentDay;
        }

        return currentYear + month + day;
    }

    static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1)
                + "." + calendar.get(Calendar.YEAR);
    }

    void downloadCurrentExchangeRate() {
        ArrayList<String> necessaryDate = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        necessaryDate.add(dateToYYYYMMDD(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR)));
        downloadExchangeRateData(necessaryDate);
    }
}
