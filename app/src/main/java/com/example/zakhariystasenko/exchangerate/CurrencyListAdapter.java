package com.example.zakhariystasenko.exchangerate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.ViewHolder>{
    private ArrayList<Currency> mCurrencyData = new ArrayList<>();
    private Map<String,Integer> mImages = getImages();
    private Context mContext;

    CurrencyListAdapter(Context context){
        mContext = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mCurrencyName;
        private ImageView mCurrencyImage;
        private TextView mCurrencyId;
        private TextView mCurrencyRate;

        ViewHolder(View v) {
            super(v);
            mCurrencyName = v.findViewById(R.id.currency_name);
            mCurrencyImage = v.findViewById(R.id.currency_image);
            mCurrencyId = v.findViewById(R.id.currency_id);
            mCurrencyRate = v.findViewById(R.id.currency_rate);
        }
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
    public CurrencyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Currency currency = mCurrencyData.get(position);

        holder.mCurrencyName.setText(currency.getCurrencyName());
        Picasso.with(mContext).load(mImages.get(currency.getCurrencyId())).fit().into(holder.mCurrencyImage);
        holder.mCurrencyId.setText(currency.getCurrencyId());
        holder.mCurrencyRate.setText(currency.getCurrencyRate().toString());
    }

    private Map<String,Integer> getImages(){
        Map<String,Integer> images = new HashMap<>();

        images.put("DZD",R.drawable.dzd);
        images.put("AZN",R.drawable.azn);
        images.put("AUD",R.drawable.aud);
        images.put("BDT",R.drawable.bdt);
        images.put("AMD",R.drawable.amd);
        images.put("BGN",R.drawable.bgn);
        images.put("CAD",R.drawable.cad);
        images.put("CLP",R.drawable.clp);
        images.put("CNY",R.drawable.cny);
        images.put("HRK",R.drawable.hrk);
        images.put("CZK",R.drawable.czk);
        images.put("DKK",R.drawable.dkk);
        images.put("HKD",R.drawable.hkd);
        images.put("HUF",R.drawable.huf);
        images.put("ISK",R.drawable.isk);
        images.put("INR",R.drawable.inr);
        images.put("IDR",R.drawable.idr);
        images.put("IRR",R.drawable.irr);
        images.put("IQD",R.drawable.iqd);
        images.put("ILS",R.drawable.ils);
        images.put("GEL",R.drawable.gel);
        images.put("JPY",R.drawable.jpy);
        images.put("KZT",R.drawable.kzt);
        images.put("KRW",R.drawable.krw);
        images.put("KWD",R.drawable.kwd);
        images.put("KGS",R.drawable.kgs);
        images.put("LBP",R.drawable.lbp);
        images.put("LYD",R.drawable.lyd);
        images.put("MYR",R.drawable.myr);
        images.put("MXN",R.drawable.mxn);
        images.put("MNT",R.drawable.mnt);
        images.put("MDL",R.drawable.mdl);
        images.put("MAD",R.drawable.mad);
        images.put("NZD",R.drawable.nzd);
        images.put("NOK",R.drawable.nok);
        images.put("PKR",R.drawable.pkr);
        images.put("PEN",R.drawable.pen);
        images.put("RON",R.drawable.ron);
        images.put("PLN",R.drawable.pln);
        images.put("BRL",R.drawable.brl);
        images.put("TJS",R.drawable.tjs);
        images.put("RUB",R.drawable.rub);
        images.put("RSD",R.drawable.rsd);
        images.put("BYN",R.drawable.byn);
        images.put("SAR",R.drawable.sar);
        images.put("SGD",R.drawable.sgd);
        images.put("VND",R.drawable.vnd);
        images.put("ZAR",R.drawable.zar);
        images.put("SEK",R.drawable.sek);
        images.put("CHF",R.drawable.chf);
        images.put("SYP",R.drawable.syp);
        images.put("THB",R.drawable.thb);
        images.put("AED",R.drawable.aed);
        images.put("TND",R.drawable.tnd);
        images.put("TRY",R.drawable.try_currency);
        images.put("TMT",R.drawable.tmt);
        images.put("EGP",R.drawable.egp);
        images.put("GBP",R.drawable.gbp);
        images.put("USD",R.drawable.usd);
        images.put("UZS",R.drawable.uzs);
        images.put("TWD",R.drawable.twd);
        images.put("XOF",R.drawable.xof);
        images.put("XAU",R.drawable.xau);
        images.put("XDR",R.drawable.xdr);
        images.put("XAG",R.drawable.xag);
        images.put("XPT",R.drawable.xpt);
        images.put("XPD",R.drawable.xpd);
        images.put("EUR",R.drawable.eur);

        return images;
    }
}
