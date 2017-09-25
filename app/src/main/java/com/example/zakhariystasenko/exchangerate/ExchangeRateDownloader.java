package com.example.zakhariystasenko.exchangerate;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface ExchangeRateDownloader {
    @GET("exchange?json")
    Observable<ArrayList<Currency>> getCurrentData(@Query("date") String date);
}
