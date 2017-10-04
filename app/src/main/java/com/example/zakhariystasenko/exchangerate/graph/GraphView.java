package com.example.zakhariystasenko.exchangerate.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GraphView extends ViewGroup {
    private static final int SPLITTING_LINES_COUNT = 5;
    private Paint mPaint = new Paint();
    private List<Float> mData;

    private float mPadding;
    private float mStep;

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

        setWillNotDraw(false);

        for (int i = 0; i <= SPLITTING_LINES_COUNT; ++i) {
            addView(new TextView(context));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mPadding = r * 0.1f;
        mStep = r * 0.2f;

        for (int i = 0; i < getChildCount(); ++i) {
            int val = (int) (mPadding + i * mStep);
            getChildAt(i).layout(0, val, val + 100, val + 100);
        }
    }

    private void setValuesToGraphView() {
        Float max = getMax();
        Float step = (getMax() - getMin()) / (getChildCount() - 2);

        if (step != 0) {
            for (int i = 0; i < getChildCount(); ++i) {
                ((TextView) getChildAt(i)).setText(((Float) (max - step * i))
                        .toString()
                        .substring(0, 5));
            }
        } else {
            ((TextView) getChildAt(0)).setText(max.toString().substring(0, 5));
        }
    }

    public void setData(List<Float> data) {
        mData = data;
        invalidate();

        setValuesToGraphView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mData != null) {
            drawBackgroundWeb(canvas);
            drawGraphLine(canvas);
        }
    }

    private void drawGraphLine(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);

        float interval = countInterval(canvas);
        float scaleFactor = countScaleFactor(canvas);

        float max = getMax();

        for (int i = 0; i < mData.size() - 1; ++i) {
            float startX = i * interval * 0.8f + mPadding;
            float startY = (max - mData.get(i)) * scaleFactor + mPadding;
            float endX = (i + 1) * interval * 0.8f + mPadding;
            float endY = (max - mData.get(i + 1)) * scaleFactor + mPadding;

            canvas.drawLine(startX, startY, endX, endY, mPaint);
        }
    }

    private void drawBackgroundWeb(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);

        for (int i = 0; i <= SPLITTING_LINES_COUNT; ++i) {
            float val = mPadding + mStep * i;

            canvas.drawLine(val, 0, val, canvas.getHeight(), mPaint);
            canvas.drawLine(0, val, canvas.getWidth(), val, mPaint);
        }
    }

    private float countInterval(Canvas canvas) {
        return canvas.getWidth() / (float) (mData.size() - 1);
    }

    private float countScaleFactor(Canvas canvas) {
        return getMax() - getMin() == 0 ? 0 : canvas.getHeight() * 0.8f / (getMax() - getMin());
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

    private float getMin() {
        float minValue = Float.MAX_VALUE;

        for (Float value : mData) {
            if (minValue > value) {
                minValue = value;
            }
        }

        return minValue;
    }
}
