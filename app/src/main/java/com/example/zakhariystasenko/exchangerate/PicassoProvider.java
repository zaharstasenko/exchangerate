package com.example.zakhariystasenko.exchangerate;

import android.content.Context;

import com.squareup.picasso.Picasso;

class PicassoProvider {
    static Picasso createPicasso(Context context){
        return Picasso.with(context);
    }
}
