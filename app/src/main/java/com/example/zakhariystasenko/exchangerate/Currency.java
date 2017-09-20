package com.example.zakhariystasenko.exchangerate;

class Currency {
    private String mCurrencyId;
    private String mCurrencyName;
    private int mCurrencyImageId;
    private double mCurrencyRate;

    String getCurrencyName() {
        return mCurrencyName;
    }

    String getCurrencyId() {
        return mCurrencyId;
    }

    int getCurrencyImageId() {
        return mCurrencyImageId;
    }

    Double getCurrencyRate() {
        return mCurrencyRate;
    }

    void setCurrencyName(String currencyName) {
        this.mCurrencyName = currencyName;
    }

    void setCurrencyId(String currencyId) {
        this.mCurrencyId = currencyId;
    }

    void setCurrencyImageId(int currencyImageId) {
        this.mCurrencyImageId = currencyImageId;
    }

    void setCurrencyRate(double currencyRate) {
        this.mCurrencyRate = currencyRate;
    }
}
