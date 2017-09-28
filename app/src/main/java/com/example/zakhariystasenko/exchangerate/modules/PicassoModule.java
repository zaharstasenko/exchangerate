package com.example.zakhariystasenko.exchangerate.modules;

import android.content.Context;

import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class PicassoModule {
    @Provides
    public Picasso providePicasso(Context context){
        return Picasso.with(context);
    }
}
