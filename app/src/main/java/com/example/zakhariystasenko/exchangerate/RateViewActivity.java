package com.example.zakhariystasenko.exchangerate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RateViewActivity extends Activity {
    private static final String BANK_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/";
    private CurrencyListAdapter mAdapter = new CurrencyListAdapter(this);
    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_view_layout);

        initializeList();
        setDataToAdapter();
    }

    private void setDataToAdapter(){
        ArrayList<Currency> data = dataBaseHelper.getCurrentRateFromDataBase();

        if (data != null) {
            mAdapter.setCurrencyData(data);
        } else {
            downloadCurrentExchangeRate();
        }
    }

    private void initializeList() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(mAdapter);
    }

    private void downloadExchangeRateData(ArrayList<String> dates) {
        for (int i = 0; i < dates.size(); ++i) {
            DownloadProvider.createDownloader(BANK_URL).getCurrentData(dates.get(i))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createObserver());
        }
    }

    private Observer<ArrayList<Currency>> createObserver() {
        return new EmptyObserver<ArrayList<Currency>>() {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(ArrayList<Currency> value) {
                //dataBaseHelper.writeToDatabase(value);

                if (mAdapter.getItemCount() == 0) {
                    mAdapter.setCurrencyData(value);
                }

                mDisposable.dispose();
            }
        };
    }

    void downloadCurrentExchangeRate() {
        ArrayList<String> necessaryDate = new ArrayList<>();
        necessaryDate.add(DateConverter.getDateYYYYMMDD());
        downloadExchangeRateData(necessaryDate);
    }
}
