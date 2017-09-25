package com.example.zakhariystasenko.exchangerate;

import android.content.Context;

import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
class PicassoModule {
    private Context mContext;

    PicassoModule(Context context){
        mContext = context;
    }

    @Provides
    Picasso providePicasso(){
        return Picasso.with(mContext);
    }
}
