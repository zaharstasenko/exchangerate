package com.example.zakhariystasenko.exchangerate.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

public class GraphView extends View {
    private Paint mPaint = new Paint();
    private List<Double> mData;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int min = Math.min(getMeasuredHeight(), getMeasuredWidth());

        setMeasuredDimension(min, min);
    }

    public GraphView(Context context, AttributeSet set) {
        super(context, set);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
    }

    public void setData(List<Double> data) {
        mData = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mData != null) {
            float interval = (float) canvas.getWidth() / (float) (mData.size() - 1);
            float scaleFactor = canvas.getHeight() / getMax();

            for (int i = 0; i < mData.size() - 1; ++i) {
                float startX = i * interval;
                float startY = canvas.getHeight() - mData.get(i).floatValue() * scaleFactor;
                float endX = (i + 1) * interval;
                float endY = canvas.getHeight() - mData.get(i + 1).floatValue() * scaleFactor;

                canvas.drawLine(startX, startY, endX, endY, mPaint);
            }
        } else {
            canvas.drawPaint(mPaint);
        }
    }

    private float getMax() {
        float maxValue = 0f;

        for (Double value : mData) {
            if (maxValue < value) {
                maxValue = value.floatValue();
            }
        }

        return maxValue;
    }
}
