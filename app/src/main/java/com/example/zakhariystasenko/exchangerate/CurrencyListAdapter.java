package com.example.zakhariystasenko.exchangerate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

class CurrencyListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<Currency> mCurrencyData = new ArrayList<>();
    private Map<String, Integer> mImages = new ImagesData().getImages();
    private Picasso mPicasso;

    CurrencyListAdapter(Picasso picasso) {
        mPicasso = picasso;
    }

    void setCurrencyData(ArrayList<Currency> currencyData) {
        mCurrencyData = currencyData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCurrencyData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Currency currency = mCurrencyData.get(position);

        holder.mCurrencyName.setText(currency.getCurrencyName());
        holder.mCurrencyId.setText(currency.getCurrencyId());
        holder.mCurrencyRate.setText(currency.getCurrencyRate().toString());

        mPicasso.load(mImages.get(currency.getCurrencyId())).fit().into(holder.mCurrencyImage);
    }
}
