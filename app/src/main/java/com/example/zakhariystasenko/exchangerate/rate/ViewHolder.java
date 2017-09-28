package com.example.zakhariystasenko.exchangerate.rate;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zakhariystasenko.exchangerate.R;

class ViewHolder extends RecyclerView.ViewHolder {
    TextView mCurrencyName;
    ImageView mCurrencyImage;
    TextView mCurrencyId;
    TextView mCurrencyRate;

    ViewHolder(View v) {
        super(v);
        mCurrencyName = v.findViewById(R.id.currency_name);
        mCurrencyImage = v.findViewById(R.id.currency_image);
        mCurrencyId = v.findViewById(R.id.currency_id);
        mCurrencyRate = v.findViewById(R.id.currency_rate);
    }
}
