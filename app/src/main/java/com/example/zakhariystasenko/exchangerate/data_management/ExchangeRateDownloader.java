package com.example.zakhariystasenko.exchangerate.data_management;

import com.example.zakhariystasenko.exchangerate.data_management.data_models.Currency;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExchangeRateDownloader {
    @GET("exchange?json")
    Single<ArrayList<Currency>> getCurrentDataNormal(@Query("date") String date);
}
