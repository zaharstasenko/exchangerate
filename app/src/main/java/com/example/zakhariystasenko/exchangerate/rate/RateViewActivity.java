package com.example.zakhariystasenko.exchangerate.rate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zakhariystasenko.exchangerate.data_management.data_models.Currency;
import com.example.zakhariystasenko.exchangerate.data_management.data_models.DailyExchangeRate;
import com.example.zakhariystasenko.exchangerate.data_management.DataManager;
import com.example.zakhariystasenko.exchangerate.utils.MyDate;
import com.example.zakhariystasenko.exchangerate.graph.GraphViewActivity;
import com.example.zakhariystasenko.exchangerate.root.MyApplication;
import com.example.zakhariystasenko.exchangerate.R;
import com.example.zakhariystasenko.exchangerate.utils.SimpleSingleObserver;
import com.squareup.picasso.Picasso;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RateViewActivity extends Activity implements CurrencyListAdapter.Callback {
    private CurrencyListAdapter mAdapter;
    @Inject
    public DataManager mDataManager;
    @Inject
    public Picasso mPicasso;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_view_layout);


        MyApplication.injector(this).inject(this);
        initializeList();
        requestExchangeRate();
    }

    private void requestExchangeRate() {
        mDataManager.exchangeRateForDate(new MyDate(new Date()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createObserver());
    }

    private SingleObserver<DailyExchangeRate> createObserver() {
        return new SimpleSingleObserver<DailyExchangeRate>() {
            @Override
            public void onSuccess(DailyExchangeRate value) {
                mAdapter.setCurrencyData(value.getCurrencies());
            }
        };
    }

    private void initializeList() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new CurrencyListAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(Currency currency) {
        Intent graphActivityIntent = new Intent(this, GraphViewActivity.class);
        graphActivityIntent.putExtras(GraphViewActivity.getStartBundle(currency.getCurrencyId()));

        startActivity(graphActivityIntent);
    }

    @Override
    public Picasso getPicasso() {
        return mPicasso;
    }
}
