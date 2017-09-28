package com.example.zakhariystasenko.exchangerate.modules;

import android.content.Context;

import com.example.zakhariystasenko.exchangerate.data_management.DataBaseHelper;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseHelperModule {
    @Provides
    public DataBaseHelper provideHelper(Context context){
        return new DataBaseHelper(context);
    }
}
