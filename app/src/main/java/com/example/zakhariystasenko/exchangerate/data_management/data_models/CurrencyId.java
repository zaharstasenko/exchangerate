package com.example.zakhariystasenko.exchangerate.data_management.data_models;

import java.io.Serializable;

public class CurrencyId implements Serializable {
    private String mId;

    public String getId() {
        return mId;
    }

    public CurrencyId(String id) {
        mId = id;
    }

    public void setId(String id) {
        mId = id;
    }
}
