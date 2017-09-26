package com.example.zakhariystasenko.exchangerate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class RateViewActivity extends Activity implements CurrencyListAdapter.Callback {
    private CurrencyListAdapter mAdapter;
    static DataManager mDataManager;

    @Inject
    ExchangeRateDownloader mDownloader;

    @Inject
    Picasso mPicasso;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_view_layout);

        DaggerRateViewActivityInjector.builder().picassoModule(new PicassoModule(this))
                .build()
                .inject(this);

        initializeList();
        initializeDataManager();

        mDataManager.rateViewActivityIsRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDataManager.rateViewActivityIsRunning = false;
        mDataManager.disposeApiCalls();
    }

    @Inject
    void initializeList() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new CurrencyListAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    void initializeDataManager(){
        mDataManager = new DataManager(new DataBaseHelper(this), mDownloader, mAdapter);
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
