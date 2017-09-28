package com.example.zakhariystasenko.exchangerate.root;

import android.app.Application;
import android.content.Context;

import com.example.zakhariystasenko.exchangerate.modules.ContextModule;

public class MyApplication extends Application {
    private Injector mInjector;

    public static Injector injector(Context context) {
        return ((MyApplication) context.getApplicationContext()).mInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInjector = DaggerInjector.builder()
                .contextModule(new ContextModule(this))
                .build();
    }
}
