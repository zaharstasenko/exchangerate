package com.example.zakhariystasenko.exchangerate;

import com.google.gson.annotations.SerializedName;

class Currency {
    @SerializedName("exchangedate")
    private String mExchangeDate;
    @SerializedName("cc")
    private String mCurrencyId;
    @SerializedName("txt")
    private String mCurrencyName;
    @SerializedName("rate")
    private double mCurrencyRate;

    String getExchangeDate() {
        return mExchangeDate;
    }

    void setExchangeDate(String exchangeDate) {
        mExchangeDate = exchangeDate;
    }

    String getCurrencyName() {
        return mCurrencyName;
    }

    String getCurrencyId() {
        return mCurrencyId;
    }

    Double getCurrencyRate() {
        return mCurrencyRate;
    }

    void setCurrencyName(String currencyName) {
        mCurrencyName = currencyName;
    }

    void setCurrencyId(String currencyId) {
        mCurrencyId = currencyId;
    }

    void setCurrencyRate(double currencyRate) {
        mCurrencyRate = currencyRate;
    }

    @Override
    public String toString() {
        return mExchangeDate + " " + mCurrencyName + " " + mCurrencyId + " " + mCurrencyRate;
    }
}
