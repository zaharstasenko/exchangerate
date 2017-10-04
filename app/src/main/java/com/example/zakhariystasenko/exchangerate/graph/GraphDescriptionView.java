package com.example.zakhariystasenko.exchangerate.graph;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zakhariystasenko.exchangerate.R;

public class GraphDescriptionView extends LinearLayout {
    private TextView mPeriodDescription;
    private TextView mMinRate;
    private TextView mMaxRate;

    public GraphDescriptionView(Context context) {
        super(context);
        initialize();
    }

    public GraphDescriptionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public GraphDescriptionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setPeriodDescription(String id, String period) {
        mPeriodDescription.setText(getResources().getString(R.string.graph_description, id, period));
    }

    public void setMinRate(Float minValue) {
        mMinRate.setText(getResources().getString(R.string.min_value, minValue));
    }

    public void setMaxRate(Float maxValue) {
        mMaxRate.setText(getResources().getString(R.string.max_value, maxValue));
    }

    void initialize() {
        inflate(getContext(), R.layout.graph_description_layout, this);

        mPeriodDescription = findViewById(R.id.graph_description);
        mMinRate = findViewById(R.id.minValue);
        mMaxRate = findViewById(R.id.maxValue);
    }
}
