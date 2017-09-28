package com.example.zakhariystasenko.exchangerate.modules;

import com.example.zakhariystasenko.exchangerate.data_management.DataBaseHelper;
import com.example.zakhariystasenko.exchangerate.data_management.DataManager;
import com.example.zakhariystasenko.exchangerate.data_management.ExchangeRateDownloader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataManagerModule {
    @Provides
    @Singleton
    public DataManager provideDataManager(DataBaseHelper dataBaseHelper, ExchangeRateDownloader downloader){
        return new DataManager(dataBaseHelper,downloader);
    }
}
