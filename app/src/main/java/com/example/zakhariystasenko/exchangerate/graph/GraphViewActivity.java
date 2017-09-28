package com.example.zakhariystasenko.exchangerate.graph;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.zakhariystasenko.exchangerate.R;
import com.example.zakhariystasenko.exchangerate.data_management.DataManager;
import com.example.zakhariystasenko.exchangerate.root.MyApplication;
import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Map;

import javax.inject.Inject;

public class GraphViewActivity extends Activity implements DataManager.PeriodRequestCallback {
    private static final String ID_KEY = "id";
    private String mCurrencyId;

    @Inject
    public DataManager mDataManager;

    @Override
    public void getData(Map<Integer, Double> data) {
        drawGraph(data);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.zakhariystasenko.exchangerate.R.layout.graph_view_layout);

        MyApplication.injector(this).inject(this);
        mDataManager.notifyActivityStateChange(GraphViewActivity.class.getSimpleName(), true);

        mCurrencyId = getIntent().getStringExtra(ID_KEY);
        mDataManager.requestDataForPeriod(mCurrencyId, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDataManager.notifyActivityStateChange(GraphViewActivity.class.getSimpleName(), false);
        mDataManager.disposeApiCalls();
    }

    public void drawGraph(Map<Integer, Double> data) {
        GraphView graph = findViewById(R.id.graph);
        DataPoint[] dataPoints = new DataPoint[data.size()];

        int i = 0;

        for (Double val : data.values()) {
            dataPoints[i] = new DataPoint(i++, val);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graph.setTitle(String.format(getString(R.string.graph_description), mCurrencyId));
        graph.setTitleTextSize(50);
        graph.addSeries(series);
    }

    public static Bundle getStartBundle(String currencyId) {
        Bundle bundle = new Bundle();

        bundle.putString(ID_KEY, currencyId);

        return bundle;
    }
}
