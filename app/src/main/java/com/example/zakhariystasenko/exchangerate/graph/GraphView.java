package com.example.zakhariystasenko.exchangerate.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GraphView extends View {
    private Paint mPaint = new Paint();

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

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mPaint);
    }
}
