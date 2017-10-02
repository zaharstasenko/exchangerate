package com.example.zakhariystasenko.exchangerate.graph;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.zakhariystasenko.exchangerate.R;
import com.example.zakhariystasenko.exchangerate.data_management.data_models.PeriodExchangeRate;
import com.example.zakhariystasenko.exchangerate.data_management.data_models.CurrencyId;
import com.example.zakhariystasenko.exchangerate.data_management.DataManager;
import com.example.zakhariystasenko.exchangerate.utils.MyDate;
import com.example.zakhariystasenko.exchangerate.root.MyApplication;
import com.example.zakhariystasenko.exchangerate.utils.SimpleSingleObserver;
import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.joda.time.LocalDate;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GraphViewActivity extends Activity {
    private static final String ID_KEY = "id";

    @Inject
    public DataManager mDataManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.zakhariystasenko.exchangerate.R.layout.graph_view_layout);

        MyApplication.injector(this).inject(this);

        mDataManager.exchangeRateForPeriod(new CurrencyId(getIntent().getStringExtra(ID_KEY)),
                new MyDate(new LocalDate().minusDays(30).toDate()), new MyDate(new Date()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSingleObserver<PeriodExchangeRate>() {
                    @Override
                    public void onSuccess(PeriodExchangeRate value) {
                        drawGraph(value);
                    }
                });
    }

    public void drawGraph(PeriodExchangeRate periodExchangeRate) {
        GraphView graph = findViewById(R.id.graph);
        DataPoint[] dataPoints = new DataPoint[periodExchangeRate.getData().size()];

        int i = 0;

        for (Double val : periodExchangeRate.getData()) {
            dataPoints[i] = new DataPoint(i++, val);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

        graph.setTitle(String.format(getString(R.string.graph_description),
                periodExchangeRate.getCurrencyId(),
                periodExchangeRate.getPeriod()));
        graph.setTitleTextSize(25);
        graph.addSeries(series);
    }

    public static Bundle getStartBundle(String currencyId) {
        Bundle bundle = new Bundle();

        bundle.putString(ID_KEY, currencyId);

        return bundle;
    }
}
