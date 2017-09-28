package com.example.zakhariystasenko.exchangerate.rate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zakhariystasenko.exchangerate.data_management.DataManager;
import com.example.zakhariystasenko.exchangerate.graph.GraphViewActivity;
import com.example.zakhariystasenko.exchangerate.root.MyApplication;
import com.example.zakhariystasenko.exchangerate.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

public class RateViewActivity extends Activity implements CurrencyListAdapter.Callback,
        DataManager.DayRequestCallback {
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

        mDataManager.notifyActivityStateChange(RateViewActivity.class.getSimpleName(), true);
        mDataManager.requestDataForDay(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDataManager.notifyActivityStateChange(RateViewActivity.class.getSimpleName(), false);
        mDataManager.disposeApiCalls();
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

    @Override
    public void getData(ArrayList<Currency> data) {
        mAdapter.setCurrencyData(data);
    }
}
