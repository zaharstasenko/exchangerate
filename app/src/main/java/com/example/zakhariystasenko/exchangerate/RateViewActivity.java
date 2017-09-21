package com.example.zakhariystasenko.exchangerate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RateViewActivity extends Activity {
    private CurrencyListAdapter mAdapter;
    private DataBaseHelper dataBaseHelper;
    private ArrayList<Disposable> mApiCalls = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_view_layout);

        initializeDataBaseHelper();
        initializeList();
        setDataToAdapter();
    }

    @Override
    protected void onStop() {
        super.onStop();

        for (Disposable disposable : mApiCalls){
            disposable.dispose();
        }
    }

    private void initializeDataBaseHelper(){
        dataBaseHelper = new DataBaseHelper(this);
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

        mAdapter = new CurrencyListAdapter(PicassoProvider.createPicasso(this));
        recyclerView.setAdapter(mAdapter);
    }

    private void downloadExchangeRateData(List<String> dates) {
        for (int i = 0; i < dates.size(); ++i) {
            DownloaderProvider.createDownloader("https://bank.gov.ua/NBUStatService/v1/statdirectory/")
                    .getCurrentData(dates.get(i))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createObserver());
        }
    }

    private Observer<ArrayList<Currency>> createObserver() {
        return new SimpleObserver<ArrayList<Currency>>() {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
                mApiCalls.add(mDisposable);
            }

            @Override
            public void onNext(ArrayList<Currency> value) {
                //dataBaseHelper.writeToDatabase(value);

                if (mAdapter.getItemCount() == 0) {
                    mAdapter.setCurrencyData(value);
                }

                mApiCalls.remove(mDisposable);
                mDisposable.dispose();
            }
        };
    }

    void downloadCurrentExchangeRate() {
        downloadExchangeRateData(Collections.singletonList(DateConverter.getDateYYYYMMDD()));
    }
}
