package com.example.zakhariystasenko.exchangerate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Map;

public class GraphViewActivity extends Activity implements DataManager.PeriodRequestCallback {
    private static final String ID_KEY = "id";
    private String mCurrencyId;

    @Override
    public void getData(Map<Integer, Double> data) {
        drawGraph(data);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view_layout);

        RateViewActivity.mDataManager.graphViewActivityIsRunning = true;

        mCurrencyId = getIntent().getStringExtra(ID_KEY);
        RateViewActivity.mDataManager.requestDataForPeriod(mCurrencyId, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        RateViewActivity.mDataManager.graphViewActivityIsRunning = false;
        RateViewActivity.mDataManager.disposeApiCalls();
    }

    void drawGraph(Map<Integer, Double> data) {
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

    static Bundle getStartBundle(String currencyId) {
        Bundle bundle = new Bundle();

        bundle.putString(ID_KEY, currencyId);

        return bundle;
    }
}
