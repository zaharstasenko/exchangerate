package com.example.zakhariystasenko.exchangerate;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.ViewHolder>{
    private ArrayList<Currency> mCurrencyData = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mCurrencyName;
        private ImageView mCurrencyImage;
        private TextView mCurrencyId;
        private TextView mCurrencyRate;

        ViewHolder(View v) {
            super(v);
            this.mCurrencyName = v.findViewById(R.id.currency_name);
            this.mCurrencyImage = v.findViewById(R.id.currency_image);
            this.mCurrencyId = v.findViewById(R.id.currency_id);
            this.mCurrencyRate = v.findViewById(R.id.currency_rate);
        }
    }

    void setCurrencyData(ArrayList<Currency> currencyData) {
        this.mCurrencyData = currencyData;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCurrencyData.size();
    }

    @Override
    public CurrencyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Currency currency = mCurrencyData.get(position);

        holder.mCurrencyName.setText(currency.getCurrencyName());
        holder.mCurrencyImage.setImageResource(currency.getCurrencyImageId());
        holder.mCurrencyId.setText(currency.getCurrencyId());
        holder.mCurrencyRate.setText(currency.getCurrencyRate().toString());
    }
}
