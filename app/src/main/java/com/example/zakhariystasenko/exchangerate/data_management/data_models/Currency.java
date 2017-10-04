package com.example.zakhariystasenko.exchangerate.data_management.data_models;

import com.google.gson.annotations.SerializedName;

public class Currency {
    @SerializedName("exchangedate")
    private String mExchangeDate;
    @SerializedName("cc")
    private String mCurrencyId;
    @SerializedName("txt")
    private String mCurrencyName;
    @SerializedName("rate")
    private float mCurrencyRate;

    public String getExchangeDate() {
        return mExchangeDate;
    }

    public void setExchangeDate(String exchangeDate) {
        mExchangeDate = exchangeDate;
    }

    public String getCurrencyName() {
        return mCurrencyName;
    }

    public String getCurrencyId() {
        return mCurrencyId;
    }

    public Float getCurrencyRate() {
        return mCurrencyRate;
    }

    public void setCurrencyName(String currencyName) {
        mCurrencyName = currencyName;
    }

    public void setCurrencyId(String currencyId) {
        mCurrencyId = currencyId;
    }

    public void setCurrencyRate(float currencyRate) {
        mCurrencyRate = currencyRate;
    }
}
