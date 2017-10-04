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
    private List<Float> mData;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int dimension;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.UNSPECIFIED
                && heightMode == MeasureSpec.UNSPECIFIED) {
            throw new IllegalArgumentException();
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            dimension = MeasureSpec.getSize(heightMeasureSpec);
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            dimension = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);

            dimension = Math.min(width, height);
        }

        setMeasuredDimension(dimension, dimension);
    }

    public GraphView(Context context, AttributeSet set) {
        super(context, set);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
    }

    public void setData(List<Float> data) {
        mData = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mData != null) {
            float interval = countInterval(canvas);
            float scaleFactor = countScaleFactor(canvas);

            for (int i = 0; i < mData.size() - 1; ++i) {
                float startX = i * interval;
                float startY = canvas.getHeight() - mData.get(i) * scaleFactor;
                float endX = (i + 1) * interval;
                float endY = canvas.getHeight() - mData.get(i + 1) * scaleFactor;

                canvas.drawLine(startX, startY, endX, endY, mPaint);
            }
        } else {
            canvas.drawPaint(mPaint);
        }
    }

    private float countInterval(Canvas canvas) {
        return (float) canvas.getWidth() / (float) (mData.size() - 1);
    }

    private float countScaleFactor(Canvas canvas) {
        return canvas.getHeight() / getMax();
    }

    private float getMax() {
        float maxValue = Float.MIN_VALUE;

        for (Float value : mData) {
            if (maxValue < value) {
                maxValue = value;
            }
        }

        return maxValue;
    }
}
