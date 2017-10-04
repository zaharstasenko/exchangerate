package com.example.zakhariystasenko.exchangerate.graph;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.zakhariystasenko.exchangerate.R;
import com.example.zakhariystasenko.exchangerate.data_management.data_models.PeriodExchangeRate;
import com.example.zakhariystasenko.exchangerate.data_management.data_models.CurrencyId;
import com.example.zakhariystasenko.exchangerate.data_management.DataManager;
import com.example.zakhariystasenko.exchangerate.utils.MyDate;
import com.example.zakhariystasenko.exchangerate.root.MyApplication;
import com.example.zakhariystasenko.exchangerate.utils.SimpleSingleObserver;

import org.joda.time.LocalDate;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GraphViewActivity extends Activity {
    private static final String ID_KEY = "id";
    private static final String RATE_DATA_KEY = "rate data";

    private PeriodExchangeRate mPeriodExchangeRate;

    @Inject
    public DataManager mDataManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view_layout);

        MyApplication.injector(this).inject(this);

        if (savedInstanceState == null) {
            requestData();
        } else {
            restoreData(savedInstanceState);
        }
    }

    private void requestData() {
        mDataManager.exchangeRateForPeriod(new CurrencyId(getIntent().getStringExtra(ID_KEY)),
                new MyDate(new LocalDate().minusDays(30).toDate()), new MyDate(new Date()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSingleObserver<PeriodExchangeRate>() {
                    @Override
                    public void onSuccess(PeriodExchangeRate value) {
                        mPeriodExchangeRate = value;
                        setDescriptionData();
                        drawGraph();
                    }
                });
    }

    private void restoreData(Bundle savedInstanceState) {
        mPeriodExchangeRate = (PeriodExchangeRate) savedInstanceState.getSerializable(RATE_DATA_KEY);
        setDescriptionData();
        drawGraph();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(RATE_DATA_KEY, mPeriodExchangeRate);
    }

    public void drawGraph() {
        GraphView graphView = findViewById(R.id.graph);
        graphView.setData(mPeriodExchangeRate.getData());
    }

    void setDescriptionData() {
        float minValue = Float.MAX_VALUE;
        float maxValue = Float.MIN_VALUE;

        for (Float value : mPeriodExchangeRate.getData()) {
            if (minValue > value) {
                minValue = value;
            }

            if (maxValue < value) {
                maxValue = value;
            }
        }

        GraphDescriptionView graphDescriptionView = findViewById(R.id.graph_description_view);

        graphDescriptionView.setPeriodDescription(
                mPeriodExchangeRate.getCurrencyId(),
                mPeriodExchangeRate.getPeriod());
        graphDescriptionView.setMinRate(minValue);
        graphDescriptionView.setMaxRate(maxValue);
    }

    public static Bundle getStartBundle(String currencyId) {
        Bundle bundle = new Bundle();
        bundle.putString(ID_KEY, currencyId);

        return bundle;
    }
}
