package com.example.zakhariystasenko.exchangerate;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

interface ExchangeRateDownloader {
    @GET
    Observable<ArrayList<Map<String, Object>>> getCurrentData(@Url String url);
}
