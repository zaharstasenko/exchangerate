package com.example.zakhariystasenko.exchangerate.modules;

import com.example.zakhariystasenko.exchangerate.data_management.ExchangeRateDownloader;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DownloaderModule {
    @Provides
    public ExchangeRateDownloader provideDownloader() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://bank.gov.ua/NBUStatService/v1/statdirectory/")
                .build()
                .create(ExchangeRateDownloader.class);
    }
}
