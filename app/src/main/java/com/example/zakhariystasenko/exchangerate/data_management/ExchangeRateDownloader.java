package com.example.zakhariystasenko.exchangerate.data_management;

import com.example.zakhariystasenko.exchangerate.rate.Currency;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExchangeRateDownloader {
    @GET("exchange?json")
    Observable<ArrayList<Currency>> getCurrentData(@Query("date") String date);
}
